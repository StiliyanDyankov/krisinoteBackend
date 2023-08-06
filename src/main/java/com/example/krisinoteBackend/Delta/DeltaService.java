package com.example.krisinoteBackend.Delta;

import org.springframework.stereotype.Service;

@Service
public class DeltaService {
    public String getNewNoteContent(String prevContent, Delta delta) {
        Delta tempDelta = new Delta();
        tempDelta.insert(prevContent);
        System.out.println(delta.getOps());

        Delta newContent = tempDelta.compose(delta);
        return newContent.plainText();
    }
}
