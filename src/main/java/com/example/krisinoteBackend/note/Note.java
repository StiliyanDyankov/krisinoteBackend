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
// T should be either Delta or String
public class Note {
    private String id;
    private String noteName;
    private List<String> topics;
    private String description;
    private Number createdAt;
    private Number metaLastModified;
    private Number contentLastModified;
    @Nullable
    private NoteType type;
    private String content;
    @Nullable
    private String fromUrl;

}
