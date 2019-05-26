package com.prs.web;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.prs.business.JsonResponse;
import com.prs.business.User;
import com.prs.db.UserRepository;


@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	private UserRepository userRepo;
	
	@GetMapping("/")
	public JsonResponse getAll(){
		JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(userRepo.findAll());
	
		}
		catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}
	
	
		
	@GetMapping("/{id}")
	public JsonResponse get(@PathVariable int id) {
		JsonResponse jr = null;
		try {
			Optional<User> u = userRepo.findById(id);
			if(u.isPresent()) {
				jr = JsonResponse.getInstance(u);
			} else {
				jr = JsonResponse.getInstance("No User found for id: " + id);
			}
		}
		catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}
	
	
	@PutMapping("/")
	public JsonResponse update(@RequestBody User u) {
		JsonResponse jr = null;
		//NOTE: may need to enhance exception handling in future for more exceptions
		try {
			if(userRepo.existsById(u.getId())) {
				jr = JsonResponse.getInstance(userRepo.save(u));
		} else {
			jr = JsonResponse.getInstance("User id: " + u.getId() + "does not exist");
		}
		}
		catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}
	
	@PostMapping("/")
	public JsonResponse add(@RequestBody User u) {
		JsonResponse jr = null;
		//NOTE: may need to enhance exception handling in future for more exceptions
		try {
			jr = JsonResponse.getInstance(userRepo.save(u));
		}
		catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}
	
	@DeleteMapping("/")
	public JsonResponse delete(@RequestBody User u) {
		JsonResponse jr = null;
		//NOTE: may need to enhance exception handling in future for more exceptions
		try {
			if(userRepo.existsById(u.getId())) {
				userRepo.delete(u);
				jr = JsonResponse.getInstance("User Deleted.");
		} else {
			jr = JsonResponse.getInstance("User id: " + u.getId() + "does not exist and you are attempting to delete it");
		}
		}
		catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}
	
	//A CUSTOM METHOD
	@PostMapping("/authenticate")
	public JsonResponse login(@RequestParam String userName, String password) {
		JsonResponse jr = null;
		try {
			Optional<User> u = userRepo.findByUserNameAndPassword(userName, password);
			if(u.isPresent()) {
				jr = JsonResponse.getInstance(u);
			} else {
				jr = JsonResponse.getInstance("No user found for userName/password comination" + userName);
			}
		}
		catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}

}
