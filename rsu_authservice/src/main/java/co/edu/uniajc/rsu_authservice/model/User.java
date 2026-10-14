package co.edu.uniajc.rsu_authservice.model;

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
public class User {

	private Long idUser;
	private String username;
	private String password;
	private String name;
	private String role;
	private String state;
	
}
