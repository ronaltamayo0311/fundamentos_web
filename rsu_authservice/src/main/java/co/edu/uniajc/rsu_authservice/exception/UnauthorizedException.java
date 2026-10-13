package co.edu.uniajc.rsu_authservice.exception;

public class UnauthorizedException extends RuntimeException{

	public UnauthorizedException(String message) {
		super(message);
	}
}
