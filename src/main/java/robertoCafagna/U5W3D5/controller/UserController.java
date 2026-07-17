package robertoCafagna.U5W3D5.controller;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import robertoCafagna.U5W3D5.entities.User;
import robertoCafagna.U5W3D5.services.UserService;

@RestController
@RequestMapping("/user")
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
}
