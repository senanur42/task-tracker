package com.senanur.service.impl;

import com.senanur.dto.DtoTask;
import com.senanur.dto.DtoTaskIU;
import com.senanur.model.Task;
import com.senanur.model.User;
import com.senanur.repository.TaskRepository;
import com.senanur.repository.UserRepository;
import com.senanur.service.ITaskService;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class TaskServiceImpl implements ITaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    /*
      Helper metot: Oturum açmış kullanıcının adını JWT token'dan çeker.
      @return String: Token'daki "preferred_username" claim değeri oluyor.
     */
    private String getLoggedInUsername() {
        // Security Context'teki Principal (kimlik) objesini Jwt tipine dönüştürüyoruz.
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof Jwt jwt) {
            // Keycloak'tan gelen standart kullanıcı adı claim'i "preferred_username"dir.
            return jwt.getClaimAsString("preferred_username");
        }

        // Eğer kimlik doğrulama yapılmadıysa veya token formatı beklenmiyorsa hata fırlatıyoruz.
        throw new RuntimeException("Oturum açmış kullanıcı bilgisi (JWT) bulunamadı.");
    }

    /*
     Helper metot: Oturum açmış kullanıcı adını kullanarak DB'deki User objesini çeker.
     @return User: Veritabanındaki User objesi.
     */
    private User getLoggedInUser() {
        String username = getLoggedInUsername();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Kullanıcı DB'de bulunamadı: " + username));
    }


    @Override
    public DtoTask saveTask(DtoTaskIU dtoTaskIU) {

        // jwy-> keyclock dönüşümleri
        // DTO'dan gelen user_id KULLANILMAYACAK. (Güvenlik zafiyeti engellendi)
        // Kullanıcı, token'dan çekilen bilgi ile bulunacak.
        User user = getLoggedInUser();

        Task task = new Task();
        BeanUtils.copyProperties(dtoTaskIU, task);

        // Görev, token'ı gönderen kullanıcıya atanır.
        task.setUser(user);

        Task savedTask = taskRepository.save(task);

        DtoTask dtoTask = new DtoTask();
        BeanUtils.copyProperties(savedTask, dtoTask);

        // Task DTO'sunu geri döndürmeden önce kullanıcı ID'sini doldurmak isteyebilirsiniz.
        dtoTask.setUser_id(user.getId());

        return dtoTask;
    }

    @Override
    public List<DtoTask> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();

        return tasks.stream().map(task -> {
            DtoTask dtoTask = new DtoTask();
            BeanUtils.copyProperties(task, dtoTask);

            if(task.getUser() != null) {
                dtoTask.setUser_id(task.getUser().getId());
            }
            else {
                dtoTask.setUser_id(null);
            }

            return dtoTask;
        }).collect(Collectors.toList());
    }

    @Override
    public List<DtoTask> getTasksByUserId(Long userId) {
        List<Task> tasks = taskRepository.findByUser_Id(userId);

        return tasks.stream().map(task -> {
            DtoTask dtoTask = new DtoTask();
            BeanUtils.copyProperties(task, dtoTask);
            if(task.getUser() != null) {
                dtoTask.setUser_id(task.getUser().getId());
            } else {
                dtoTask.setUser_id(null);
            }
            return dtoTask;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteTask(Long id) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Silinecek Görev (ID: " + id + ") bulunamadı."));

        // 🌟 GÜVENLİK KONTROLÜ: Görevi sadece sahibi silebilir.
        User loggedInUser = getLoggedInUser();
        if (!task.getUser().getId().equals(loggedInUser.getId())) {
            throw new RuntimeException("Bu görevi silme yetkiniz yoktur.");
        }

        taskRepository.delete(task);

    }

    @Override
    @Transactional
    public DtoTask updateTask(Long id, DtoTaskIU dtoTaskIU) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Güncellenecek Görev (ID: " + id + ") bulunamadı."));

        User loggedInUser = getLoggedInUser();
        if (!existingTask.getUser().getId().equals(loggedInUser.getId())) {
            throw new RuntimeException("Bu görevi güncelleme yetkiniz yoktur.");
        }

        // KULLANICI ID'SİNİ DTO'DAN DEĞİL, GİRİŞ YAPAN KULLANICIDAN ALIYORUZ.
        // Kullanıcı ID'sini değiştirme mantığını kaldırıyoruz,
        // çünkü görevin sahibini değiştirmek için ayrı bir Admin yetkisi gerekir.
        if (dtoTaskIU.getUser_id() != null && !dtoTaskIU.getUser_id().equals(loggedInUser.getId())) {
        }

        BeanUtils.copyProperties(dtoTaskIU, existingTask, "id", "user_id"); // user_id'yi kopyalamayı engelle

        Task updatedTask = taskRepository.save(existingTask);
        DtoTask dtoTask = new DtoTask();
        BeanUtils.copyProperties(updatedTask, dtoTask);
        dtoTask.setUser_id(loggedInUser.getId());

        return dtoTask;
    }

}
