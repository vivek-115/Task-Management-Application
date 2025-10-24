package com.vivekdev.TaskApp.repo;

import com.vivekdev.TaskApp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepositry extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
}
