package com.example.krisinoteBackend.topic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Topic {
    private String id;
    private String topicName;
    private String description;
    private Number createdAt;
    private Number metaLastModified;
    private String color;
}
