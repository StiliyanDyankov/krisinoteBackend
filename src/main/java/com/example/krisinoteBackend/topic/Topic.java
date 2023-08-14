package com.example.krisinoteBackend.topic;

import jakarta.annotation.Nullable;
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
    @Nullable
    private Number createdAt;
    private Number lastModified;
    private String color;
}
