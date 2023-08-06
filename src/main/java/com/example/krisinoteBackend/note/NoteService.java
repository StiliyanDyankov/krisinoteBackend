package com.example.krisinoteBackend.note;

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
public class NoteService {

    @Autowired
    private NoteDAOImpl noteRepository;

    public ResponseEntity createNote(Note note) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = (User) authentication.getPrincipal();
        Number userId = user.getUserId();

        System.out.println(user);
        System.out.println(userId);

        Map<String, String> body = new HashMap<String, String>();
        boolean success = noteRepository.save(userId, note);

        if(success){
            body.put("message", "Successful");
            return ResponseEntity.ok(body);
        } else {
            body.put("message", "Unsuccessful");
            return ResponseEntity.status(400).body(body);
        }
    }

    public ResponseEntity updateNote(String id, Note note, String field) {
        System.out.println(id);
        System.out.println(note);
        System.out.println(field);

        Map<String, String> body = new HashMap<String, String>();

        if (field.isEmpty()) return ResponseEntity.badRequest().build();
        else if (field.equals("metadata")) {
            boolean success = noteRepository.updateMetadata(note);
            if(success){
                body.put("message", "Successful");
                return ResponseEntity.ok(body);
            } else {
                body.put("message", "Unsuccessful");
                return ResponseEntity.status(400).body(body);
            }

        } else if (field.equals("content")) {
            boolean success = noteRepository.updateContent(note.getId(), note.getContent());
            if(success){
                body.put("message", "Successful");
                return ResponseEntity.ok(body);
            } else {
                body.put("message", "Unsuccessful");
                return ResponseEntity.status(400).body(body);
            }
        }

        body.put("message", "Unsuccessful");
        return ResponseEntity.status(400).body(body);
    }

    public ResponseEntity deleteNote(String id) {
        Map<String, String> body = new HashMap<String, String>();

        boolean success = noteRepository.delete(id);
        if(success){
            body.put("message", "Successful");
            return ResponseEntity.ok(body);
        } else {
            body.put("message", "Unsuccessful");
            return ResponseEntity.status(400).body(body);
        }
    }

}
