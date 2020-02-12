/*
 * Copyright: Philipp Emmermacher 2020
 */

package de.emmermacher.springboot.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

/**
 * @author emmermacherp
 *
 */
public class HashUtils {

  public static byte[] randomSalt(int length) {
    final Random r = new SecureRandom();
    byte[] salt = new byte[length];
    r.nextBytes(salt);
    return salt;
  }

  public static String randomSaltString(int length) {
    byte[] salt = randomSalt(length);
    return encodeBytes(salt);
  }

  public static String encodeBytes(byte[] input) {
    String result = "";
    for (int i = 0; i < input.length; i++) {
      result += Integer.toString((input[i] & 0xff) + 0x100, 16).substring(1);
    }
    return result;
  }

  public static String encodeBytes2(byte[] input) {
    StringBuilder sb = new StringBuilder(input.length * 2);
    for (int i = 0; i < input.length; i++) {
      sb.append(String.format("%02x", new Object[] { Integer.valueOf(input[i] & 255) }));
    }
    return sb.toString();
  }

  public static byte[] getHash(String algorithm, byte[] input) {
    try {
      return MessageDigest.getInstance(algorithm).digest(input);
    } catch (NoSuchAlgorithmException e) {
      // cannot happen
      return null;
    }
  }

  public static String getHashString(String algorithm, byte[] input) {
    byte[] digest = getHash(algorithm, input);
    return encodeBytes(digest);
  }

  public static String getHashString(String algorithm, String input) {
    return encodeBytes(getHash(algorithm, input.getBytes()));
  }

  public static byte[] getSHA256(byte[] input) {
    return getHash("SHA-256", input); 
  }
  
  public static String getSHA256String(byte[] input) {
    byte[] digest = getHash("SHA-256", input);
    return encodeBytes(digest);
  }

  public static String getSHA256String(String input) {
    return encodeBytes(getHash("SHA-256", input.getBytes()));
  }
  
  public static byte[] getMD5(byte[] input) {
    return getHash("MD5", input); 
  }
  
  public static String getMD5String(byte[] input) {
    byte[] digest = getHash("MD5", input);
    return encodeBytes(digest);
  }

  public static String getMD5String(String input) {
    return encodeBytes(getHash("MD5", input.getBytes()));
  }
}
