package com.vocal.repos.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vocal.entities.HeaderContent;

@Repository
public interface HeaderContentRepo extends JpaRepository<HeaderContent, Integer> {

	List<HeaderContent> findAllByHeaderTitle(String headerTitle);
	
}
