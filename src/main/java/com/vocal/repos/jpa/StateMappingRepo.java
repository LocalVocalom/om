package com.vocal.repos.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vocal.entities.StateMapping;

public interface StateMappingRepo extends JpaRepository<StateMapping, Long> {
	
	List<StateMapping> findByAlternateNamesContaining(String alternateName);
	
	/*
	List<StateMapping> findByTitleContaining(String title);
	List<StateMapping> findByTitleContains(String title);
	List<StateMapping> findByTitleIsContaining(String title);
	*/
	
	/*
	 * List<Movie> findByTitleLike(String title); it should be called as movieRepository.findByTitleLike("%in%");
	 */
	// StartsWith, EndsWith, IgnoreCase, NotContains, NotContaining, and NotLike are other keywords
	
}
