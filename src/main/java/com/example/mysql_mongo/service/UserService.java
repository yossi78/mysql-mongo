package com.example.mysql_mongo.service;
import com.example.mysql_mongo.exception.ResourceNotFoundException;
import com.example.mysql_mongo.repository.MongoUserRepository;
import com.example.mysql_mongo.repository.MySQLUserRepository;
import com.example.mysql_mongo.repository.User;
import com.example.mysql_mongo.repository.UserDocument;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;


@Service
@Slf4j
public class UserService {


    private final MySQLUserRepository mySQLUserRepository;
    private MongoUserRepository mongoUserRepository;

    @Autowired
    public UserService(MySQLUserRepository mySQLUserRepository, MongoUserRepository mongoUserRepository) {
        this.mySQLUserRepository = mySQLUserRepository;
        this.mongoUserRepository = mongoUserRepository;
    }



    public Object createUser(User user) {
        if (isBusinessHours()) {
            return mySQLUserRepository.save(user);
        } else {
            UserDocument userDocument = UserDocument.builder()
                    .lastName(user.getLastName())
                    .firstName(user.getFirstName())
                    .build();
            return mongoUserRepository.save(userDocument);
        }
    }


    public User getUser(Long userId) {
        User user = checkUserExistance(userId);
        return user;
    }

    public List<Object> getAllUsers() {
        List<Object> users;
        if (isBusinessHours()) {
            users = (List<Object>) (List<?>) mySQLUserRepository.findAll();
        } else {
            users = (List<Object>) (List<?>) mongoUserRepository.findAll();
        }
        if(users==null){
            log.error("The users have not been found");
            throw new ResourceNotFoundException("The users have not been found");
        }
        log.info("Retrieved all users, total count: " + users.size());
        return users;
    }



    public void deleteUser(Long userId) {
        checkUserExistance(userId);
        mySQLUserRepository.deleteById(userId);
    }


    public User updateUser(Long userId, User updatedUser) {
        checkUserExistance(userId);
        updatedUser.setId(userId);
        return mySQLUserRepository.save(updatedUser);
    }


    private User checkUserExistance(Long userId){
        User user =  mySQLUserRepository.findById(userId).orElse(null);
        if(user==null){
            log.error("The user has not been found , userId="+userId);
            throw new ResourceNotFoundException("The user has not been found");
        }
        log.info("Find user by id: " + userId);
        return user;
    }


    private boolean isBusinessHours() {
        LocalTime now = LocalTime.now();
       return now.isAfter(LocalTime.of(8, 0)) && now.isBefore(LocalTime.of(10, 0));                         // MONGODB
 //       return now.isAfter(LocalTime.of(8, 0)) && now.isBefore(LocalTime.of(17, 0));  // MYSQL
    }


}
