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
import co.edu.uniajc.rsu_authservice.dto.RegisterRequest;
import co.edu.uniajc.rsu_authservice.model.User;
import co.edu.uniajc.rsu_authservice.service.UserService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/user")
public class UserController {
	
	private final UserService userService;
	
	public UserController(UserService userService) {
		this.userService=userService;
	}
	
	@PostMapping("/create")
    public ApiResponse<User> create(@RequestBody RegisterRequest request, HttpServletRequest httpRequest) {

        User user = userService.create(request);

        return ApiResponse.ok("Creado con exito",user);
    }
    
    @PostMapping("/list")
    public ApiResponse<List<User>> register(HttpServletRequest httpRequest) {

        List<User> users = userService.showAll();

        return ApiResponse.ok("Creado con exito",users);
    }
}
