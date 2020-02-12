/*
 * Copyright: Philipp Emmermacher 2020
 */

package de.emmermacher.springboot.authentication.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.emmermacher.springboot.authentication.AuthenticationService;
import de.emmermacher.springboot.authentication.UserService;

/**
 * @author AMD
 *
 */
@Service
public final class AuthenticationServiceImpl implements AuthenticationService {

	private static final Logger LOG = Logger.getLogger(AuthenticationServiceImpl.class.getName());

	@Autowired
	private UserService userService;

	private final Response AUTH_FAILED = Response.status(Status.UNAUTHORIZED)
			.header("WWW-Authenticate", "Authorization failed").build();

	private final ScheduledExecutorService nonceRefreshExecutor;
	private String nonce;

	public AuthenticationServiceImpl() {
		nonce = calculateNonce();
		nonceRefreshExecutor = Executors.newScheduledThreadPool(1);
		nonceRefreshExecutor.scheduleAtFixedRate(new Runnable() {
			public void run() {
				nonce = calculateNonce();
			}
		}, 30, 30, TimeUnit.MINUTES);
	}

	public void close() {
		nonceRefreshExecutor.shutdownNow();
	}

	/**
	 * Calculate the nonce based on current time-stamp upto the second, and a
	 * random seed
	 *
	 * @return
	 */
	private String calculateNonce() {
		Date d = new Date();
		SimpleDateFormat f = new SimpleDateFormat("yyyy:MM:dd:hh:mm:ss");
		String fmtDate = f.format(d);
		Random rand = new Random(100000);
		Integer randomInt = rand.nextInt();
		return DigestUtils.md5Hex(fmtDate + randomInt.toString());
	}

	private String getOpaque(String domain, String nonce) {
		return DigestUtils.md5Hex(domain + nonce);
	}

	private String getAuthenticateHeaderDigest() {
		String header = "";
		header += "Digest realm=\"" + userService.getRealm() + "\",";
		header += "qop=auth,";
		header += "nonce=\"" + nonce + "\",";
		header += "opaque=\"" + getOpaque(userService.getRealm(), nonce) + "\"";
		return header;
	}

	@Deprecated
	@SuppressWarnings("unused")
	private String getAuthenticateHeaderBasic() {
		return "Basic realm=\"" + userService.getRealm() + "\"";
	}

	/**
	 * Gets the Authorization header string minus the "AuthType" and returns a
	 * hashMap of keys and values
	 *
	 * @param headerString
	 * @return
	 */
	private HashMap<String, String> parseHeader(String headerString) {
		// seperte out the part of the string which tells you which Auth scheme is it
		String headerStringWithoutScheme = headerString.substring(headerString.indexOf(" ") + 1).trim();
		HashMap<String, String> values = new HashMap<String, String>();
		String keyValueArray[] = headerStringWithoutScheme.split(",");
		for (String keyval : keyValueArray) {
			if (keyval.contains("=")) {
				String key = keyval.substring(0, keyval.indexOf("="));
				String value = keyval.substring(keyval.indexOf("=") + 1);
				values.put(key.trim(), value.replaceAll("\"", "").trim());
			}
		}
		return values;
	}

	public Response authenticate(String authorization, String method) {
		try {
			if (authorization == null || authorization.trim().isEmpty() || userService == null) {
				return Response.status(Status.UNAUTHORIZED).header("WWW-Authenticate", getAuthenticateHeaderDigest())
						.build();
			} else {
				if (authorization.startsWith("Digest")) {
					HashMap<String, String> headerValues = parseHeader(authorization);

					String qop = headerValues.get("qop");
					// String opaque = headerValues.get("opaque");
					String nc = headerValues.get("nc");
					String clientResponse = headerValues.get("response");
					String realm = headerValues.get("realm");
					// String clientNonce = headerValues.get("nonce");
					String uri = headerValues.get("uri");
					String cnonce = headerValues.get("cnonce");
					String username = headerValues.get("username");
					String algorithm = headerValues.get("algorithm");

					String HA1 = "";
					String HA2 = "";
					String serverResponse = "";

					if (!userService.userExsist(username)) {
						return AUTH_FAILED;
					}

					String password = userService.getPassword(username);

					if (algorithm == null || algorithm.equals("MD5")) {
						HA1 = DigestUtils.md5Hex(username + ":" + realm + ":" + password);
					}
					if (qop != null && qop.equals("auth")) {
						HA2 = DigestUtils.md5Hex(method + ":" + uri);
					}
					if (qop != null && qop.equals("auth")) {
						serverResponse = DigestUtils.md5Hex(HA1 + ":" + nonce + ":" + nc + ":" + cnonce + ":" + qop + ":" + HA2);
					}

					if (!serverResponse.equals(clientResponse)) {
						return Response.status(Status.UNAUTHORIZED).header("WWW-Authenticate", getAuthenticateHeaderDigest()).build();
					} else {
						userService.login(username);
						return null;
					}
				} else if (authorization.startsWith("Basic ")) {
					String base64 = authorization.substring(authorization.indexOf("Basic ") + "Basic ".length());
					if (!Base64.isBase64(base64)) {
						return AUTH_FAILED;
					}

					String[] credentials = new String(Base64.decodeBase64(base64)).split(":");
					if (credentials.length != 2) {
						return AUTH_FAILED;
					}

					if (!userService.userExsist(credentials[0])|| !userService.getPassword(credentials[0]).equals(credentials[1])) {
						return AUTH_FAILED;
					} else {
						userService.login(credentials[0]);
						return null;
					}
				} else {
					return AUTH_FAILED;
				}
			}
		} catch (Exception e) {
			LOG.error("authenticate()", e);
			return AUTH_FAILED;
		}
	}
}
