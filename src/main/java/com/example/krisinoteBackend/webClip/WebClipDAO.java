package com.example.krisinoteBackend.webClip;

import com.example.krisinoteBackend.note.Note;

import java.util.List;

public interface WebClipDAO {
    boolean save(Number userId, WebClip webClip);

    boolean updateMetadata(WebClip webClip);

    boolean delete(String id);

    List<WebClip> getMissingWebClips(Number userId, List<String> hasIds);

    List<WebClip> getAllWebClips(Number userId);
}
