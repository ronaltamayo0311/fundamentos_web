package co.edu.uniajc.rsu_authservice.dto;

import java.time.OffsetDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ApiResponse<T> {

	private boolean succes;
	private String message;
	private T data;
	private OffsetDateTime timestamp;
	
	public static <T> ApiResponse<T> ok(T data){
		return new ApiResponse<>(
				true, 
				"Operación exítosa",
				data,
				OffsetDateTime.now()
				);
	}
	
	public static <T> ApiResponse<T> ok(String message, T data){
		return new ApiResponse<>(
				true, 
				message,
				data,
				OffsetDateTime.now()
				);
	}
	
	public static <T> ApiResponse<T> error(String message){
		return new ApiResponse<>(
				false, 
				message,
				null,
				OffsetDateTime.now()
				);
	}
	
	public static <T> ApiResponse<T> error(String message, T data){
		return new ApiResponse<>(
				false, 
				message,
				data,
				OffsetDateTime.now()
				);
	}
	
}
