package com.example.demo.service;

import com.example.demo.model.Note;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.NoteRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@CacheConfig(cacheNames = {"note"})
public class NoteService {
    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private UserRepository userRepository;

    public Note create(Note note) {
        return noteRepository.save(note);
    }

    @CachePut(key = "#id")
    public Note update(Long id, String note, String username) {
        Note currentNote = noteRepository.findById(id).orElse(new Note());
        if(currentNote.getId() != null && (currentNote.getUsername().equals(username) || isAdmin(username))) {
            currentNote.setNote(note);
            return noteRepository.save(currentNote);
        }
        return currentNote;
    }

    public List<Note> getAllNotes(String username) {
        if(isAdmin(username)) {
            return noteRepository.findAll();
        }
        return new ArrayList<>();
    }

    public List<Note> getNotesByUsername(String username) {
        return noteRepository.findByUsernameAndStatus(username, "A");
    }

    @Cacheable(key = "#id")
    public Note getNoteById(Long id) {
        return noteRepository.findById(id).orElse(new Note());
    }

    @CacheEvict(key = "#id")
    public String deleteNote(Long id, String username) {
        Note note = noteRepository.findById(id).orElse(new Note());
        if(note.getId() != null) {
            if (note.getUsername().equals(username) || isAdmin(username)){
                note.setStatus("D");
                noteRepository.save(note);
                return "Note deleted";
            }else {
                return "Could not delete";
            }
        }
        return "Incorrect Note ID";
    }

    public boolean isAdmin(String username) {
        Optional<User> optionalUser = userRepository.findByUserName(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Optional<Role> role_admin = user.getRoles().stream()
                    .filter(role -> role.getName().equals("ROLE_ADMIN"))
                    .findFirst();
            return role_admin.isPresent();
        }else {
            return false;
        }
    }
}
