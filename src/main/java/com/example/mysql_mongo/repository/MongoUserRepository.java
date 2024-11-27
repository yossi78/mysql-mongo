package com.example.mysql_mongo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.example.mysql_mongo.model.UserDocument;

@Repository
public interface MongoUserRepository extends MongoRepository<UserDocument,String> {
}
