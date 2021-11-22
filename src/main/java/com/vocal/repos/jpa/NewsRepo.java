package com.vocal.repos.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vocal.entities.News;

public interface NewsRepo extends JpaRepository<News, Long> {
	
	List<News> findByUniqueId(String uniqueId);
	
	News findByNewsId(long newsId);
		
	News findTopByLanguageIdOrderByNewsIdDesc(int languageId);
	
}
