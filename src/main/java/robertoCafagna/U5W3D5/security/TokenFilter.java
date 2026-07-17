package robertoCafagna.U5W3D5.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import robertoCafagna.U5W3D5.entities.User;
import robertoCafagna.U5W3D5.exceptions.UnauthorizedException;
import robertoCafagna.U5W3D5.services.UserService;

import java.io.IOException;

@Component
public class TokenFilter extends OncePerRequestFilter {
    private final JWTTools jwtTools;
    private final UserService userService;

    public TokenFilter(JWTTools jwtTools, UserService userService) {
        this.jwtTools = jwtTools;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer "))
            throw new UnauthorizedException("inserire il token del giusto formato");

        String accessToken = authHeader.replace("Bearer ", "");

        this.jwtTools.verifyToken(accessToken);

        
        Long userId = this.jwtTools.extractIdFromToken(accessToken);
        User found = this.userService.findById(userId);

        Authentication authentication = new UsernamePasswordAuthenticationToken(found, null,
                found.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);


        filterChain.doFilter(request, response);
    }


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return new AntPathMatcher().match("/auth/**", request.getServletPath());

    }
}