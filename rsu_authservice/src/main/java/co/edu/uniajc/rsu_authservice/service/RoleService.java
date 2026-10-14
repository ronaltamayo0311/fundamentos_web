package co.edu.uniajc.rsu_authservice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;


import co.edu.uniajc.rsu_authservice.dto.RoleRequest;
import co.edu.uniajc.rsu_authservice.exception.BusinessException;
import co.edu.uniajc.rsu_authservice.exception.ForbiddenException;
import co.edu.uniajc.rsu_authservice.exception.UnauthorizedException;
import co.edu.uniajc.rsu_authservice.model.Role;
import co.edu.uniajc.rsu_authservice.repository.RoleRepository;

@Service
public class RoleService {

	private final RoleRepository roleRepository;
	
	public RoleService(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	public Role create(RoleRequest request) {

		if (request == null) {
			throw new BusinessException("La solicitud no puede ser nula");
		}

		if (request.getCode() == null || request.getCode().trim().isEmpty()) {
			throw new BusinessException("El codigo es obligatorio");
		}

		if (request.getName() == null || request.getName().trim().isEmpty()) {
			throw new BusinessException("El nombre es obligatorio");
		}

		Optional<Role> existingRole = roleRepository.findByCode(request.getCode());
		if (existingRole.isPresent()) {
			throw new BusinessException("El username ya se encuentra registrado");
		}

		Role role = new Role();
		role.setIdRole(roleRepository.getNextId());
		role.setName(request.getName());
		role.setCode(request.getCode().trim());
		//role.setCode(request.getDescription());

		roleRepository.create(role);

		
		return role;
	}

	public List<Role> showAll() {

		return roleRepository.showAll();
	}
}