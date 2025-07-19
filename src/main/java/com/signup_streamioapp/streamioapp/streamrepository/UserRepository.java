package com.signup_streamioapp.streamioapp.streamrepository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.signup_streamioapp.streamioapp.streammodels.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmailIgnoreCase(String email);

}
