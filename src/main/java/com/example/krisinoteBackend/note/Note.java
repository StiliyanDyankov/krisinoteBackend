package com.example.krisinoteBackend.note;

import com.example.krisinoteBackend.Delta.Delta;
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
public class Note {
    private String id;
    @Nullable
    private String noteName;
    @Nullable
    private List<String> topics;
    @Nullable
    private String description;
    @Nullable
    private Number createdAt;
    @Nullable
    private Number lastModified;
    @Nullable
    private String content;
    @Nullable
    private Delta delta;
}
