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
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class TaskServiceImpl implements ITaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public DtoTask saveTask(DtoTaskIU dtoTaskIU) {

        Task task = new Task();
        BeanUtils.copyProperties(dtoTaskIU, task);
        User user = userRepository.findById(dtoTaskIU.getUser_id())
                .orElseThrow(() -> new RuntimeException("User bulunamadı"));
        task.setUser(user);

        Task savedTask = taskRepository.save(task);

        DtoTask dtoTask = new DtoTask();
        BeanUtils.copyProperties(savedTask, dtoTask);

        return dtoTask;
    }

    @Override
    public List<DtoTask> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream().map(task -> {
            DtoTask dtoTask = new DtoTask();
            BeanUtils.copyProperties(task, dtoTask);
            return dtoTask;
        }).collect(Collectors.toList());
    }

    @Override
    public List<DtoTask> getTasksByUserId(Long userId) {
        List<Task> tasks = taskRepository.findByUser_Id(userId);

        return tasks.stream().map(task -> {
            DtoTask dtoTask = new DtoTask();
            BeanUtils.copyProperties(task, dtoTask);
            return dtoTask;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteTask(Long id) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Silinecek Görev (ID: " + id + ") bulunamadı."));
        taskRepository.delete(task);

    }

    @Override
    @Transactional
    public DtoTask updateTask(Long id, DtoTaskIU dtoTaskIU) {

        // 1. Güncellenecek görevi ID ile veritabanından çek (bulamazsa hata fırlatır)
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Güncellenecek Görev (ID: " + id + ") bulunamadı."));

        // 2. Yeni verileri mevcut görev objesine kopyala
        // Not: BeanUtils, DTO'daki title, description, status ve dueDate alanlarını kopyalar.
        BeanUtils.copyProperties(dtoTaskIU, existingTask, "id"); // "id" alanını kopyalamayı engellemek önemli

        // 3. User'ı manuel olarak tekrar ata (user_id değişmiş olabilir veya foreign key kuralı gereği)
        // Eğer DTO'da user_id varsa, ilgili User'ı çekip Task'a atamalıyız.
        // DtoTaskIU'da user_id alanının olduğunu varsayıyoruz.
        if (dtoTaskIU.getUser_id() != null) {
            // Kullanıcıyı bul
            User user = userRepository.findById(dtoTaskIU.getUser_id())
                    .orElseThrow(() -> new RuntimeException("Yeni Kullanıcı (ID: " + dtoTaskIU.getUser_id() + ") bulunamadı."));
            existingTask.setUser(user);
        }

        // 4. Güncellenmiş görevi kaydet (Hibernate/JPA otomatik olarak UPDATE sorgusu oluşturur)
        Task updatedTask = taskRepository.save(existingTask);

        // 5. Sonucu DTO olarak döndür
        DtoTask dtoTask = new DtoTask();
        BeanUtils.copyProperties(updatedTask, dtoTask);

        return dtoTask;
    }

}
