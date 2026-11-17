package controller;

import model.Admin;
import model.Kasir;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthController {

    public User login(String username, String password) throws SQLException {
        String sql = "SELECT id, username, password, nama_lengkap, role FROM users WHERE username = ? AND password = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                int id = rs.getInt("id");
                String user = rs.getString("username");
                String pass = rs.getString("password");
                String nama = rs.getString("nama_lengkap");
                if ("ADMIN".equalsIgnoreCase(rs.getString("role"))) {
                    return new Admin(id, user, pass, nama);
                }
                return new Kasir(id, user, pass, nama);
            }
        }
    }
}