package com.example.krisinoteBackend.sync;

import com.example.krisinoteBackend.config.JwtService;
import com.example.krisinoteBackend.note.NoteDAOImpl;
import com.example.krisinoteBackend.topic.TopicDAOImpl;
import com.example.krisinoteBackend.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class SyncService {

    private final NoteDAOImpl noteRepository;
    private final TopicDAOImpl topicRepository;
    private final JwtService jwtService;

    public ResponseEntity<ResourceSyncResponse> checkSyncDiffs(SyncRequest syncRequest) {

        // get the sync data from client
        System.out.println(syncRequest);
        Map<String, NoteSyncData> noteSyncDataClient = syncRequest.getNotes();
        Map<String, TopicSyncData> topicSyncDataClient = syncRequest.getTopics();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = (User) authentication.getPrincipal();
        Number userId = user.getUserId();

        // get the sync data from db
        Map<String, NoteSyncData> noteSyncDataDb = noteRepository.getSyncData(userId);
        Map<String, TopicSyncData> topicSyncDataDb = topicRepository.getSyncData(userId);

        // calc diff

        return ResponseEntity.ok(ResourceSyncResponse.builder().build());
    }
}
