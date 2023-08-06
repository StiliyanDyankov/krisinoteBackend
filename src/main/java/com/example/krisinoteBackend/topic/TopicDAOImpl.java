package com.example.krisinoteBackend.topic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Repository
public class TopicDAOImpl implements  TopicDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public boolean save(Topic topic) {
        String sql = "INSERT INTO topics (id, topicName, description, createdAt, lastModified, color) VALUES (?, ?, ?, ?, ?, ?)";

// Create an array of parameters for the query
        Object[] params = new Object[6];
        params[0] = topic.getId();
        params[1] = topic.getTopicName();
        params[2] = topic.getDescription();
        params[3] = topic.getCreatedAt();
        params[4] = topic.getLastModified();
        params[5] = topic.getColor();

// Execute the query and return the number of rows affected
        int rows = jdbcTemplate.update(sql, params);
        boolean result = rows > 0;
        return result;
    }

    @Override
    public boolean update(Topic topic) {
        // Create a SQL query to update an existing topic in the topics table
        String sql = "UPDATE topics SET topicName = ?, description = ?, createdAt = ?, lastModified = ?, color = ? WHERE id = ?";

// Create an array of parameters for the query
        Object[] params = new Object[6];
        params[0] = topic.getTopicName();
        params[1] = topic.getDescription();
        params[2] = topic.getCreatedAt();
        params[3] = topic.getLastModified();
        params[4] = topic.getColor();
        params[5] = topic.getId();

// Execute the query and return the number of rows affected
        int rows = jdbcTemplate.update(sql, params);

// Check if the update was successful and return a boolean value
        boolean result = rows > 0;
        return result;
    }

    @Transactional
    @Override
    public boolean delete(String id) {
        String sql = "DELETE FROM topics WHERE id = ?";

// Create an array of parameters for the query
        Object[] params = new Object[1];
        params[0] = id;

// Execute the query and return the number of rows affected
        int rows = jdbcTemplate.update(sql, params);

// Check if the deletion was successful and return a boolean value
        boolean result = rows > 0;

// If the deletion was successful, delete the topic from the NoteTopic and WebClipTopic tables as well
        if (result) {
            // Create a SQL query to delete the topic from the NoteTopic table
            String sql2 = "DELETE FROM NoteTopic WHERE topicId = ?";

            // Execute the query and return the number of rows affected
            int rows2 = jdbcTemplate.update(sql2, params);

            // Create a SQL query to delete the topic from the WebClipTopic table
            String sql3 = "DELETE FROM WebClipTopic WHERE topicId = ?";

            // Execute the query and return the number of rows affected
            int rows3 = jdbcTemplate.update(sql3, params);
        }
        return result;
    }

//    @Override
//    public List<Topic> getAll(Number userId) {
//        return null;
//    }

//    @Override
//    public Optional<Topic> getTopicById(String id) {
//        return Optional.empty();
//    }
}
