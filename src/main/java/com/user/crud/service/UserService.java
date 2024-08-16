package com.user.crud.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.user.crud.dto.UserDTO;
import com.user.crud.entity.User;
import com.user.crud.exception.UserNotFoundException;
import com.user.crud.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public Object getAllUsers() {
		List<User> users = userRepository.findAll();
		if (users.isEmpty()) {
			return "No users found.";
		}
		// Display without passwords
		List<UserDTO> userDTOs = users.stream()
				.map(user -> new UserDTO(user.getId(), user.getUsername(), user.isActive()))
				.collect(Collectors.toList());

		StringBuilder response = new StringBuilder("This is the user list:\n\n");

		for (UserDTO userDTO : userDTOs) {
			response.append("ID: ").append(userDTO.getId()).append(", Username: ").append(userDTO.getUsername())
					.append(", Active: ").append(userDTO.getActive()).append("\n");
		}
		return response.toString();
	}

	public String getUserById(Long id) {
		Optional<User> optionalUser = userRepository.findById(id);
		if (optionalUser.isEmpty()) {
			return "User not found with ID: " + id;
		}
		User user = optionalUser.get();
		return String.format("User found - ID: %d, Username: %s, Active: %b", user.getId(), user.getUsername(),
				user.isActive());
	}

	public User saveUser(User user) {
		return userRepository.save(user);
	}

	public String registerUser(String username, String plainPassword, boolean active) {
		if (userRepository.existsByUsername(username)) {
			return "Username is already taken.";
		}
		String encodedPassword = passwordEncoder.encode(plainPassword);
		User user = new User();
		user.setUsername(username);
		user.setPassword(encodedPassword);
		user.setActive(active);
		User savedUser = userRepository.save(user);
		return "User successfully registered with ID: " + savedUser.getId();
	}

	public String updateUser(Long userId, String newUsername, String newPassword, Boolean isActive) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

		StringBuilder updateMessage = new StringBuilder("User with ID: " + userId + " has been updated. Changes: ");
		boolean updated = false;

		if (newUsername != null && !newUsername.equals(user.getUsername())) {
			if (userRepository.existsByUsername(newUsername)) {
				return "Username '" + newUsername + "' is already taken.";
			}
			user.setUsername(newUsername);
			updateMessage.append("Username changed to: ").append(newUsername).append(". ");
			updated = true;
		}

		if (isActive != null && !isActive.equals(user.isActive())) {
			user.setActive(isActive);
			updateMessage.append("Active status changed to: ").append(isActive).append(". ");
			updated = true;
		}

		if (newPassword != null && !newPassword.isEmpty()) {
			if (!passwordEncoder.matches(newPassword, user.getPassword())) {
				String encodedPassword = passwordEncoder.encode(newPassword);
				user.setPassword(encodedPassword);
				updateMessage.append("Password updated. ");
				updated = true;
			}
		}
		if (!updated) {
			return "No changes were made to the user with ID: " + userId;
		}
		userRepository.save(user);
		return updateMessage.toString();
	}

	public String deleteUser(Long id) {
		if (!userRepository.existsById(id)) {
			return "User not found with ID: " + id;
		}
		userRepository.deleteById(id);
		return "User successfully deleted with ID: " + id;
	}

	public boolean authenticateUser(String username, String plainPassword) {
		User user = userRepository.findByUsername(username);
		if (user != null) {
			return passwordEncoder.matches(plainPassword, user.getPassword());
		}
		return false;
	}
}
