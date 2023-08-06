package com.example.krisinoteBackend.webClip;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebClip {
    private String id;
    private String webClipName;
    private List<String> topics;
    private String description;
    private Number createdAt;
    private Number lastModified;
    private String content;
    private String fromUrl;
}
