package com.example.auth.repository;

import com.example.auth.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("exampleUserRepository")
public interface UserRepository extends MongoRepository<User, String> {
    
    @Query("{ 'username' : ?0 }")
    Optional<User> findByUsername(String username);
}
