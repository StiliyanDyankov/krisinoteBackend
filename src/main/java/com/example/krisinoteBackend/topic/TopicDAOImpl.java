package com.example.krisinoteBackend.topic;

import com.example.krisinoteBackend.sync.TopicSyncData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class TopicDAOImpl implements  TopicDAO {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public int save(Topic topic, Number userId) {
        return 0;
    }

    @Override
    public int update(Topic topic) {
        return 0;
    }

    @Override
    public int delete(String id) {
        return 0;
    }

    @Override
    public List<Topic> getAll(Number userId) {
        return null;
    }

    @Override
    public Optional<Topic> getTopicById(String id) {
        return Optional.empty();
    }

    @Override
    public Map<String, TopicSyncData> getSyncData(Number userId) {
        String sql = "SELECT t.id, t.metaLastModified FROM topics t JOIN NoteTopic nt ON t.id = nt.topicId JOIN UserNote un ON nt.noteId = un.noteId WHERE un.userId = ?";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, new Object[]{userId});
        Map<String, TopicSyncData> topics = new HashMap<>();
        for (Map<String, Object> row : rows) {
            TopicSyncData topic = new TopicSyncData();
            topic.setId((String) row.get("id"));
            topic.setMetaLastModified((Number) row.get("metaLastModified"));
            topics.put(topic.getId(), topic);
        }

        return topics;
    }
}
