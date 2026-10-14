package co.edu.uniajc.rsu_authservice.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniajc.rsu_authservice.dto.ApiResponse;
import co.edu.uniajc.rsu_authservice.dto.LoginRequest;
import co.edu.uniajc.rsu_authservice.dto.LoginResponse;
import co.edu.uniajc.rsu_authservice.dto.RegisterRequest;
import co.edu.uniajc.rsu_authservice.model.User;
import co.edu.uniajc.rsu_authservice.service.AuthService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
	
	private final AuthService authService;
	
	public AuthController(AuthService authService) {
		this.authService=authService;
	}
	
	@PostMapping("/login")
	public ApiResponse<LoginResponse> login(@Validated @RequestBody LoginRequest loginRequest)
	{
		LoginResponse data=authService.login(loginRequest);
		return ApiResponse.ok("Credenciales validas", data);
	}

    /*@PostMapping("/register")
    public ApiResponse<User> register(@RequestBody RegisterRequest request, HttpServletRequest httpRequest) {

        User user = authService.register(request);

        return ApiResponse.ok("Creado con exito",user);
    }*/
    
    @PostMapping("/list")
    public ApiResponse<List<User>> register(HttpServletRequest httpRequest) {

        List<User> users = authService.showAll();

        return ApiResponse.ok("Creado con exito",users);
    }
	
	
}
