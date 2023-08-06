package com.example.krisinoteBackend.sync;

import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/app/resource")
public class SyncController {

    @Autowired
    private SyncService syncService;

    @PostMapping
    public ResponseEntity<ResourceSyncResponse> checkSyncDiffs(
            @RequestBody SyncRequest syncRequest,
            @RequestHeader("Authorization") String token
            ) {
        return syncService.checkSyncDiffs(syncRequest);
    }

}
