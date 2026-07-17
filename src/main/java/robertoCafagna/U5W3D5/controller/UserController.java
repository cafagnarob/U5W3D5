package robertoCafagna.U5W3D5.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import robertoCafagna.U5W3D5.DTO.PasswordChangeDTO;
import robertoCafagna.U5W3D5.DTO.UserDTO;
import robertoCafagna.U5W3D5.DTO.UserResponseDTO;
import robertoCafagna.U5W3D5.entities.User;
import robertoCafagna.U5W3D5.exceptions.ValidationException;
import robertoCafagna.U5W3D5.services.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    // 1. GET http://localhost:3001/user?page=1&size=3&orderBy=name --> 200 OK    ARRAY DI USERS
    @GetMapping
    public Page<User> getUsers(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "5") int size,
                               @RequestParam(defaultValue = "name") String orderBy) {
        return this.userService.getAll(page, size, orderBy);
    }

    @GetMapping("/me")
    public User getOwnProfile(@AuthenticationPrincipal User authUser) {
        return authUser;
    }


    @PutMapping("/me")
    public User updateOwnProfile(@AuthenticationPrincipal User authUser, @RequestBody UserDTO body) {
        return this.userService.findAndUpdate(authUser.getId(), body);
    }


    @DeleteMapping("/me")
    public void deleteOwnProfile(@AuthenticationPrincipal User authUser) {
        this.userService.findAndDelete(authUser.getId());
    }

    @PatchMapping("/me/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePassword(@AuthenticationPrincipal User authUser, @RequestBody PasswordChangeDTO body) {
        this.userService.updatePass(authUser.getId(), body);
    }

    // 2. POST http://localhost:3001/users (+req.body) --> 201 CREATED    ID USERS APPENA CREATO
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // 201
    public UserResponseDTO saveUser(@Valid @RequestBody UserDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).toList();
            throw new ValidationException(errorsList);
        }
        User saved = this.userService.save(body);
        return new UserResponseDTO(saved.getId());
    }

    // 3. GET http://locahost:3001/users/{userId} --> 200 OK  USER TROVATO
    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyAuthority('ORGANIZZATORE')")
    public User getById(@PathVariable Long userId) {
        return this.userService.findById(userId);
    }

    // 4. PUT http://localhost:3001/users/{userId} (+payload) --> 200 OK  USER AGGIORNATO
    @PutMapping("/{userId}")
    @PreAuthorize("hasAnyAuthority('ORGANIZZATORE')")
    public User findByIdAndUpdate(@PathVariable long userId, @Valid @RequestBody UserDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).toList();
            throw new ValidationException(errorsList);
        }
        return this.userService.findAndUpdate(userId, body);
    }

    //5.DELETE http://localhost:3001/user/{userId} --> 204 NO CONTENT
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAnyAuthority('ORGANIZZATORE')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByIdAndDelete(@PathVariable long userId) {
        this.userService.findAndDelete(userId);
    }


}
