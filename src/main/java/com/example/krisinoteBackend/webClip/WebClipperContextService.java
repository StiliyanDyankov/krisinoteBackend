package com.example.krisinoteBackend.webClip;

import com.example.krisinoteBackend.user.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WebClipperContextService {
    private ConcurrentHashMap<Number, List<String>> contextHolder = new ConcurrentHashMap<>();

    public boolean toRecieveWebClipContent(List<String> webClipIds) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = (User) authentication.getPrincipal();
        Number userId = user.getUserId();

        return (contextHolder.get(userId) != null);
    }

    public List<String> claimWebClipContent() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = (User) authentication.getPrincipal();
        Number userId = user.getUserId();

        List<String> webClipIds = contextHolder.get(userId);
        if((webClipIds != null) && !webClipIds.isEmpty()) {
            contextHolder.remove(userId);
            return webClipIds;
        }
        else return new ArrayList<String>();
    }

    public void publishWebClipContent(String webClipId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = (User) authentication.getPrincipal();
        Number userId = user.getUserId();

        if(contextHolder.get(userId) != null) {
            contextHolder.get(userId).add(webClipId);
        } else {
            List<String> webClipIds = new ArrayList<String>();
            webClipIds.add(webClipId);
            contextHolder.put(userId, webClipIds);
        }

        System.out.println(userId);
        System.out.println("runs publish");
        System.out.println(contextHolder);
    }
}
