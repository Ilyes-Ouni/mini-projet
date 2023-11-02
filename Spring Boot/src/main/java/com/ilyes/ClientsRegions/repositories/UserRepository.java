package com.ilyes.ClientsRegions.repositories;

import com.ilyes.ClientsRegions.entities.Role;
import com.ilyes.ClientsRegions.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {


  @Query("select u from User as u where u.email = :email")
  Optional<User> findByEmail(String email);

  Optional<Role> findByRole(String role);

  List<User> findAll();

  boolean existsByEmail(String email);

  User findTopByOrderByIdDesc();

}
