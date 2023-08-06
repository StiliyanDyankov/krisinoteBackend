package com.example.krisinoteBackend.note;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface NoteDAO {
    boolean save(Number userId, Note note);

    String getNoteContent(String id);

    boolean updateMetadata(Note note);

    int updateContent(String id, String newContent);

    boolean delete(String id);

//    List<Note> getAll(Number userId);

    Optional<Note> getNoteById(String id);

}
