package com.buddz.auth.controller;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.GroupManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.buddz.auth.model.BuddzUser;
import com.buddz.auth.repository.BuddzUserRepository;

@RestController
public class SignupController {
	
	@Autowired
	private UserDetailsManager userDetailsManager;
	
	@Autowired
	private GroupManager groupManager;
	
	@Autowired
	private BuddzUserRepository buddzUserRepository;
	
	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	private Logger logger = Logger.getLogger(getClass().getName());
	
//	@InitBinder
//	public void initBinder(WebDataBinder dataBinder) {
//		
//		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
//		
//		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
//	}	
	
//	@GetMapping("/showRegistrationForm")
//	public String showMyLoginPage(Model theModel) {
//		
//		theModel.addAttribute("crmUser", new CrmUser());
//		
//		return "registration-form";
//		
//	}

	@GetMapping(path="/")
	public String getDefault() {
		return "Hello Auth!";
	}
	
	//@GetMapping(path="/login")
	public String getLogin() {
		return "Please login to access the system!";
	}
	
	@PostMapping(path="/signup")
	public SignupResponse SignupResponse(@RequestBody BuddzUser buddzUser) {
						
		String username = buddzUser.getUsername();
		
		logger.info("Processing registration form for: " + username);
		
		// check the database if user already exists
		if (!doesUserExist(username)) {
			// encrypt the password
	        String encodedPassword = passwordEncoder.encode(buddzUser.getPassword());

	        // prepend the encoding algorithm id
	        encodedPassword = "{bcrypt}" + encodedPassword;
	        
	        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_USER");
	        
	        // create user object (from Spring Security framework)
	        User tempUser = new User(username, encodedPassword, authorities);
	        
	        // save user in the database
	        userDetailsManager.createUser(tempUser);
	        groupManager.addUserToGroup(username, "ALL");		
		
			BuddzUser savedUser = buddzUserRepository.save(buddzUser);
	        logger.info("Successfully created user: " + username);
	        return new SignupResponse("success", username, savedUser.getId());
		} else {
			//TODO: HTTP resposne code needs to be fixed.
			return new SignupResponse("failed", username, "User already exists.");
		}
		 
	}
	
	private boolean doesUserExist(String userName) {
		
		logger.info("Checking if user exists: " + userName);
		
		// check the database if the user already exists
		boolean exists = userDetailsManager.userExists(userName);
		
		logger.info("User: " + userName + ", exists: " + exists);
		
		return exists;
	}
}

class SignupResponse {
	
	private String status;
	private String username;
	private long id;
	private String message;
	
	public SignupResponse(String status, String username, long id) {
		super();
		this.status = status;
		this.username = username;
		this.id = id;
	}
	
	public SignupResponse(String status, String username, String message) {
		super();
		this.status = status;
		this.username = username;
		this.message = message;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}

