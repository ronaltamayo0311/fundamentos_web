package co.edu.uniajc.rsu_authservice.dto;

import javax.validation.constraints.NotBlank;

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
public class LoginRequest {

	@NotBlank(message="Campo username es obligatorio.")
	private String username;
	
	@NotBlank(message="Campo password es obligatorio.")
	private String password;
	
}
