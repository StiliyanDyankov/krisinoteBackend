package com.example.krisinoteBackend.sync;

import com.example.krisinoteBackend.note.Note;
import com.example.krisinoteBackend.topic.Topic;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SyncDiffs {
    @Nullable
    private List<ResourceDiff<Note>> notes;
    @Nullable
    private List<ResourceDiff<Topic>> topics;
}
