package co.edu.uniajc.rsu_authservice.exception;

public class ForbiddenException extends RuntimeException{

	public ForbiddenException(String message) {
		super(message);
	}
}
