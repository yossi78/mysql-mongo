package com.example.mysql_mongo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;



@SpringBootApplication
@EnableJpaRepositories//(basePackages = "com.example.mysql_mongo.repository.MySQLUserRepository")
@EnableMongoRepositories//(basePackages = "com.example.mysql_mongo.repository.MongoUserRepository")
@ComponentScan(basePackages={"com.example.mysql_mongo.***"})


public class MysqlMongoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MysqlMongoApplication.class, args);
	}

}
