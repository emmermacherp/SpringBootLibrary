/*
 * Copyright: Philipp Emmermacher 2020
 */

package de.emmermacher.springboot.util;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.util.IOUtils;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;
import org.imgscalr.Scalr.Mode;

/**
 * Utility class for image transformation
 * 
 * @author emmermacherp
 */
public final class ImageUtils {

  /**
   * Conversion a byte array containing image data to a byte array containing
   * the JPG data.
   * 
   * @param src
   *          the byte array to convert
   * @return the resulting byte array
   * @throws IOException
   *           if an error occurs during reading.
   * @throws ImageReadException
   */
  public static final byte[] convertImageToJPEG(byte[] src) throws IOException, ImageReadException {
    return getByteArray(getBufferedImage(src));
  }

  /**
   * Conversion of a byte array to a BufferedImage.
   * 
   * @param src
   *          the byte array to convert
   * @return the resulting BufferedImage
   * @throws IOException
   *           if an error occurs during reading.
   * @throws ImageReadException
   */
  public static final BufferedImage getBufferedImage(byte[] src) throws IOException, ImageReadException {
    return new JPGReader().readImage(src);
  }

  public static byte[] readAndResizeImageFromURL(URL url, int maxPixels) throws Exception {
    ByteArrayOutputStream bis = new ByteArrayOutputStream();
    InputStream is = url.openStream();
    byte[] bytebuff = new byte[4096];
    int n;

    while ((n = is.read(bytebuff)) > 0)
      bis.write(bytebuff, 0, n);

    BufferedImage image = ImageUtils.getBufferedImage(bis.toByteArray());
    image = ImageUtils.createThumbnail(image, maxPixels);
    return ImageUtils.getByteArray(image);
  }

  public static byte[] readAndResizeImage(String path, int maxPixels) throws Exception {
    byte[] imageData = FileUtils.readFileToByteArray(new File(path));
    BufferedImage image = ImageUtils.getBufferedImage(imageData);
    image = ImageUtils.createThumbnail(image, maxPixels);
    return ImageUtils.getByteArray(image);
  }

  public static final BufferedImage readCMYKToRGBBufferedImage(File file) throws ImageReadException, IOException {
    return new JPGReader().readImage(IOUtils.getFileBytes(file));
  }

  /**
   * Conversion of a BufferedImage to a byte array.
   * <br>
   * The byte array will contain the JPG data of the BufferedImage.
   * 
   * @param src
   *          the BufferedImage instance to convert
   * @return the resulting byte array
   * @throws IOException
   *           if an error occurs during writing.
   */
  public static final byte[] getByteArray(BufferedImage src) throws IOException {
    BufferedImage newImage = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_RGB);
    newImage.createGraphics().drawImage(src, 0, 0, Color.BLACK, null);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ImageIO.write(newImage, "jpg", baos);
    baos.flush();
    byte[] bytes = baos.toByteArray();
    baos.close();
    return bytes;
  }

  public static byte[] getPlanarRGBImage(BufferedImage src) {

    int[] rgb = src.getRGB(0, 0, src.getWidth(), src.getHeight(), null, 0, src.getWidth());
    ByteBuffer byteBuffer = ByteBuffer.allocate(rgb.length * 4);
    IntBuffer intBuffer = byteBuffer.asIntBuffer();
    intBuffer.put(rgb);

    byte[] bytes = int2byte(rgb);
    byte[] bytesOut = new byte[rgb.length * 3];

    int offset = 0;
    for (int i = 0; i < rgb.length * 3; i += 3) {
      bytesOut[i] = bytes[i + 2 + offset];
      bytesOut[i + 1] = bytes[i + 1 + offset];
      bytesOut[i + 2] = bytes[i + offset];
      offset++;
    }

    return bytesOut;
  }

  private static byte[] int2byte(int[] src) {
    int srcLength = src.length;
    byte[] dst = new byte[srcLength << 2];

    for (int i = 0; i < srcLength; i++) {
      int x = src[i];
      int j = i << 2;
      dst[j++] = (byte) ((x >>> 0) & 0xff);
      dst[j++] = (byte) ((x >>> 8) & 0xff);
      dst[j++] = (byte) ((x >>> 16) & 0xff);
      dst[j++] = (byte) ((x >>> 24) & 0xff);
    }
    return dst;
  }

  public static BufferedImage getGrayImage(BufferedImage src) throws Exception {
    return Scalr.apply(src, Scalr.OP_GRAYSCALE);
  }

  /**
   * 
   * @param src
   * @param size
   * @return
   * @throws IOException
   */
  public static final BufferedImage createThumbnail(BufferedImage src, int size) throws Exception {
    return Scalr.resize(src, Method.QUALITY, Mode.AUTOMATIC, size, size, Scalr.OP_ANTIALIAS);
  }
}
