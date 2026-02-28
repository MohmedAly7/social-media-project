package com.socialmedia.dao;

import com.socialmedia.db.DatabaseConnection;
import com.socialmedia.models.Comment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CommentDAO {

    public boolean addComment(int postId, int userId, String content) {
        String sql = "INSERT INTO comments (post_id, user_id, content) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, postId);
            pstmt.setInt(2, userId);
            pstmt.setString(3, content);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Database error adding comment: " + e.getMessage());
            return false;
        }
    }

    public List<Comment> getCommentsForPost(int postId) {
        List<Comment> comments = new ArrayList<>();
        String sql = "SELECT c.*, u.name as author_name FROM comments c JOIN users u ON c.user_id = u.id WHERE c.post_id = ? ORDER BY c.created_at ASC";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, postId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Comment comment = new Comment(
                            rs.getInt("id"),
                            rs.getInt("post_id"),
                            rs.getInt("user_id"),
                            rs.getString("content"),
                            rs.getTimestamp("created_at"));
                    comment.setAuthorName(rs.getString("author_name"));
                    comments.add(comment);
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error fetching comments: " + e.getMessage());
        }
        return comments;
    }
}
