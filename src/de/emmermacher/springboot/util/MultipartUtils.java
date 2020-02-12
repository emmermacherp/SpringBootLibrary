/*
 * Copyright: Philipp Emmermacher 2020
 */

package de.emmermacher.springboot.util;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.MultipartStream;

import de.emmermacher.springboot.multipart.MultipartEntityFile;

public class MultipartUtils {

  private static final Logger LOG = Logger.getLogger(MultipartUtils.class);

  public static List<MultipartEntityFile> getMultipartData(HttpResponse response) {
    byte[] boundary = null;

    for (Header header : response.getAllHeaders()) {
      if (header.getName().equalsIgnoreCase("boundary")) {
        boundary = header.getValue().getBytes();
      }
    }

    try {
      List<MultipartEntityFile> mefs = new ArrayList<MultipartEntityFile>();

      MultipartStream ms = new MultipartStream(response.getEntity().getContent(), boundary, 4096, null);
      boolean nextPart = ms.skipPreamble();
      while (nextPart) {
        String headers = ms.readHeaders();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ms.readBodyData(os);
        MultipartEntityFile mef = new MultipartEntityFile(headers, os.toByteArray());
        mefs.add(mef);
        nextPart = ms.readBoundary();
      }

      return mefs;
    } catch (Exception e) {
      LOG.error("getMutipartData()", e);
      return new ArrayList<MultipartEntityFile>();
    }
  }
}
