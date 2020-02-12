/*
 * Copyright: Philipp Emmermacher 2020
 */

package de.emmermacher.springboot.util;

import java.nio.ByteBuffer;

/**
 * Utility class for data conversion.
 * 
 * @author emmermacherp
 */
public final class DataUtils {

  /**
   * Conversion of a byte array to a double array. Convert each byte into a
   * double.
   *
   * @param byteArray
   *          the byte array to convert
   * @return the resulting double array
   */
  public static double[] convertToDoubleArray(byte[] byteArray) {
    double[] doubleArray = null;

    if (byteArray.length > 0) {
      doubleArray = new double[byteArray.length / 8];
      ByteBuffer byteBuffer = ByteBuffer.wrap(byteArray);
      for (int i = 0; i < doubleArray.length; i++)
        doubleArray[i] = byteBuffer.getDouble(i * 8);
    }

    return doubleArray;
  }

  /**
   * Conversion of an double array to a byte array.
   *
   * @param doubleArray
   *          the double array to convert
   * @return the resulting byte array
   */
  public static byte[] convertToByteArray(double[] doubleArray) {
    byte[] byteArray = new byte[doubleArray.length * 8];
    ByteBuffer byteBuffer = ByteBuffer.wrap(byteArray);

    for (int i = 0; i < doubleArray.length; i++) {
      byteBuffer.putDouble(i * 8, doubleArray[i]);
    }

    return byteBuffer.array();
  }

  /**
   * Conversion of a byte array to a float array. Convert each byte into a
   * float.
   *
   * @param byteArray
   *          the byte array to convert
   * @return the resulting float array
   */
  public static float[] convertToFloatArray(byte[] byteArray) {
    float[] floatArray = null;

    if (byteArray.length > 0) {
      floatArray = new float[byteArray.length / 4];
      ByteBuffer byteBuffer = ByteBuffer.wrap(byteArray);
      for (int i = 0; i < floatArray.length; i++)
        floatArray[i] = byteBuffer.getFloat(i * 4);
    }

    return floatArray;
  }

  /**
   * Conversion of an float array to a byte array.
   *
   * @param floatArray
   *          the float array to convert
   * @return the resulting byte array
   */
  public static byte[] convertToByteArray(float[] floatArray) {
    byte[] byteArray = new byte[floatArray.length * 4];
    ByteBuffer byteBuffer = ByteBuffer.wrap(byteArray);

    for (int i = 0; i < floatArray.length; i++) {
      byteBuffer.putFloat(i * 4, floatArray[i]);
    }

    return byteBuffer.array();
  }
}

