package lk.vihanganimsara.classsphere.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lk.vihanganimsara.classsphere.dto.ApiResponse;
import lk.vihanganimsara.classsphere.dto.AuthDto;
import lk.vihanganimsara.classsphere.dto.AuthResponseDto;
import lk.vihanganimsara.classsphere.dto.RegisterDTO;
import lk.vihanganimsara.classsphere.service.impl.AuthService;
import lk.vihanganimsara.classsphere.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtUtil jwtUtil;


    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerUser(
            @RequestBody RegisterDTO registerDTO) {
        return ResponseEntity.ok(
                new ApiResponse(
                        200,
                        "User registered successfully",
                        authService.register(registerDTO)
                )
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody AuthDto authDTO , HttpServletResponse response) {
        AuthResponseDto authenticate = authService.authenticate(authDTO);
        String genaratedToken = authenticate.getToken();

//        Cookie cookie = new Cookie("token", genaratedToken);
//        cookie.setPath("/");
//        cookie.setMaxAge(3600);
//        cookie.setHttpOnly(true);
//        cookie.setSecure(true);
//        //cookie.setSameSite("Lax");
//        cookie.setAttribute("Partitioned", "true");
//        response.addCookie(cookie);

        log.info("Set cookie with token: {}", genaratedToken);
        return ResponseEntity.ok(new ApiResponse(200,
                "OK", authenticate));
    }
}
