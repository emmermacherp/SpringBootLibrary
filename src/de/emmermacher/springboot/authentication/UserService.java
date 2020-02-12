/*
 * Copyright: Philipp Emmermacher 2020
 */

package de.emmermacher.springboot.authentication;

import org.springframework.stereotype.Service;

@Service
public interface UserService {
	
	boolean userExsist(String username);
	boolean matchPassword(String username, String password);
	String getPassword(String username);
	void login(String username);
	String getRealm();
	
}