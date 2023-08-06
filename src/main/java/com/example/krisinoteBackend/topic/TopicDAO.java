package com.example.krisinoteBackend.topic;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TopicDAO {
    boolean save(Topic topic);

    boolean update(Topic topic);

    boolean delete(String id);

//    List<Topic> getAll(Number userId);

//    Optional<Topic> getTopicById(String id);

//    Map<String, TopicSyncData> getSyncData(Number userId);
}
