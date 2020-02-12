package de.emmermacher.springboot.multipart;

import static org.junit.Assert.*;

import org.junit.Test;

import de.emmermacher.springboot.util.HashUtils;

public class HashTest {

  @Test
  public void testMD5String() {
    String md5 = HashUtils.getMD5String("Test");
    assertEquals("0cbc6611f5540bd0809a388dc95a615b".toLowerCase(), md5.toLowerCase());
  }
  
  @Test
  public void testSHA256String() {
    String sha256 = HashUtils.getSHA256String("Test");
    assertEquals("532EAABD9574880DBF76B9B8CC00832C20A6EC113D682299550D7A6E0F345E25".toLowerCase(), sha256.toLowerCase());
  }
  
  @Test
  public void testSHA256withSaltString() {
    String salt = HashUtils.randomSaltString(32); 
    String sha256_1 = HashUtils.getSHA256String("Test" + salt);    
    String sha256_2 = HashUtils.getSHA256String("Test" + salt);
    assertEquals(sha256_1, sha256_2);    
  }

}
