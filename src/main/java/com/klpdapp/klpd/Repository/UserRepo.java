package com.klpdapp.klpd.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.klpdapp.klpd.model.User;

public interface UserRepo extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

}
