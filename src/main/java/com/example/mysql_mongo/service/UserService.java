package com.example.mysql_mongo.service;
import com.example.mysql_mongo.exception.ResourceNotFoundException;
import com.example.mysql_mongo.repository.MongoUserRepository;
import com.example.mysql_mongo.repository.MySQLUserRepository;
import com.example.mysql_mongo.repository.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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



    public User createUser(User user) {
        return mySQLUserRepository.save(user);
    }


    public User getUser(Long userId) {
        User user = checkUserExistance(userId);
        return user;
    }

    public List<User> getAllUsers() {
        List<User> users = mySQLUserRepository.findAll();
        if(users==null){
            log.error("The users have not been found");
            throw new ResourceNotFoundException("The users have not been found");
        }
        log.info("Retrieved all users, total count: " + users.size());
        return users;
    }



    public void deleteUser(Long userId) {
        checkUserExistance(userId);
        mySQLUserRepository.deleteById(String.valueOf(userId));
    }


    public User updateUser(Long userId, User updatedUser) {
        checkUserExistance(userId);
        updatedUser.setId(userId);
        return mySQLUserRepository.save(updatedUser);
    }


    private User checkUserExistance(Long userId){
        User user =  mySQLUserRepository.findById(String.valueOf(userId)).orElse(null);
        if(user==null){
            log.error("The user has not been found , userId="+userId);
            throw new ResourceNotFoundException("The user has not been found");
        }
        log.info("Find user by id: " + userId);
        return user;
    }



}
