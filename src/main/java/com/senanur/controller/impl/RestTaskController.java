package com.senanur.controller.impl;

import com.senanur.controller.IRestTaskController;
import com.senanur.controller.RestBaseController;
import com.senanur.controller.RootEntity;
import com.senanur.dto.DtoTask;
import com.senanur.dto.DtoTaskIU;
import com.senanur.service.ITaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/tasks")
public class RestTaskController extends RestBaseController implements IRestTaskController {

    @Autowired
    private ITaskService taskService;
    @PostMapping("save")
    @Override
    public RootEntity<DtoTask> savedTask(@Valid @RequestBody DtoTaskIU dtoTaskIU) {
        return ok(taskService.saveTask(dtoTaskIU));
    }

    @GetMapping("/list")
    @Override
    public List<DtoTask> getAllTasks() {
        return taskService.getAllTasks();
    }

    @Override
    @GetMapping("/list/{userId}")
    public ResponseEntity<List<DtoTask>> getTasksByUserId(@PathVariable("userId") Long userId) {

        List<DtoTask> tasks = taskService.getTasksByUserId(userId);

        return ResponseEntity.ok(tasks);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable("id") Long id) {

        taskService.deleteTask(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @Override
    @PutMapping("/update/{id}")
    public ResponseEntity<DtoTask> updateTask(
            @PathVariable("id") Long id,
            @RequestBody DtoTaskIU dtoTaskIU) {

        DtoTask updatedTask = taskService.updateTask(id, dtoTaskIU);

        return ResponseEntity.ok(updatedTask);
    }


}
