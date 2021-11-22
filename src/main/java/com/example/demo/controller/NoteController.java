package com.example.demo.controller;

import com.example.demo.jwt.JwtTokenUtil;
import com.example.demo.model.Note;
import com.example.demo.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/notes")
public class NoteController {
    @Autowired
    NoteService noteService;
    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @PostMapping
    public Note addNote(@RequestBody String note, HttpServletRequest request) {
        final String tokenHeader = request.getHeader("Authorization");
        final String token = tokenHeader.replace("Bearer ", "");
        try{
            String username = jwtTokenUtil.getUsernameFromToken(token);
            Note newNote = new Note();
            newNote.setNote(note);
            newNote.setUsername(username);
            newNote.setStatus("A");
            return noteService.create(newNote);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new Note();
    }

    @GetMapping
    public List<Note> getAllNotesByUsername(HttpServletRequest request) {
        final String tokenHeader = request.getHeader("Authorization");
        final String token = tokenHeader.replace("Bearer ", "");
        try{
            String username = jwtTokenUtil.getUsernameFromToken(token);
            return noteService.getNotesByUsername(username);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @GetMapping("/{id}")
    public Note getNoteById(@PathVariable Long id, HttpServletRequest request) {
        final String tokenHeader = request.getHeader("Authorization");
        final String token = tokenHeader.replace("Bearer ", "");
        try{
            String username = jwtTokenUtil.getUsernameFromToken(token);
            Note note = noteService.getNoteById(id);
            if(note.getId() != null) {
                if(note.getUsername().equals(username)) {
                    if(note.getStatus().equals("A")) {
                        return note;
                    }else {
                        return null;
                    }
                }else if (noteService.isAdmin(username)) {
                    return note;
                }else {
                    return null;
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/all")
    public List<Note> getAllNotesByAdmin(HttpServletRequest request) {
        final String tokenHeader = request.getHeader("Authorization");
        final String token = tokenHeader.replace("Bearer ", "");
        try{
            String username = jwtTokenUtil.getUsernameFromToken(token);

            return noteService.getAllNotes(username);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @PutMapping("/{id}")
    public Note updateNote(@PathVariable Long id, @RequestBody String note, HttpServletRequest request) {
        final String tokenHeader = request.getHeader("Authorization");
        final String token = tokenHeader.replace("Bearer ", "");
        try{
            String username = jwtTokenUtil.getUsernameFromToken(token);
            return noteService.update(id, note, username);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public String deleteNote(@PathVariable Long id, HttpServletRequest request) {
        final String tokenHeader = request.getHeader("Authorization");
        final String token = tokenHeader.replace("Bearer ", "");
        try{
            String username = jwtTokenUtil.getUsernameFromToken(token);
            return noteService.deleteNote(id, username);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return "Could not delete";
    }
}
