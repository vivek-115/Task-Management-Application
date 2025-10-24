package com.vivekdev.TaskApp.services.impl;

import com.vivekdev.TaskApp.dto.Response;
import com.vivekdev.TaskApp.dto.TaskRequest;
import com.vivekdev.TaskApp.entity.Task;
import com.vivekdev.TaskApp.entity.User;
import com.vivekdev.TaskApp.enums.Priority;
import com.vivekdev.TaskApp.exceptions.NotFoundException;
import com.vivekdev.TaskApp.repo.TaskRepository;
import com.vivekdev.TaskApp.services.TaskService;
import com.vivekdev.TaskApp.services.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;

    @Override
    @Transactional
    public Response<Task> createTask(TaskRequest taskRequest) {
        log.info("Inside createTask()");
       User user= userService.getCurrentLoggedInUser();
       Task tasktoSave=Task.builder()
               .title(taskRequest.getTitle())
               .description(taskRequest.getDescription())
               .completed(taskRequest.getCompleted())
               .priority(taskRequest.getPriority())
               .dueDate(taskRequest.getDueDate())
               .createdAt(LocalDateTime.now())
               .updatedAt(LocalDateTime.now())
               .user(user)
               .build();

       Task savedTask=taskRepository.save(tasktoSave);
        return Response.<Task>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Task Created Successfully")
                .data(savedTask)
                .build();
    }

    @Override
    public Response<List<Task>> getALlMyTasks() {
        log.info("Inside getAllMyTasks()");
        User currentUser = userService.getCurrentLoggedInUser();
       List<Task> tasks= taskRepository.findByUser(currentUser, Sort.by(Sort.Direction.DESC,"id"));
        return Response.<List<Task>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("task retrieved Successfully")
                .data(tasks)
                .build();
    }

    @Override
    public Response<Task> getTaskById(Long id) {
        log.info("Inside getTaskById()");
       Task task= taskRepository.findById(id)
               .orElseThrow(()->new NotFoundException("Task not Found"));

        return Response.<Task>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Task retrieved Successfully")
                .data(task)
                .build();
    }

    @Override
    public Response<Task> updateTask(TaskRequest taskRequest) {
        log.info("Inside updateTask()");
        Task task= taskRepository.findById(taskRequest.getId())
                .orElseThrow(()->new NotFoundException("Task not Found"));

        if(taskRequest.getTitle()!=null) task.setTitle(taskRequest.getTitle());
        if(taskRequest.getDescription()!=null) task.setDescription(taskRequest.getDescription());
        if(taskRequest.getCompleted()!=null) task.setCompleted(taskRequest.getCompleted());
        if(taskRequest.getPriority()!=null) task.setPriority(taskRequest.getPriority());
        if(taskRequest.getDueDate()!=null) task.setDueDate(taskRequest.getDueDate());
        task.setUpdatedAt(LocalDateTime.now());

        //updated the task in the database
        Task updatedTask=taskRepository.save(task);
        return Response.<Task>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Task Updated Successfully")
                .data(updatedTask)
                .build();
    }

    @Override
    public Response<Void> deleteTask(Long id) {
        log.info("Inside deleteTask()");
        if(!taskRepository.existsById(id)){
            throw new NotFoundException("Task Does not exits");
        }
        taskRepository.deleteById(id);

        return Response.<Void>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Task Deleted Successfully")
                .build();
    }

    @Override
    public Response<List<Task>> getMyTaskByCompletionStatus(boolean completed) {
        log.info("Inside getMyTaskByCompletionStatus()");
        User currentUser=userService.getCurrentLoggedInUser();
        List<Task> task = taskRepository.findByCompletedAndUser(completed, currentUser);
        return Response.<List<Task>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Task filtered by completion status for user")
                .data(task)
                .build();
    }

    @Override
    public Response<List<Task>> getMyTaskByPriority(String priority) {
        log.info("Inside getMyTaskByPriority()");
        User currentUser=userService.getCurrentLoggedInUser();
        Priority priorityEnum=Priority.valueOf(priority.toUpperCase());
        List<Task> tasks=taskRepository.findByPriorityAndUser(priorityEnum,currentUser,Sort.by(Sort.Direction.DESC,"id"));

        return Response.<List<Task>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Task filtered by Priority for user")
                .data(tasks)
                .build();
    }
}
