package com.example.demo.service;

import com.example.demo.model.Note;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.NoteRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NoteService {
    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private UserRepository userRepository;

    public Note create(Note note) {
        return noteRepository.save(note);
    }

    public Note update(Long id, String note, String username) {
        Note currentNote = noteRepository.findById(id).orElse(new Note());
        if(currentNote.getId() != null && currentNote.getUsername().equals(username)) {
            currentNote.setNote(note);
            return noteRepository.save(currentNote);
        }
        return currentNote;
    }

    public List<Note> getAllNotes(String username) {
        Optional<User> optionalUser = userRepository.findByUserName(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Optional<Role> role_admin = user.getRoles().stream()
                    .filter(role -> role.getName().equals("ROLE_ADMIN"))
                    .findFirst();
            if (!role_admin.isEmpty()) {
                return noteRepository.findAll();
            }
        }
        return new ArrayList<>();
    }

    public List<Note> getNotesByUsername(String username) {
        return noteRepository.findByUsername(username);
    }

    public List<Note> getNotesByStatus(String status) {
        return noteRepository.findByStatus(status);
    }

    public String deleteNote(Long id, String usename) {
        Note note = noteRepository.findById(id).orElse(new Note());
        if(note.getId() != null) {
            if (note.getUsername().equals(usename)){
                noteRepository.delete(note);
                return "Note deleted";
            }else {
                return "Could not delete";
            }
        }
        return "Incorrect Note ID";
    }
}
