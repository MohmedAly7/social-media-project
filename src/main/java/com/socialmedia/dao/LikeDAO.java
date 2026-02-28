package com.socialmedia.dao;

import com.socialmedia.db.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LikeDAO {

    public boolean toggleLike(int postId, int userId) {
        if (hasLiked(postId, userId)) {
            return removeLike(postId, userId);
        } else {
            return addLike(postId, userId);
        }
    }

    public boolean hasLiked(int postId, int userId) {
        String sql = "SELECT 1 FROM likes WHERE post_id = ? AND user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, postId);
            pstmt.setInt(2, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Error checking like: " + e.getMessage());
        }
        return false;
    }

    private boolean addLike(int postId, int userId) {
        String sql = "INSERT INTO likes (post_id, user_id) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, postId);
            pstmt.setInt(2, userId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    private boolean removeLike(int postId, int userId) {
        String sql = "DELETE FROM likes WHERE post_id = ? AND user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, postId);
            pstmt.setInt(2, userId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public int getLikeCount(int postId) {
        String sql = "SELECT COUNT(*) AS total FROM likes WHERE post_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, postId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next())
                    return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("Error getting like count: " + e.getMessage());
        }
        return 0;
    }
}
