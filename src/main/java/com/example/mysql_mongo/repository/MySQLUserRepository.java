package com.example.mysql_mongo.repository;
import com.example.mysql_mongo.repository.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MySQLUserRepository extends JpaRepository<User, String> {


}
