package com.loan.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import com.loan.entity.User;
import com.loan.repository.UserRepository;

@Service
public class CustomUserDetailService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	
	@GetMapping("/test")
	public String test() {
	    System.out.println("🔥 TEST endpoint HIT");
	    return "OK";
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		 User user = userRepository
	                .findByEmailOrMobileNumber(username, username)
	                .orElseThrow(() ->
	                        new UsernameNotFoundException(
	                                "User not found with email/mobile: " + username));

	        return new CustomUserDetail(user);
	  
	}

	
}
