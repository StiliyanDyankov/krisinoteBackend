package com.example.krisinoteBackend.note;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/app/note")
@RequiredArgsConstructor
@CrossOrigin
public class NoteController {

    @Autowired
    private NoteService noteService;

    @PostMapping
    public ResponseEntity createNote(@RequestBody Note note) {
        return noteService.createNote(note);
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity updateNote(
            @PathVariable("id") String id,
            @RequestBody Note note,
            @RequestParam("field") String field
    ) {
        return noteService.updateNote(id, note, field);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity deleteNote(@PathVariable("id") String id) {
        return noteService.deleteNote(id);
    }
}
