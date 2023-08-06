package com.example.krisinoteBackend.sync;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TopicSyncData {
    private String id;
    private Number metaLastModified;
}
