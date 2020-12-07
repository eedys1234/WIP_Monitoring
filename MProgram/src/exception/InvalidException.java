package exception;

public class InvalidException extends RuntimeException {
	public InvalidException() {
		super("입력값 검증실패");
	}
}
