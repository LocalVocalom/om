package com.vocal.repos.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vocal.entities.Comment;

@Repository("commentRepo")
public interface CommentRepo extends JpaRepository<Comment, Integer> {

}
