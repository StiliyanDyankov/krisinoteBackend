package com.example.krisinoteBackend.note;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface NoteDAO {
    boolean save(Number userId, Note note);

    String getNoteContent(String id);

    boolean updateMetadata(Note note);

    boolean updateContent(String id, String newContent);

    boolean delete(String id);

    Optional<Note> getNoteById(String id);

}
