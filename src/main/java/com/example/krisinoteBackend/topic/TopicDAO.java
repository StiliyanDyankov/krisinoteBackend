package com.example.krisinoteBackend.topic;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TopicDAO {
    int save(Topic topic, Number userId);

    int update(Topic topic);

    int delete(String id);

    List<Topic> getAll(Number userId);

    Optional<Topic> getTopicById(String id);

//    Map<String, TopicSyncData> getSyncData(Number userId);
}
