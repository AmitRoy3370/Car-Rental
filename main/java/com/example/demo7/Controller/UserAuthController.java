package com.example.demo7.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo7.Model.User;
import com.example.demo7.Repository.UserMongoRepository;

@RestController
@RequestMapping("/api/auth/user")
public class UserAuthController {
	
	@Autowired
	UserMongoRepository userRepository;
	
	@PostMapping("/signup")
	public ResponseEntity<?> signUp(@RequestBody User user) {
		
		if(user == null || userRepository.findUserByPassword(user.getPassword()) != null || userRepository.findUserByEmail(user.getEmail()) != null ) {
			
			return ResponseEntity.status(401).body("User is not created");
			
		}
		
		User createdUser = userRepository.save(user);
		
		return ResponseEntity.status(201).body(createdUser);
		
	}
	
	@GetMapping("/searchByName")
	public ResponseEntity<?> findUserByName(@RequestParam String name) {
		
		User searchingUser = userRepository.findUserByName(name);
		
		if(searchingUser == null) {
			
			return ResponseEntity.status(404).body("This name user doesn't exist");
			
		}
		
		return ResponseEntity.status(200).body(searchingUser);
		
	}
	
	@GetMapping("/searchByEmail")
	public ResponseEntity<?> findUserByEmail(@RequestParam String email) {
		
		User searchingUser = userRepository.findUserByEmail(email);
		
		if(searchingUser == null) {
			
			return ResponseEntity.status(404).body("user not found");
			
		}
		
		return ResponseEntity.status(200).body(searchingUser);
		
	}
	
	@PutMapping("/updateUser")
	public ResponseEntity<?> updateEmail(@RequestBody User user, @RequestParam String id) {
		
		if(user == null) {
			
			return ResponseEntity.status(401).body("have to give valid input");
			
		}
		
		if(id == null) {
			
			return ResponseEntity.status(401).body("invalid updated user");
			
		}
		
		if(!userRepository.findById(id).isPresent()) {
			
			return ResponseEntity.status(404).body("your updated id not exist");
			
		}
		
		var existingUser = userRepository.findById(id).get();
		
		user.setId(id);
		
		if(user.getEmail() == null) {
			
			user.setEmail(existingUser.getEmail());
			
		}
		
		if(user.getName() == null) {
			
			user.setName(existingUser.getName());
			
		}
		
		if(user.getRole() == null) {
			
			user.setRole(existingUser.getRole());
			
		}
		
		if(user.getPassword() == null) {
			
			user.setPassword(existingUser.getPassword());
			
		}
		
		userRepository.save(user);
		
		return ResponseEntity.status(200).body(user);
		
	}
	
	@DeleteMapping("/remove")
	public ResponseEntity<?> removeUser(@RequestParam String id) {
		
		long count = userRepository.count();
		
		userRepository.deleteById(id);
		
		return count != userRepository.count() ? ResponseEntity.status(200).body("user deleted successfully") : ResponseEntity.status(501).body("user not deleted");
		
	}
	
	@GetMapping("/login")
	public ResponseEntity<?> logIn(@RequestBody User user) {
		
		if(user == null || userRepository.findById(user.getId()) == null) {
			
			return ResponseEntity.status(401).body("not log in");
			
		}
		
		if(userRepository.existsById(user.getId()) && userRepository.findUserByEmail(user.getEmail()) != null && userRepository.findUserByName(user.getName()) != null && userRepository.findUserByPassword(user.getPassword()) != null) {
			
			return ResponseEntity.status(200).body("log in successfull");
			
		}
		
		return ResponseEntity.status(404).body("not log in");
		
	}
	
	@GetMapping("/showAll")
	public ResponseEntity<?> showAll() {
		
		return ResponseEntity.status(200).body(userRepository.findAll());
		
	}

}
