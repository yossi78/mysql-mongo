package com.example.mysql_mongo.repository;
import com.example.mysql_mongo.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoUserRepository extends MongoRepository<User, String> {
}
