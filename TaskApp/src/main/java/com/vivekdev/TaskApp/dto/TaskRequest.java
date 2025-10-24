package com.vivekdev.TaskApp.dto;

import com.vivekdev.TaskApp.enums.Priority;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TaskRequest {

    private Long id;

    @NotBlank(message = "Title cannot be empty")
    @Size(max = 200, message = "Title must be less than 200 characters")
    private String title;

    @Size(max = 1000, message = "Title must be less than 1000 characters")
    private String description;

    @NotNull(message = "Completed Status is required")
    private Boolean completed;

    @NotNull(message = "Priority is required")
    private Priority priority;

    @FutureOrPresent(message = "Due Date must be Today or in Future")
    private LocalDate dueDate;
}
