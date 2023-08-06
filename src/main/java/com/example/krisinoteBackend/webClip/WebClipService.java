package com.example.krisinoteBackend.webClip;

import com.example.krisinoteBackend.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WebClipService {
    @Autowired
    private WebClipDAO webClipRepository;

    @Autowired
    private WebClipperContextService contextService;

    public ResponseEntity createWebClip(WebClip webClip) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = (User) authentication.getPrincipal();
        Number userId = user.getUserId();

        Map<String, String> body = new HashMap<String, String>();
        boolean success = webClipRepository.save(userId, webClip);

        if(success){
            contextService.publishWebClipContent(webClip.getId());
            body.put("message", "Successful");
            return ResponseEntity.ok(body);
        } else {
            body.put("message", "Unsuccessful");
            return ResponseEntity.status(400).body(body);
        }
    }

    public ResponseEntity updateWebClip(WebClip webClip) {

        Map<String, String> body = new HashMap<String, String>();
        boolean success = webClipRepository.updateMetadata(webClip);

        if(success){
            body.put("message", "Successful");
            return ResponseEntity.ok(body);
        } else {
            body.put("message", "Unsuccessful");
            return ResponseEntity.status(400).body(body);
        }
    }

    public ResponseEntity deleteWebClip(String id) {
        Map<String, String> body = new HashMap<String, String>();

        boolean success = webClipRepository.delete(id);
        if(success){
            body.put("message", "Successful");
            return ResponseEntity.ok(body);
        } else {
            body.put("message", "Unsuccessful");
            return ResponseEntity.status(400).body(body);
        }
    }

    public ResponseEntity<List<WebClip>> getMissingWebClips(List<String> hasIds) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = (User) authentication.getPrincipal();
        Number userId = user.getUserId();

        List<WebClip> missingWebClips = new ArrayList<>();

        if(hasIds.isEmpty()) {
            missingWebClips = webClipRepository.getAllWebClips(userId);
        } else {
            missingWebClips = webClipRepository.getMissingWebClips(userId, hasIds);
        }


        return ResponseEntity.status(200).body(missingWebClips);
    }

    // for long polling
    public ResponseEntity getNewWebClips() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = (User) authentication.getPrincipal();
        Number userId = user.getUserId();

        return ResponseEntity.ok().build();
    }
}
