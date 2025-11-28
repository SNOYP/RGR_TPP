package hello.controller;

import hello.DatabaseConnector;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Connection;
import java.sql.PreparedStatement;

@Controller
public class AuthController {

    private final DatabaseConnector connector;
    private final PasswordEncoder passwordEncoder;

    public AuthController(DatabaseConnector connector, PasswordEncoder passwordEncoder) {
        this.connector = connector;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String login() { return "login"; }

    @GetMapping("/register")
    public String register() { return "register"; }

    @PostMapping("/auth/register")
    public String registerUser(@RequestParam String username, @RequestParam String password) {
        Connection conn = connector.getConnection();

        if (conn == null) {
            System.err.println("❌ ПОМИЛКА: conn == null. База даних недоступна!");
            return "redirect:/register?error";
        }

        try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO users (username, password, role) VALUES (?, ?, 'ROLE_USER')")) {

            pstmt.setString(1, username);
            pstmt.setString(2, passwordEncoder.encode(password));
            pstmt.executeUpdate();

            return "redirect:/login?registered";
        } catch (Exception e) {
            // --- ДОДАЙТЕ ЦЕЙ РЯДОК, ЩОБ БАЧИТИ ПОМИЛКУ В КОНСОЛІ ---
            System.err.println("❌ ПОМИЛКА РЕЄСТРАЦІЇ:");
            e.printStackTrace();
            // -------------------------------------------------------
            return "redirect:/register?error";
        } finally {
            DatabaseConnector.close(conn);
        }
    }
}