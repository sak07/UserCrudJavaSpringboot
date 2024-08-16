package com.user.crud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.user.crud.entity.User;
import com.user.crud.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping
	public Object getAllUsers() {
		return userService.getAllUsers();
	}

	@GetMapping("/{id}")
	public ResponseEntity<String> getUser(@PathVariable String id) {
		Long parsedId;
		try {
			parsedId = Long.parseLong(id);
		} catch (NumberFormatException e) {
			return ResponseEntity.badRequest().body("Invalid ID format. Please enter a numeric ID.");
		}
		String result = userService.getUserById(parsedId);
		return ResponseEntity.ok(result);
	}

	@PostMapping
	public String createUser(@RequestBody User user) {
		return userService.registerUser(user.getUsername(), user.getPassword(), user.isActive());
	}

	@PutMapping("/{id}")
	public String updateUser(@PathVariable Long id, @RequestBody User user) {
		user.setId(id);
		return userService.updateUser(id, user.getUsername(), user.getPassword(), user.isActive());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteUser(@PathVariable String id) {
		try {
			Long userId = Long.parseLong(id);
			return ResponseEntity.ok(userService.deleteUser(userId));
		} catch (NumberFormatException e) {
			return ResponseEntity.badRequest().body("Invalid ID format. ID must be a number.");
		}
	}
}
