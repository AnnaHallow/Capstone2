package com.techelevator.tenmo.tenmo.services;

import com.techelevator.tenmo.tenmo.model.Account;
import com.techelevator.tenmo.tenmo.model.User;
import com.techelevator.tenmo.tenmo.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

	UserRepository userRepository;

	public List<User> getUsers() {
		return userRepository.findAll();
	}
}
