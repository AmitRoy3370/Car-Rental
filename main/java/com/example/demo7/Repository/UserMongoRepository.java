package com.example.demo7.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo7.Model.User;

public interface UserMongoRepository extends MongoRepository<User, String> {
	
	public User findUserByEmail(String email);
	public User findUserByName(String name);
	public User findUserByPassword(String password);

}
