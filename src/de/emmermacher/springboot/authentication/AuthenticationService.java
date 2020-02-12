/*
 * Copyright: Philipp Emmermacher 2020
 */

package de.emmermacher.springboot.authentication;

import javax.ws.rs.core.Response;

public interface AuthenticationService {

	Response authenticate(String authorization, String method);
	  
}
