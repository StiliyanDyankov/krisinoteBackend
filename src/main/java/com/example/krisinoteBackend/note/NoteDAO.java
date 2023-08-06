package com.example.krisinoteBackend.note;

import com.example.krisinoteBackend.sync.NoteSyncData;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface NoteDAO {
    int save(Note note, Number userId);

    int update(Note note);

    int delete(String id);

    List<Note> getAll(Number userId);

    Optional<Note> getNoteById(String id);

    Map<String, NoteSyncData> getSyncData(Number userId);
}
