package com.example.mysql_mongo.service;
import com.example.mysql_mongo.model.User;
import com.example.mysql_mongo.repository.MongoUserRepository;
import com.example.mysql_mongo.repository.MySQLUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalTime;
import java.util.List;


@Service
public class UserService {

    private MySQLUserRepository mySQLUserRepository;

    private MongoUserRepository mongoUserRepository;

    @Autowired
    public UserService(MySQLUserRepository mySQLUserRepository, MongoUserRepository mongoUserRepository) {
        this.mySQLUserRepository = mySQLUserRepository;
        this.mongoUserRepository = mongoUserRepository;
    }

    public User saveUser(User user) {
        if (isBusinessHours()) {
            return mySQLUserRepository.save(user);
        } else {
            return mongoUserRepository.save(user);
        }
    }

    public List<User> getUsers() {
        if (isBusinessHours()) {
            return mySQLUserRepository.findAll();
        } else {
            return mongoUserRepository.findAll();
        }
    }

    private boolean isBusinessHours() {
        LocalTime now = LocalTime.now();
        return now.isAfter(LocalTime.of(8, 0)) && now.isBefore(LocalTime.of(17, 0));
    }
}
