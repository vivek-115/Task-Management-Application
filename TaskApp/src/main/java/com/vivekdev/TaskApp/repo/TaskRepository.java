package com.vivekdev.TaskApp.repo;

import com.vivekdev.TaskApp.entity.Task;
import com.vivekdev.TaskApp.entity.User;
import com.vivekdev.TaskApp.enums.Priority;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByUser(User user, Sort sort);
    List<Task> findByCompletedAndUser(boolean completed, User user);
    List<Task> findByPriorityAndUser(Priority priority, User user, Sort sort);
}
