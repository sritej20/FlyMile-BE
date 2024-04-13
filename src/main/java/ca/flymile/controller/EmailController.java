package ca.flymile.controller;

import jakarta.validation.constraints.Email;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;

import java.util.Set;
import java.util.regex.Pattern;
@CrossOrigin(origins = "*")
@RestController
@Validated
public class EmailController {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private final StringRedisTemplate redisTemplate;

    public EmailController(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostMapping("/saveEmail")
    public String saveEmail(@RequestParam @Email String email) {
        email = email.toLowerCase().trim(); // Normalize the email for consistency
        if (!isEmailValid(email)) {
            return "Invalid email format.";
        }
        if (Boolean.TRUE.equals(redisTemplate.hasKey("email:" + email))) {
            return "Email already exists in the database.";
        } else {
            redisTemplate.opsForValue().set("email:" + email, email);
            return "Email stored successfully:" + email;
        }
    }

    private static boolean isEmailValid(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    // USE LATER
    public Set<String> getAllEmails() {
        return redisTemplate.keys("email:*");
    }
}

