package co.edu.uniajc.rsu_authservice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import co.edu.uniajc.rsu_authservice.dto.LoginRequest;
import co.edu.uniajc.rsu_authservice.dto.LoginResponse;
import co.edu.uniajc.rsu_authservice.dto.RegisterRequest;
import co.edu.uniajc.rsu_authservice.exception.BusinessException;
import co.edu.uniajc.rsu_authservice.exception.ForbiddenException;
import co.edu.uniajc.rsu_authservice.exception.UnauthorizedException;
import co.edu.uniajc.rsu_authservice.model.User;
import co.edu.uniajc.rsu_authservice.repository.UserRepository;

@Service
public class AuthService {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	public AuthService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public LoginResponse login(LoginRequest loginRequest) {

		User user = userRepository.findByUsername(loginRequest.getUsername())
				.orElseThrow(() -> new UnauthorizedException("El usuario es incorrecto"));

		if (!user.getState().equalsIgnoreCase("ACTIVO")) {
			throw new ForbiddenException("Usuario inactivo");
		}

		if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
			throw new UnauthorizedException("El password es incorrecto");
		}

		return new LoginResponse(user.getIdUser(), user.getUsername(), user.getName(), user.getRole());
	}


	public List<User> showAll() {

		return userRepository.showAll();
	}
}