package encoding;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

public class EncoderUtil {

	/**
	 * URLEncoding 함수
	 * @param strPlainText
	 * @return
	 */
	public static String urlEncoding(String strPlainText) 
	{
		String strEncodingText = null;

		try {
			strEncodingText = URLEncoder.encode(strPlainText, "UTF-8");
		} 
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} 
		return strEncodingText;
	}
	
	/**
	 * URLDecoding 함수
	 * @param strPlainText
	 * @return
	 */
	public static String urlDecoding(String strPlainText) 
	{
		String strDecodingText = null;

		try {
			strDecodingText = URLDecoder.decode(strPlainText, "UTF-8");
		} 
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} 
		return strDecodingText;
	}
	
	/**
	 * Base64 Encoding 함수
	 * @param strPlainText
	 * @return
	 */
	public static String base64Encoding(String strPlainText)
	{
		Encoder encoder = Base64.getEncoder();
		byte[] BaseEncode =  encoder.encode(strPlainText.getBytes());
		return new String(BaseEncode);	
	}
	
	/**
	 * Base64 Encoding 함수
	 * @param strPlainText
	 * @return
	 */
	public static String base64Encoding(byte[] encoding)
	{
		Encoder encoder = Base64.getEncoder();
		byte[] BaseEncode =  encoder.encode(encoding);
		return new String(BaseEncode);	
	}
	
	/**
	 * Base64 Decoding 함수
	 * @param strPlainText
	 * @return
	 */
	public static String base64Decoding(String strPlainText)
	{
		Decoder decoder = Base64.getDecoder();

		byte[] BaseDecode = decoder.decode(strPlainText);
		try {
			return new String(BaseDecode, "UTF-8");
		} 
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Base64 Decoding 함수
	 * @param strPlainText
	 * @return
	 */
	public static String base64Decoding(byte[] decoding)
	{
		Decoder decoder = Base64.getDecoder();

		byte[] BaseDecode = decoder.decode(decoding);
		try {
			return new String(BaseDecode, "UTF-8");
		} 
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
	

    public static byte[] base64DecodingByte(String strPlainText) {

        Base64.Decoder decoder = Base64.getDecoder();
        return decoder.decode(strPlainText);
    }
}
