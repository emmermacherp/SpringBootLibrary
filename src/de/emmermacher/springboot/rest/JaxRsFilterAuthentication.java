/*
 * Copyright: Philipp Emmermacher 2020
 */

package de.emmermacher.springboot.rest;

import java.io.IOException;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.emmermacher.springboot.authentication.AuthenticationService;

@Provider
@Component
public class JaxRsFilterAuthentication implements ContainerRequestFilter {

	@Autowired
	private AuthenticationService authenticationService;	
	
	public void filter(ContainerRequestContext requestContext) throws IOException {
		Response authResponse = authenticationService.authenticate(requestContext.getHeaderString("Authorization"), requestContext.getMethod());
		if(authResponse != null){
			throw new WebApplicationException(authResponse);
		}
	}
}
