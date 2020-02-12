package de.emmermacher.springboot.util;

import java.io.File;
import java.net.URL;

import org.apache.commons.io.FileUtils;

import de.emmermacher.springboot.util.ImageUtils;

public class ImageUtilsTest {

  public static void main(String[] args) throws Exception {
    testReadAndResizeImageWithFTPLink();
  }
  
  private static void testReadAndResizeImageWithFTPLink() throws Exception{
    File tmpFile = new File("image-utils-test.png");
    byte[] data = ImageUtils.readAndResizeImageFromURL(new URL("ftp://hive:gfnddLUXnp4j@image-upload.ryde.one/1/1/14/1-1-729639_wenn3153981.jpg"), 400);
    FileUtils.writeByteArrayToFile(tmpFile, data);
  }

}
