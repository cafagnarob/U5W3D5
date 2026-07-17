package robertoCafagna.U5W3D5.controller;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import robertoCafagna.U5W3D5.DTO.LoginDTO;
import robertoCafagna.U5W3D5.DTO.LoginResponseDTO;
import robertoCafagna.U5W3D5.DTO.UserDTO;
import robertoCafagna.U5W3D5.DTO.UserResponseDTO;
import robertoCafagna.U5W3D5.entities.User;
import robertoCafagna.U5W3D5.exceptions.ValidationException;
import robertoCafagna.U5W3D5.services.AuthService;
import robertoCafagna.U5W3D5.services.UserService;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;


    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginDTO body) {
        return new LoginResponseDTO(this.authService.checkAndGenerate(body));
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDTO saveUser(@RequestBody @Validated UserDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            throw new ValidationException(errorsList);
        }
        User saved = this.userService.save(body);
        return new UserResponseDTO(saved.getId());
    }
}
