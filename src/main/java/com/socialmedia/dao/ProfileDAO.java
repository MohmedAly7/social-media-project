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
                            rs.getBoolean("is_private"),
                            rs.getTimestamp("updated_at"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Database fetch error for profile: " + e.getMessage());
        }
        return null;
    }

    public boolean saveOrUpdateProfile(int userId, String bio, String pictureUrl, boolean isPrivate) {
        String sql = "INSERT INTO profiles (user_id, bio, picture_url, is_private) VALUES (?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE bio = ?, picture_url = ?, is_private = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setString(2, bio);
            pstmt.setString(3, pictureUrl);
            pstmt.setBoolean(4, isPrivate);
            pstmt.setString(5, bio);
            pstmt.setString(6, pictureUrl);
            pstmt.setBoolean(7, isPrivate);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Database error saving profile: " + e.getMessage());
            return false;
        }
    }
}
