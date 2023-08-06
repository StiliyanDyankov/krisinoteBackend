package com.example.krisinoteBackend.topic;

import com.example.krisinoteBackend.note.Note;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/app/topic")
@RequiredArgsConstructor
@CrossOrigin
public class TopicController {

    @Autowired
    private TopicService topicService;

    @PostMapping
    public ResponseEntity createTopic(@RequestBody Topic topic) {
        return topicService.createTopic(topic);
    }

    @PatchMapping
    public ResponseEntity updateTopic(@RequestBody Topic topic) {
        return topicService.updateTopic(topic);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity deleteTopic(@PathVariable("id") String id) {
        return topicService.deleteTopic(id);
    }
}
