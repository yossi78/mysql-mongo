package com.example.mysql_mongo.service;
import com.example.mysql_mongo.exception.ResourceNotFoundException;
import com.example.mysql_mongo.repository.MongoUserRepository;
import com.example.mysql_mongo.repository.MySQLUserRepository;
import com.example.mysql_mongo.model.UserEntity;
import com.example.mysql_mongo.model.UserDocument;
import com.example.mysql_mongo.util.ConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;


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



    public Object createUser(UserEntity userEntity) {
        if (isBusinessHours()) {
            return mySQLUserRepository.save(userEntity);
        } else {
            UserDocument userDocument = UserDocument.builder()
                    .lastName(userEntity.getLastName())
                    .firstName(userEntity.getFirstName())
                    .build();
            return mongoUserRepository.save(userDocument);
        }
    }


    public Object getUser(String userId) {
        Object user = checkUserExistance(userId);
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



    public List<UserEntity> migrationMongoToMySQl(){
        List<UserDocument> users;
        users =  mongoUserRepository.findAll();

        List<UserEntity> mysqlList = users.stream()
                .map(a -> {
                    try {
                        return new UserEntity(null,a.getFirstName(),a.getLastName());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }) // Mapping logic
                .collect(Collectors.toList());
        return mySQLUserRepository.saveAll(mysqlList);

    }

    public void deleteUser(String userId) {
        if(isBusinessHours() && isEntityExist(userId)){
            mySQLUserRepository.deleteById(userId);
        }else if(!isBusinessHours() && isDocumentExist(userId)){
            mongoUserRepository.deleteById(userId);
        }else {
            throw new ResourceNotFoundException("User has not been found");
        }

    }


    public Object updateUser(String userId, UserEntity updatedUser) throws Exception {
        if (isBusinessHours()) {
            updatedUser.setId(userId);
            if (isEntityExist(userId)) {
                return mySQLUserRepository.save(updatedUser);
            }else{
                throw new ResourceNotFoundException("User has not been found");
            }
        }
        UserDocument userDocument = ConvertUtil.convertObject(updatedUser, UserDocument.class);
        userDocument.setId(userId);
        return updateUserDocument(userDocument);
    }

    private UserDocument updateUserDocument(UserDocument userDocument){
        if (isDocumentExist(userDocument.getId())) {
            return mongoUserRepository.save(userDocument);
        }  else{
            throw new ResourceNotFoundException("User has not been found");
        }
    }

    private Object checkUserExistance(String userId){
        Object user;
        if (isBusinessHours()) {
           user = mySQLUserRepository.findById(userId).orElse(null);
        } else {
            user = mongoUserRepository.findById(userId).orElse(null);
        }
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




    private Boolean isEntityExist(String userId){
        if(mySQLUserRepository.findById(userId).orElse(null)!=null){
            return true;
        }
        return false;
    }


    private Boolean isDocumentExist(String userId){
        if(mongoUserRepository.findById(userId).orElse(null)!=null){
            return true;
        }
        return false;
    }




}
