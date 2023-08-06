package com.example.krisinoteBackend.note;

import com.example.krisinoteBackend.Delta.Delta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.util.*;

@Repository
public class NoteDAOImpl implements NoteDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private PlatformTransactionManager transactionManager;

    // create the transaction template
    private TransactionTemplate transactionTemplate;

    @Override
    public boolean save(Number userId, Note note) {
        return Boolean.TRUE.equals(transactionTemplate.execute(new TransactionCallback<Boolean>() {
            @Override
            public Boolean doInTransaction(TransactionStatus status) {
                try {
                    String sql1 = "INSERT INTO notes (id, noteName, description, createdAt, lastModified, content) VALUES (?, ?, ?, ?, ?, ?)";
                    jdbcTemplate.update(sql1, note.getId(), note.getNoteName(), note.getDescription(), note.getCreatedAt(), note.getLastModified(), note.getContent());

                    String sql2 = "INSERT INTO NoteTopic (noteId, topicId) VALUES (?, ?)";
                    for (String topic : note.getTopics()) {
                        jdbcTemplate.update(sql2, note.getId(), topic);
                    }

                    String sql3 = "INSERT INTO UserNote (userId, noteId) VALUES (?, ?)";
                    jdbcTemplate.update(sql3, userId, note.getId());

                    return true;
                } catch (Exception e) {
                    return false;
                }
            }
        }));
    }
    @Override
    public String getNoteContent(String id) {
        String sql = "SELECT content FROM notes WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, String.class, id);
    }

    @Override
    public boolean updateMetadata(Note note) {
        return Boolean.TRUE.equals(transactionTemplate.execute(new TransactionCallback<Boolean>() {
            @Override
            public Boolean doInTransaction(TransactionStatus status) {
                try {
                    String sql1 = "UPDATE notes SET noteName = ?, description = ?, createdAt = ?, lastModified = ? WHERE id = ?";
                    jdbcTemplate.update(sql1, note.getNoteName(), note.getDescription(), note.getCreatedAt(), note.getLastModified(), note.getId());

                    String sql2 = "DELETE FROM NoteTopic WHERE noteId = ?";
                    jdbcTemplate.update(sql2, note.getId());

                    String sql3 = "INSERT INTO NoteTopic (noteId, topicId) VALUES (?, ?)";
                    for (String topic : note.getTopics()) {
                        jdbcTemplate.update(sql3, note.getId(), topic);
                    }

                    return true;
                } catch (Exception e) {
                    return false;
                }
            }
        }));
    }

    @Override
    public int updateContent(String id, String newContent) {
        String sql = "UPDATE notes SET content = ? WHERE id = ?";
        int rows = jdbcTemplate.update(sql, newContent, id);
        return rows;
    }


    @Override
    public boolean delete(String id) {
        return Boolean.TRUE.equals(transactionTemplate.execute(new TransactionCallback<Boolean>() {
            @Override
            public Boolean doInTransaction(TransactionStatus status) {
                try {
                    String sql1 = "DELETE FROM notes WHERE id = ?";
                    jdbcTemplate.update(sql1, id);

                    String sql2 = "DELETE FROM NoteTopic WHERE noteId = ?";
                    jdbcTemplate.update(sql2, id);

                    String sql3 = "DELETE FROM UserNote WHERE noteId = ?";
                    jdbcTemplate.update(sql3, id);

                    return true;
                } catch (Exception e) {
                    return false;
                }
            }
        }));
    }

    // don't really need that
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
                    .lastModified((Number) note.get("lastModified"))
                    .topics(topicIds)
                    .content((String) note.get("content"))
                    .build();

            var content = new Delta().insert((String) note.get("content"));
        }
        return Optional.empty();
    }

}
