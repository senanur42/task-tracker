package com.senanur.controller;

import com.senanur.dto.DtoTask;
import com.senanur.dto.DtoTaskIU;
import com.senanur.dto.DtoTask;
import com.senanur.dto.DtoTaskIU;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface IRestTaskController {
    public RootEntity<DtoTask> savedTask(DtoTaskIU dtoTaskIU);
    List<DtoTask> getAllTasks();
    public ResponseEntity<List<DtoTask>> getTasksByUserId( Long userId);
    public ResponseEntity<Void> deleteTask(Long id);
    public ResponseEntity<DtoTask> updateTask(Long id, DtoTaskIU dtoTaskIU);
}


