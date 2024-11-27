package com.example.mysql_mongo.api;
import com.example.mysql_mongo.exception.ResourceNotFoundException;
import com.example.mysql_mongo.model.UserEntity;
import com.example.mysql_mongo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import java.util.List;


@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {
    private final UserService userService;


    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping
    public Object createUser(@RequestBody UserEntity userEntity) {
        try {
            Object savedUser = userService.createUser(userEntity);
            return new ResponseEntity<>(savedUser, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Failed to save user to DB ");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping
    public ResponseEntity<List<Object>> getAllUsers() {
        try {
            List<Object> users = userService.getAllUsers();
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Failed to get all users from DB", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping(path = "/{userId}")
    public ResponseEntity<Object> findUserById(@PathVariable("userId") String userId) {
        try {
            Object user =userService.getUser(userId);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error("Failed to get user from DB ");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }




    @PutMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable String userId, @RequestBody UserEntity updatedUser) {
        try {
            Object user = userService.updateUser(userId, updatedUser);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error("Failed to get user from DB ");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @DeleteMapping("/{userId}")
    public ResponseEntity deleteUser(@PathVariable String userId) {
        try {
            userService.deleteUser(userId);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error("Failed to get user from DB ");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/mergeMongoToMysql")
    public ResponseEntity<List<UserEntity>> mergeMongoToMysql() {
        try {
            List<UserEntity> userEntities =userService.migrationMongoToMySQl();// userService.getUser(userId);
            return new ResponseEntity<>(userEntities, HttpStatus.OK);
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error("Failed to get user from DB ");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
