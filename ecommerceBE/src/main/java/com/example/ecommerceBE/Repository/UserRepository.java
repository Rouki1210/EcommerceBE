package com.example.ecommerceBE.Repository;

import com.example.ecommerceBE.entity.User;
import com.example.ecommerceBE.entity.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<User> findByVerifyToken(String token);
    Page<User> findAll(Pageable pageable);
    Optional<User> findByResetToken(String token);
    List<User> findByRole(Role role);
}
