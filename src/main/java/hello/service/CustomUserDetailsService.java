package hello.service;

import hello.DatabaseConnector;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final DatabaseConnector connector;

    public CustomUserDetailsService(DatabaseConnector connector) {
        this.connector = connector;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Connection conn = connector.getConnection();

        if (conn == null) {
            throw new UsernameNotFoundException("Немає підключення до бази даних");
        }

        try (PreparedStatement pstmt = conn.prepareStatement("SELECT username, password, role FROM users WHERE username = ?")) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return User.builder()
                        .username(rs.getString("username"))
                        .password(rs.getString("password"))
                        .roles(rs.getString("role").replace("ROLE_", ""))
                        .build();
            } else {
                throw new UsernameNotFoundException("User not found");
            }
        } catch (Exception e) {
            throw new UsernameNotFoundException("Error loading user", e);
        } finally {
            DatabaseConnector.close(conn);
        }
    }
}