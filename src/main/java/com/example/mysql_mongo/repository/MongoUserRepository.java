package com.example.mysql_mongo.repository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;




@Repository
public interface MongoUserRepository extends MongoRepository<User, String> {
}
