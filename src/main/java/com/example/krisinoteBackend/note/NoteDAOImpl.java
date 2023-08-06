package com.example.krisinoteBackend.note;

import com.example.krisinoteBackend.Delta.Delta;
import com.example.krisinoteBackend.sync.NoteSyncData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.*;

@Repository
public class NoteDAOImpl implements NoteDAO {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public int save(Note note, Number userId) {
        return 0;
    }

    @Override
    public int update(Note note) {
        return 0;
    }

    @Override
    public int delete(String id) {
        return 0;
    }

    @Override
    public List<Note> getAll(Number userId) {
        return null;
    }

    @Override
    public Optional<Note> getNoteById(String noteId) {
        String sqlForNote = "SELECT * FROM notes WHERE id = ?";

        Map<String, Object> note = jdbcTemplate.queryForMap(sqlForNote, noteId);


        if(note.isEmpty()) {
            return Optional.empty();
        } else {


            String sqlForTopics = "SELECT t.id " +
                    "FROM notes n " +
                    "JOIN NoteTopic nt ON n.id = nt.noteId " +
                    "JOIN topics t ON nt.topicId = t.id " +
                    "WHERE n.id = ?";

            List<Map<String, Object>> topics = jdbcTemplate.queryForList(sqlForTopics, noteId);

            List<String> topicIds = new ArrayList<>();
            for (Map<String, Object> topic : topics) {
                String id = (String) topic.get("id");
                topicIds.add(id);
            }

            var userNote = Note.builder()
                    .id((String) note.get("id"))
                    .noteName((String) note.get("noteName"))
                    .description((String) note.get("description"))
                    .createdAt((Number) note.get("createdAt"))
                    // need to chenge shite here
                    .metaLastModified((Number) note.get("lastModified"))
                    .contentLastModified((Number) note.get("lastModified"))
                    .topics(topicIds)
                    .fromUrl((String) note.get("fromUrl"))
                    .type(NoteType.valueOf((String) note.get("type")))
                    .content((String) note.get("content"))
                    .build();

            var content = new Delta().insert((String) note.get("content"));
        }
        return Optional.empty();
    }

    @Override
    public Map<String, NoteSyncData> getSyncData(Number userId) {
//        String sql = "SELECT n.id, n.metaLastModified, n.contentLastModified FROM notes n JOIN UserNote un ON n.id = un.noteId WHERE un.userId = ?";
//        ArrayList<NoteSyncData> notes = (ArrayList<NoteSyncData>) jdbcTemplate.query(sql, new Object[]{userId}, noteSyncDataRowMapper);

        String sql = "SELECT n.id, n.metaLastModified, n.contentLastModified FROM notes n JOIN UserNote un ON n.id = un.noteId WHERE un.userId = ?";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, new Object[]{userId});
        Map<String, NoteSyncData> notes = new HashMap<>();
        for (Map<String, Object> row : rows) {
            NoteSyncData note = new NoteSyncData();
            note.setId((String) row.get("id"));
            note.setMetaLastModified((Number) row.get("metaLastModified"));
            note.setContentLastModified((Number) row.get("contentLastModified"));
            notes.put(note.getId(), note);
        }

        return notes;
    }
}
