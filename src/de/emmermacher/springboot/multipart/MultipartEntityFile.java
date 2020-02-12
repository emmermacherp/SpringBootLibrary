/*
 * Copyright: Philipp Emmermacher 2020
 */

package de.emmermacher.springboot.multipart;

public class MultipartEntityFile {

	private static final String CONTENT_DISPOSITION_KEY = "Content-Disposition:";
	private static final String CONTENT_TYPE = "Content-Type:";
	private static final String CONTENT_TRANSFER_ENCODING = "Content-Transfer-Encoding:";
	
  private static final String NAME_KEY     = "name=\"";
  private static final String FILENAME_KEY = "filename=\"";
  private static final String CHARSET_KEY  = "charset=";

  private String              name;
  private String              filename;
  private String              contentType;
  private String              charset;
  private final String        contentTransferEncoding;
  private final byte[]        content;

  public MultipartEntityFile(String headersString, byte[] content) {
    this.content = content;

    int dispoIndex = headersString.indexOf(CONTENT_DISPOSITION_KEY) + CONTENT_DISPOSITION_KEY.length();
    int typeIndex = headersString.indexOf(CONTENT_TYPE) + CONTENT_TYPE.length();
    int transferEncodingIndex = headersString.indexOf(CONTENT_TRANSFER_ENCODING) + CONTENT_TRANSFER_ENCODING.length();

    String dispo = headersString.substring(dispoIndex, headersString.indexOf(CONTENT_TYPE) - 1);
    String[] fields = dispo.split(";");
    for (String str : fields) {
      str = str.trim();
    	str = str.trim();
      if (str.contains("filename=")) {
        this.filename = str.substring(str.indexOf(FILENAME_KEY) + FILENAME_KEY.length(), str.length() - 1).trim();
      }else if (str.contains("name=")) {
          this.name = str.substring(str.indexOf(NAME_KEY) + NAME_KEY.length(), str.length() - 1).trim();
        }
    }

    String contentType = headersString.substring(typeIndex, headersString.indexOf(CONTENT_TRANSFER_ENCODING) - 1);
    fields = contentType.split(";");
    for (String str : fields) {
      if (str.contains("charset=")) {
        this.charset = str.substring(str.indexOf(CHARSET_KEY) + CHARSET_KEY.length()).trim();
      } else {
        this.contentType = str.trim();
      }
    }

    this.contentTransferEncoding = headersString.substring(transferEncodingIndex).trim();
  }

  public String getName() {
    return name;
  }

  public String getFilename() {
    return filename;
  }

  public String getContentType() {
    return contentType;
  }

  public String getCharset() {
    return charset;
  }

  public String getContentTransferEncoding() {
    return contentTransferEncoding;
  }

  public byte[] getContent() {
    return content;
  }
}