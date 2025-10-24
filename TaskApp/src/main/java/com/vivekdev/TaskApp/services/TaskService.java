package com.vivekdev.TaskApp.services;

import com.vivekdev.TaskApp.dto.Response;
import com.vivekdev.TaskApp.dto.TaskRequest;
import com.vivekdev.TaskApp.entity.Task;

import java.util.List;

public interface TaskService {
    Response<Task> createTask(TaskRequest taskRequest);
    Response<List<Task>> getALlMyTasks();
    Response<Task> getTaskById(Long id);
    Response<Task> updateTask(TaskRequest taskRequest);
    Response<Void> deleteTask(Long id);
    Response<List<Task>> getMyTaskByCompletionStatus(boolean completed);
    Response<List<Task>> getMyTaskByPriority(String priority);

}

