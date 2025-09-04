package lk.vihanganimsara.classsphere.service.impl;

import lk.vihanganimsara.classsphere.dto.AuthDto;
import lk.vihanganimsara.classsphere.dto.AuthResponseDto;
import lk.vihanganimsara.classsphere.dto.RegisterDTO;
import lk.vihanganimsara.classsphere.entity.Role;
import lk.vihanganimsara.classsphere.entity.User;
import lk.vihanganimsara.classsphere.repository.UserRepo;
import lk.vihanganimsara.classsphere.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepo userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthResponseDto authenticate(AuthDto authDto) {
        User user = userRepository.findByUsername(authDto.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));


        if (!passwordEncoder.matches(authDto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }
        String token = jwtUtil.genarateToken(authDto.getUsername());
        return new AuthResponseDto(token);
    }


    public String register(RegisterDTO registerDto) {
        if (userRepository.findByUsername(registerDto.getUsername()).isPresent()) {
            throw new RuntimeException("Username is already in use");
        }
        User user = User.builder().username(registerDto.getUsername())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .role(Role.valueOf(registerDto.getRole())).build();
        userRepository.save(user);
        return "User registered successfully";
    }
}
