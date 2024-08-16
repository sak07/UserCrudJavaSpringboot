package com.user.crud.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.user.crud.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByUsername(String username);

	boolean existsByUsername(String username);
}
