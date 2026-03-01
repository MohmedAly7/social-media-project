package com.socialmedia.dao;

import com.socialmedia.db.DatabaseConnection;
import com.socialmedia.models.Post;
import com.socialmedia.structures.PostList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PostDAO {

    public boolean createPost(int userId, String content, String imageUrl) {
        String sql = "INSERT INTO posts (user_id, content, image_url) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setString(2, content);
            pstmt.setString(3, imageUrl);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Database error creating post: " + e.getMessage());
            return false;
        }
    }

    // Fetches posts for a feed using pagination and friends
    public PostList getFeedPosts(int userId, int limit, int offset) {
        PostList posts = new PostList();
        String sql = "SELECT p.*, u.name as author_name FROM posts p " +
                "JOIN users u ON p.user_id = u.id " +
                "LEFT JOIN profiles prof ON p.user_id = prof.user_id " +
                "WHERE p.user_id = ? OR p.user_id IN (" +
                "   SELECT user_id2 FROM friends WHERE user_id1 = ? AND status='ACCEPTED' " +
                "   UNION " +
                "   SELECT user_id1 FROM friends WHERE user_id2 = ? AND status='ACCEPTED'" +
                ") " +
                "OR prof.is_private = FALSE OR prof.is_private IS NULL " +
                "ORDER BY p.created_at DESC LIMIT ? OFFSET ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setInt(2, userId);
            pstmt.setInt(3, userId);
            pstmt.setInt(4, limit);
            pstmt.setInt(5, offset);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Post post = new Post(
                            rs.getInt("id"),
                            rs.getInt("user_id"),
                            rs.getString("content"),
                            rs.getString("image_url"),
                            rs.getTimestamp("created_at"));
                    post.setAuthorName(rs.getString("author_name"));
                    posts.add(post);
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error fetching feed posts: " + e.getMessage());
        }
        return posts;
    }

    public int getPostOwnerId(int postId) {
        String sql = "SELECT user_id FROM posts WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, postId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next())
                    return rs.getInt("user_id");
            }
        } catch (SQLException e) {
            System.err.println("Database error fetching post owner: " + e.getMessage());
        }
        return -1;
    }
}
