package secure;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import encoding.EncoderUtil;

public class EncryptUtil {

	private EncryptUtil() {
		new AssertionError();
	}
	

    /**
     * AES 암호화 함수
     * @param strPlainText
     * @param strAESKey
     * @return
     */
    public static String encryptAES128(String strPlainText, String strAESKey)
    {
        String encryptString = null;

        try {

            byte[] keyBytes = new byte[16];
            byte[] b = strAESKey.getBytes("UTF-8");
            int len = b.length;

            if(len > keyBytes.length) {
                len = keyBytes.length;
            }

            System.arraycopy(b, 0, keyBytes, 0, len);
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(strAESKey.getBytes("UTF-8")));

            byte[] encrypt = cipher.doFinal(strPlainText.getBytes("UTF-8"));
            encryptString = new String(EncoderUtil.base64Encoding(encrypt));
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        catch (BadPaddingException e) {
            e.printStackTrace();
        }

        return encryptString;
    }

    /**
     * AES 복호화 함수
     * @param strEncryptText
     * @param strAESKey
     * @return
     */
    public static String decryptAES128(String strEncryptText, String strAESKey)
    {
        String decryptString = null;

        try {
            byte[] keyBytes = new byte[16];
            byte[] b = strAESKey.getBytes("UTF-8");
            int len = b.length;

            if(len > keyBytes.length) {
                len = keyBytes.length;
            }

            System.arraycopy(b, 0, keyBytes, 0, len);
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(strAESKey.getBytes("UTF-8")));
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] BaseDecode = decoder.decode(strEncryptText);
            byte[] decrypt = cipher.doFinal(BaseDecode);
            decryptString  = new String(decrypt);

        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        catch (BadPaddingException e) {
            e.printStackTrace();
        }

        return decryptString;
    }

    public static String encryptAES256(String strPlainText, String strAESKey) {

        String encryptString = "";

        try {
            SecureRandom random = new SecureRandom();

            byte bytes[] = new byte[20];

            random.nextBytes(bytes);

            byte[] saltBytes = bytes;

            // Password-Based Key Derivation function 2
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

            // 70000번 해시하여 256 bit 길이의 키를 만든다.
            PBEKeySpec spec = new PBEKeySpec(strAESKey.toCharArray(), saltBytes, 70000, 256);

            SecretKey secretKey = factory.generateSecret(spec);

            SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");

            // 알고리즘/모드/패딩
            // CBC : Cipher Block Chaining Mode
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secret);
            AlgorithmParameters params = cipher.getParameters();
            // Initial Vector(1단계 암호화 블록용)
            byte[] ivBytes = params.getParameterSpec(IvParameterSpec.class).getIV();
            byte[] encryptedTextBytes = cipher.doFinal(strPlainText.getBytes("UTF-8"));

            byte[] buffer = new byte[saltBytes.length + ivBytes.length + encryptedTextBytes.length];
            System.arraycopy(saltBytes, 0, buffer, 0, saltBytes.length);
            System.arraycopy(ivBytes, 0, buffer, saltBytes.length, ivBytes.length);
            System.arraycopy(encryptedTextBytes, 0, buffer, saltBytes.length + ivBytes.length, encryptedTextBytes.length);

            encryptString = EncoderUtil.base64Encoding(buffer);
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        catch (BadPaddingException e) {
            e.printStackTrace();
        }
        catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        catch (InvalidParameterSpecException e) {
            e.printStackTrace();
        }

        return encryptString;

    }

    public static String decryptAES256(String strEncryptText, String strAESKey) {

        String decryptString = "";
        try {

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            ByteBuffer buffer = ByteBuffer.wrap(EncoderUtil.base64DecodingByte(strEncryptText));

            byte[] saltBytes = new byte[20];
            buffer.get(saltBytes, 0, saltBytes.length);
            byte[] ivBytes = new byte[cipher.getBlockSize()];
            buffer.get(ivBytes, 0, ivBytes.length);
            byte[] encryoptedTextBytes = new byte[buffer.capacity() - saltBytes.length - ivBytes.length];
            buffer.get(encryoptedTextBytes);

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            PBEKeySpec spec = new PBEKeySpec(strAESKey.toCharArray(), saltBytes, 70000, 256);

            SecretKey secretKey = factory.generateSecret(spec);
            SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");

            cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(ivBytes));

            byte[] decryptedTextBytes = cipher.doFinal(encryoptedTextBytes);

            decryptString = new String(decryptedTextBytes);

        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        catch (BadPaddingException e) {
            e.printStackTrace();
        }
        catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }

        return decryptString;
    }

    static {
		Security.addProvider(new BouncyCastleProvider());		
	}
	
	public static KeyPair genRSAKeyPair() throws NoSuchAlgorithmException {
		
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		
		SecureRandom secureRandom = new SecureRandom();
		KeyPairGenerator gen;
		KeyPair keyPair = null;
		
		try {
			gen = KeyPairGenerator.getInstance("RSA");
			gen.initialize(2048, secureRandom);
			
			keyPair = gen.genKeyPair();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return keyPair;
	}
	
	public static String encryptRSA(String plainText, PublicKey publicKey)
	            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
	                      BadPaddingException, IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] bytePlain = cipher.doFinal(plainText.getBytes());
        String encrypted = Base64.getEncoder().encodeToString(bytePlain);
    	return encrypted;
   }
	 
   public static String decryptRSA(String encrypted, PrivateKey privateKey)
    		throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
    		         BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {


        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

        byte[] byteEncrypted = Base64.getDecoder().decode(encrypted.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] bytePlain = cipher.doFinal(byteEncrypted);
        String decrypted = new String(bytePlain, "utf-8");

        return decrypted;

    }

	public static String encryptStringRSA(String plainText, String strPublicKey)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
                      BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException {
	
		 KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		 byte[] bytePublicKey = Base64.getDecoder().decode(strPublicKey.getBytes());
		 X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(bytePublicKey);
		 PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
		
		 Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		 cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		 byte[] bytePlain = cipher.doFinal(plainText.getBytes());
		 String encrypted = Base64.getEncoder().encodeToString(bytePlain);
		 return encrypted;
	}
 
	public static String decryptStringRSA(String encrypted, String strPrivateKey)
		throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
		         BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, InvalidKeySpecException {

		 KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		 byte[] bytePrivateKey = Base64.getDecoder().decode(strPrivateKey);
         PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(bytePrivateKey);
         PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
         
	     Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
	
	     byte[] byteEncrypted = Base64.getDecoder().decode(encrypted.getBytes());
	     cipher.init(Cipher.DECRYPT_MODE, privateKey);
	     byte[] bytePlain = cipher.doFinal(byteEncrypted);
	     String decrypted = new String(bytePlain, "UTF-8");
	
 	     return decrypted;

	}	
}
