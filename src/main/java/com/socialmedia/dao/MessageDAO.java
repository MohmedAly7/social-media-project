package com.socialmedia.dao;

import com.socialmedia.db.DatabaseConnection;
import com.socialmedia.models.Message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {

    public boolean sendMessage(int senderId, int receiverId, String content) {
        String sql = "INSERT INTO messages (sender_id, receiver_id, content) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, senderId);
            pstmt.setInt(2, receiverId);
            pstmt.setString(3, content);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Database error sending message: " + e.getMessage());
            return false;
        }
    }

    public List<Message> getChatHistory(int userId1, int userId2) {
        List<Message> history = new ArrayList<>();
        String sql = "SELECT m.*, u.name as sender_name FROM messages m " +
                "JOIN users u ON m.sender_id = u.id " +
                "WHERE (m.sender_id = ? AND m.receiver_id = ?) " +
                "   OR (m.sender_id = ? AND m.receiver_id = ?) " +
                "ORDER BY m.sent_at ASC";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId1);
            pstmt.setInt(2, userId2);
            pstmt.setInt(3, userId2);
            pstmt.setInt(4, userId1);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Message msg = new Message(
                            rs.getInt("id"),
                            rs.getInt("sender_id"),
                            rs.getInt("receiver_id"),
                            rs.getString("content"),
                            rs.getTimestamp("sent_at"));
                    msg.setSenderName(rs.getString("sender_name"));
                    history.add(msg);
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error fetching chat history: " + e.getMessage());
        }
        return history;
    }
}
