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
public class UserService {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public User create(RegisterRequest request) {

		if (request == null) {
			throw new BusinessException("La solicitud no puede ser nula");
		}

		if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
			throw new BusinessException("El username es obligatorio");
		}

		if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
			throw new BusinessException("La contraseña es obligatoria");
		}

		if (request.getName() == null || request.getName().trim().isEmpty()) {
			throw new BusinessException("El nombre es obligatorio");
		}

		if (request.getRole() == null || request.getRole().trim().isEmpty()) {
			throw new BusinessException("El rol es obligatorio");
		}

		Optional<User> existingUser = userRepository.findByUsername(request.getUsername());
		if (existingUser.isPresent()) {
			throw new BusinessException("El username ya se encuentra registrado");
		}

		User user = new User();
		user.setIdUser(userRepository.getNextId());
		user.setUsername(request.getUsername().trim());
		user.setPassword(passwordEncoder.encode(request.getPassword().trim()));
		user.setName(request.getName().trim());
		user.setRole(request.getRole().trim().toUpperCase());
		user.setState("ACTIVO".toUpperCase());

		userRepository.create(user);

		user.setPassword(null);
		return user;
	}

	public List<User> showAll() {

		return userRepository.showAll();
	}
}