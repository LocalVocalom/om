package com.vocal.repos.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vocal.entities.MoreNews;

public interface MoreNewsRepo extends JpaRepository<MoreNews, Integer> {
	
	List<MoreNews> findByLanguageId(int languageId);
}
