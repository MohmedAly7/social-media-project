package com.socialmedia.dao;

import com.socialmedia.db.DatabaseConnection;
import com.socialmedia.models.Notification;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO {

    public void createNotification(int userId, String message, String type) {
        String sql = "INSERT INTO notifications (user_id, message, type) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, message);
            pstmt.setString(3, type);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error creating notification: " + e.getMessage());
        }
    }

    public List<Notification> getUnreadNotifications(int userId) {
        List<Notification> notifs = new ArrayList<>();
        String sql = "SELECT * FROM notifications WHERE user_id = ? AND is_read = FALSE ORDER BY created_at DESC";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    notifs.add(new Notification(
                            rs.getInt("id"),
                            rs.getInt("user_id"),
                            rs.getString("message"),
                            rs.getString("type"),
                            rs.getBoolean("is_read"),
                            rs.getTimestamp("created_at")));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching notifications: " + e.getMessage());
        }
        return notifs;
    }

    public void markAsRead(int notificationId) {
        String sql = "UPDATE notifications SET is_read = TRUE WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, notificationId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error marking notification as read: " + e.getMessage());
        }
    }
}
