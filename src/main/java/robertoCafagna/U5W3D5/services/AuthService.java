package robertoCafagna.U5W3D5.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import robertoCafagna.U5W3D5.DTO.LoginDTO;
import robertoCafagna.U5W3D5.entities.User;
import robertoCafagna.U5W3D5.exceptions.UnauthorizedException;
import robertoCafagna.U5W3D5.security.JWTTools;

@Service
public class AuthService {
    private final UserService userService;
    private final JWTTools jwtTools;
    private final PasswordEncoder bcrypt;

    public AuthService(UserService userService,
                       JWTTools jwtTools, PasswordEncoder bcrypt) {
        this.userService = userService;
        this.jwtTools = jwtTools;
        this.bcrypt = bcrypt;
    }

    public String checkAndGenerate(LoginDTO body) {
        User found = this.userService.findByEmail(body.email());

        if (this.bcrypt.matches(body.password(), found.getPassword())) {
            return this.jwtTools.generateToken(found);
        } else {
            throw new UnauthorizedException("errore nelle credenziali");
        }
    }
}
