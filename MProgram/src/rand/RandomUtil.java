package rand;

import java.util.Random;
import java.util.UUID;

public class RandomUtil {
	
	private RandomUtil() {
		new AssertionError();
	}
	

    /**
     * 소문자 생성 함수
     * @param digit
     * @return
     */
    public static String getSmallLetter(int digit) {

        StringBuilder sb = new StringBuilder();
        Random rnd = new Random();

        for(int i=0;i<digit; i++) {
            sb.append(String.valueOf((char)((int)rnd.nextInt(26) + 97)));
        }

        return sb.toString();
    }

    /**
     * 대문자 생성 함수
     * @param digit
     * @return
     */
    public static String getBigLetter(int digit) {
        StringBuilder sb = new StringBuilder();
        Random rnd = new Random();

        for(int i=0;i<digit; i++) {
            sb.append(String.valueOf((char)((int)rnd.nextInt(26) + 65)));
        }

        return sb.toString();
    }

    /**
     * 숫자 생성 함수
     * @param digit
     * @return
     */
    public static String getNum(int digit) {
        StringBuilder sb = new StringBuilder();
        Random rnd = new Random();

        for(int i=0;i<digit; i++) {
            sb.append(String.valueOf((char)((int)rnd.nextInt(10))));
        }

        return sb.toString();
    }

	/**
	 * 랜덤 문자열 생성 함수
	 * @param digit
	 * @return
	 */
	public static String getRandomString(int digit)
	{

		StringBuilder randString = new StringBuilder();
		
		Random rnd = new Random();
		int type = rnd.nextInt(3);
		
		for(int i=0;i<digit;i++)
		{
			switch(type) {
				//숫자
				case 0 : {
					randString.append(getNum(1));
					break;
				}
					//영소문자
				case 1 : {
					randString.append(getSmallLetter(1));
	
					break;
				}
				//영대문자
				case 2 : {
					randString.append(getBigLetter(1));				
					break;
				}
			}
		}
		
		return randString.toString();
	}
	
	/**
	 * UUID 생성 함수
	 * @return
	 */
	public static String createUUID()
	{
		String uuidString = null;
		uuidString = UUID.randomUUID().toString().replace("-", "").toUpperCase();
		return uuidString;
	}
}
