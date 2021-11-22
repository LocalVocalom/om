package com.vocal.repos.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vocal.entities.NewsDetails;

public interface NewsDetailsRepo extends JpaRepository<NewsDetails, Long> {
	
	NewsDetails findFirstByOrderByNewsIdDesc();
	
	NewsDetails findByNewsId(long newsId);
	
}
