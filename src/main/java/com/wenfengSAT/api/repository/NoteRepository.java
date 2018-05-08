package com.wenfengSAT.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wenfengSAT.api.model.Note;

public interface NoteRepository extends JpaRepository<Note, Long> {

}
