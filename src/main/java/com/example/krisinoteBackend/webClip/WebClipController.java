package com.example.krisinoteBackend.webClip;

import com.example.krisinoteBackend.note.Note;
import com.example.krisinoteBackend.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/app/webClip")
@RequiredArgsConstructor
@CrossOrigin
public class WebClipController {

    @Autowired
    private WebClipService webClipService;

    // for clipper only ----------------------------
    @PostMapping
    public ResponseEntity createWebClip(@RequestBody WebClip webClip) {
        return webClipService.createWebClip(webClip);
    }

    // for web app only ----------------------------
    @PatchMapping
    public ResponseEntity updateWebClip(
            @RequestBody WebClip webClip
    ) {
        return webClipService.updateWebClip(webClip);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity deleteWebClip(
            @PathVariable("id") String id
    ) {
        return webClipService.deleteWebClip(id);
    }

    @PostMapping("/missing")
    public ResponseEntity getMissingWebClips(@RequestBody List<String> hasIds) {
        return webClipService.getMissingWebClips(hasIds);
    }

    // for long polling
    @GetMapping("/new")
    public ResponseEntity getNewWebClips() {
        return webClipService.getNewWebClips();
    }
}
