package hibernate.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CryptographyUtil {

	public static String encrypt(String senha) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			md.update(senha.getBytes(), 0, senha.length());
			return new BigInteger(1, md.digest()).toString(16);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
}
