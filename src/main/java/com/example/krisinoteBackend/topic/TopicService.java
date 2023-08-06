package com.example.krisinoteBackend.topic;


import com.example.krisinoteBackend.note.Note;
import com.example.krisinoteBackend.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TopicService {
    @Autowired
    private TopicDAOImpl topicRepository;

    public ResponseEntity createTopic(Topic topic) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = (User) authentication.getPrincipal();
        Number userId = user.getUserId();

        Map<String, String> body = new HashMap<String, String>();
        boolean success = topicRepository.save(topic);

        if(success){
            body.put("message", "Successful");
            return ResponseEntity.ok(body);
        } else {
            body.put("message", "Unsuccessful");
            return ResponseEntity.status(400).body(body);
        }
    }

    public ResponseEntity updateTopic(Topic topic) {
        Map<String, String> body = new HashMap<String, String>();
        boolean success = topicRepository.update(topic);

        if(success){
            body.put("message", "Successful");
            return ResponseEntity.ok(body);
        } else {
            body.put("message", "Unsuccessful");
            return ResponseEntity.status(400).body(body);
        }
    }

    public ResponseEntity deleteTopic(String id) {
        Map<String, String> body = new HashMap<String, String>();
        boolean success = topicRepository.delete(id);

        if(success){
            body.put("message", "Successful");
            return ResponseEntity.ok(body);
        } else {
            body.put("message", "Unsuccessful");
            return ResponseEntity.status(400).body(body);
        }
    }
}
