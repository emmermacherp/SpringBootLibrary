package de.emmermacher.springboot.multipart;

import static org.junit.Assert.*;

import org.junit.Test;

import de.emmermacher.springboot.multipart.MultipartEntityFile;

public class MultipartEntityFileTest {

	private static final String NAME = "574d8386689c381dfc492a3b";
	private static final String FILE_NAME = "574d8386689c381dfc492a3b";
	private static final String CONTENT_TYPE = "image/jpg";
	private static final String CHARSET = "UTF-8";
	private static final String CONTENT_TRANSFER_ENCODING = "binary";
	
	@Test
	public void test() {
		
		String teststring = "Content-Disposition: form-data; name=\"" + NAME + "\"; filename=\"" + FILE_NAME + "\" "
							+ "Content-Type: " + CONTENT_TYPE + "; charset=" + CHARSET + " "
							+ "Content-Transfer-Encoding: " + CONTENT_TRANSFER_ENCODING;
		
		MultipartEntityFile mef = new MultipartEntityFile(teststring, new byte[0]);
		
		assertEquals(mef.getName(), NAME);
		
		assertEquals(mef.getFilename(), FILE_NAME);
		
		assertEquals(mef.getContentType(), CONTENT_TYPE);
		
		assertEquals(mef.getCharset(), CHARSET);
		
		assertEquals(mef.getContentTransferEncoding(), CONTENT_TRANSFER_ENCODING);
	}

}
