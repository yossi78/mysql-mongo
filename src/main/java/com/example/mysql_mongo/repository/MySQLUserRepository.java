package com.example.mysql_mongo.repository;
import com.example.mysql_mongo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MySQLUserRepository extends JpaRepository<User, Long> {
}
