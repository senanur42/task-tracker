package com.senanur.service;

import com.senanur.dto.DtoTask;
import com.senanur.dto.DtoTaskIU;

import java.util.List;

public interface ITaskService {
    public DtoTask saveTask(DtoTaskIU dtoTaskIU);
    List<DtoTask> getAllTasks();
    List<DtoTask> getTasksByUserId(Long userId);
    void deleteTask(Long id);

    DtoTask updateTask(Long id, DtoTaskIU dtoTaskIU);
}
