package co.edu.uniajc.rsu_authservice.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniajc.rsu_authservice.dto.ApiResponse;
import co.edu.uniajc.rsu_authservice.dto.RoleRequest;
import co.edu.uniajc.rsu_authservice.model.Role;
import co.edu.uniajc.rsu_authservice.service.RoleService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/role")
public class RoleController {
	
	private final RoleService roleService;
	
	public RoleController(RoleService roleService) {
		this.roleService=roleService;
	}
	
	@PostMapping("/create")
    public ApiResponse<Role> create(@RequestBody RoleRequest request, HttpServletRequest httpRequest) {

        Role role = roleService.create(request);

        return ApiResponse.ok("Creado con exito",role);
    }
    
    @PostMapping("/list")
    public ApiResponse<List<Role>> register(HttpServletRequest httpRequest) {

        List<Role> roles = roleService.showAll();

        return ApiResponse.ok("Creado con exito",roles);
    }
}
