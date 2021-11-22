package com.example.demo.repository;

import com.example.demo.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    public List<Note> findByUsername(String username);
    public List<Note> findByStatus(String status);
    public List<Note> findByUsernameAndStatus(String username, String status);
}
