package todoList.demo.controller;

import todoList.demo.domain.User;
import todoList.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @PostMapping("/signup")
    public String signup(@RequestBody User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return "이미 존재하는 이메일입니다.";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "회원가입 성공";
    }

    @PostMapping("/login")
    public User login(@RequestBody User user) {
        return userRepository.findByEmail(user.getEmail())
                .map(existingUser -> {
                    if (passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
                        return existingUser;
                    } else {
                        return null;
                    }
                })
                .orElse(null);
    }
}
