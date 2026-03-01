package com.socialmedia.dao;

import com.socialmedia.db.DatabaseConnection;
import com.socialmedia.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FriendDAO {

    public boolean addFriend(int user1, int user2) {
        int minId = Math.min(user1, user2);
        int maxId = Math.max(user1, user2);

        String sql = "INSERT INTO friends (user_id1, user_id2, status) VALUES (?, ?, 'ACCEPTED') ON DUPLICATE KEY UPDATE status='ACCEPTED'";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, minId);
            pstmt.setInt(2, maxId);
            boolean success = pstmt.executeUpdate() > 0;
            if (success) {
                new NotificationDAO().createNotification(user1, "You are now friends!", "FRIEND");
                new NotificationDAO().createNotification(user2, "You are now friends!", "FRIEND");
            }
            return success;
        } catch (SQLException e) {
            System.err.println("Database error adding friend: " + e.getMessage());
            return false;
        }
    }

    public List<User> getFriends(int userId) {
        List<User> friends = new ArrayList<>();
        String sql = "SELECT u.* FROM users u JOIN friends f ON (u.id = f.user_id1 OR u.id = f.user_id2) " +
                "WHERE (f.user_id1 = ? OR f.user_id2 = ?) AND u.id != ? AND f.status = 'ACCEPTED'";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setInt(2, userId);
            pstmt.setInt(3, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    friends.add(new User(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            null, // omitting hash
                            rs.getTimestamp("created_at")));
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error fetching friends: " + e.getMessage());
        }
        return friends;
    }
}
