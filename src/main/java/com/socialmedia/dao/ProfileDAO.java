package com.socialmedia.dao;

import com.socialmedia.db.DatabaseConnection;
import com.socialmedia.models.Profile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfileDAO {

    public Profile getProfile(int userId) {
        String sql = "SELECT * FROM profiles WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Profile(
                            rs.getInt("user_id"),
                            rs.getString("bio"),
                            rs.getString("picture_url"),
                            rs.getTimestamp("updated_at"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Database fetch error for profile: " + e.getMessage());
        }
        return null;
    }

    public boolean saveOrUpdateProfile(int userId, String bio, String pictureUrl) {
        String sql = "INSERT INTO profiles (user_id, bio, picture_url) VALUES (?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE bio = ?, picture_url = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setString(2, bio);
            pstmt.setString(3, pictureUrl);
            pstmt.setString(4, bio);
            pstmt.setString(5, pictureUrl);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Database error saving profile: " + e.getMessage());
            return false;
        }
    }
}
