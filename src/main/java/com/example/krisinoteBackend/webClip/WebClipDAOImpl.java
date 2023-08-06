package com.example.krisinoteBackend.webClip;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class WebClipDAOImpl implements WebClipDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private PlatformTransactionManager transactionManager;

    // create the transaction template
    @Autowired
    private TransactionTemplate transactionTemplate;
    @Override
    public boolean save(Number userId, WebClip webClip) {
        return Boolean.TRUE.equals(transactionTemplate.execute(new TransactionCallback<Boolean>() {
            @Override
            public Boolean doInTransaction(TransactionStatus status) {
                try {
                    String sql1 = "INSERT INTO webClips (id, noteName, description, createdAt, lastModified, content, fromUrl) VALUES (?, ?, ?, ?, ?, ?, ?)";
                    jdbcTemplate.update(sql1, webClip.getId(), webClip.getWebClipName(), webClip.getDescription(), webClip.getCreatedAt(), webClip.getLastModified(), webClip.getContent(), webClip.getFromUrl());

                    String sql2 = "INSERT INTO WebClipTopic (webClipId, topicId) VALUES (?, ?)";
                    for (String topic : webClip.getTopics()) {
                        jdbcTemplate.update(sql2, webClip.getId(), topic);
                    }

                    String sql3 = "INSERT INTO UserWebClip (userId, webClipId) VALUES (?, ?)";
                    jdbcTemplate.update(sql3, userId, webClip.getId());

                    return true;
                } catch (Exception e) {
                    System.out.println(e);
                    return false;
                }
            }
        }));
    }

    @Override
    public boolean updateMetadata(WebClip webClip) {
        return Boolean.TRUE.equals(transactionTemplate.execute(new TransactionCallback<Boolean>() {
            @Override
            public Boolean doInTransaction(TransactionStatus status) {
                try {
                    String sql = "UPDATE webClips SET noteName = ?, description = ?, createdAt = ?, lastModified = ?, fromUrl = ? WHERE id = ?";
                    jdbcTemplate.update(sql, webClip.getWebClipName(), webClip.getDescription(), webClip.getCreatedAt(), webClip.getLastModified(), webClip.getFromUrl(), webClip.getId());

                    String sql2 = "DELETE FROM WebClipTopic WHERE webClipId = ?";
                    jdbcTemplate.update(sql2, webClip.getId());

                    String sql3 = "INSERT INTO WebClipTopic (webClipId, topicId) VALUES (?, ?)";
                    for (String topic : webClip.getTopics()) {
                        jdbcTemplate.update(sql3, webClip.getId(), topic);
                    }

                    return true;
                } catch (Exception e) {
                    return false;
                }
            }
        }));
    }

    @Override
    public boolean delete(String id) {
        return Boolean.TRUE.equals(transactionTemplate.execute(new TransactionCallback<Boolean>() {
            @Override
            public Boolean doInTransaction(TransactionStatus status) {
                try {
                    // Delete from UserWebClip table
                    String sql1 = "DELETE FROM UserWebClip WHERE webClipId = ?";
                    jdbcTemplate.update(sql1, id);

// Delete from WebClipTopic table
                    String sql2 = "DELETE FROM WebClipTopic WHERE webClipId = ?";
                    jdbcTemplate.update(sql2, id);

// Delete from webClips table
                    String sql3 = "DELETE FROM webClips WHERE id = ?";
                    jdbcTemplate.update(sql3, id);

                    return true;
                } catch (Exception e) {
                    return false;
                }
            }
        }));
    }

    @Override
    public List<WebClip> getMissingWebClips(Number userId, List<String> hasIds) {
        StringBuilder sql = new StringBuilder("SELECT * FROM UserWebClip uwc JOIN webClips wc ON uwc.webClipId = wc.id WHERE uwc.userId = ? AND uwc.webClipId NOT IN (");
        for (int i = 0; i < hasIds.size(); i++) {
            sql.append("?");
            if (i < hasIds.size() - 1) {
                sql.append(", ");
            }
        }
        sql.append(")");

// Create an array of parameters for the query
        Object[] params = new Object[hasIds.size() + 1];
        params[0] = userId;
        for (int i = 0; i < hasIds.size(); i++) {
            params[i + 1] = hasIds.get(i);
        }

// Execute the query and return a list of WebClip objects
        List<WebClip> result = jdbcTemplate.query(sql.toString(), params, new RowMapper<WebClip>() {
            @Override
            public WebClip mapRow(ResultSet rs, int rowNum) throws SQLException {
                WebClip webClip = new WebClip();
                webClip.setId(rs.getString("id"));
                webClip.setWebClipName(rs.getString("noteName"));
                webClip.setDescription(rs.getString("description"));
                webClip.setCreatedAt(rs.getLong("createdAt"));
                webClip.setLastModified(rs.getLong("lastModified"));
                webClip.setContent(rs.getString("content"));
                webClip.setFromUrl(rs.getString("fromUrl"));

                // Query the WebClipTopic table to get the topics for this web clip
                String sql2 = "SELECT topicId FROM WebClipTopic WHERE webClipId = ?";
                List<String> topics = jdbcTemplate.queryForList(sql2, String.class, webClip.getId());
                webClip.setTopics(topics);

                return webClip;
            }
        });
        return result;
    }

    @Override
    public List<WebClip> getAllWebClips(Number userId) {
        String sql = "SELECT * FROM UserWebClip uwc JOIN webClips wc ON uwc.webClipId = wc.id WHERE uwc.userId = ?";

// Execute the query and return a list of WebClip objects
        List<WebClip> result = jdbcTemplate.query(sql, new Object[]{userId}, new RowMapper<WebClip>() {
            @Override
            public WebClip mapRow(ResultSet rs, int rowNum) throws SQLException {
                WebClip webClip = new WebClip();
                webClip.setId(rs.getString("id"));
                webClip.setWebClipName(rs.getString("noteName"));
                webClip.setDescription(rs.getString("description"));
                webClip.setCreatedAt(rs.getLong("createdAt"));
                webClip.setLastModified(rs.getLong("lastModified"));
                webClip.setContent(rs.getString("content"));
                webClip.setFromUrl(rs.getString("fromUrl"));

                // Query the WebClipTopic table to get the topics for this web clip
                String sql2 = "SELECT topicId FROM WebClipTopic WHERE webClipId = ?";
                List<String> topics = jdbcTemplate.queryForList(sql2, String.class, webClip.getId());
                webClip.setTopics(topics);

                return webClip;
            }
        });
        return result;

    }
}
