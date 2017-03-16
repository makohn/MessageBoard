package de.htwsaar.wirth.remote.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtil {
	public static String hashSha512 (String input) {
		try {
			MessageDigest hash = MessageDigest.getInstance("SHA-512");
			hash.update(input.getBytes());
			byte[] mdbytes = hash.digest();
			StringBuffer sb = new StringBuffer();
		    for (int i = 0; i < mdbytes.length; i++) {
	          sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
	        }
	    	return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
}
