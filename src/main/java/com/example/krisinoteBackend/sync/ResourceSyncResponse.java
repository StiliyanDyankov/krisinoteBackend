package com.example.krisinoteBackend.sync;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResourceSyncResponse {
    private Map<Pace, SyncDiffs> clientAndServerDifferences;
}
