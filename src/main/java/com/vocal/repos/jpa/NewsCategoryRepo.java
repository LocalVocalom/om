package com.vocal.repos.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vocal.entities.NewsCategory;

@Repository
public interface NewsCategoryRepo extends JpaRepository<NewsCategory, Integer>{
	
	List<NewsCategory> findByLanguageId(int languageId);
	
	List<NewsCategory> findByLanguageIdOrderByPriorityAsc(int languageId);
	
	List<NewsCategory> findByLanguageIdAndStatusOrderByPriorityAsc(int languageId, int status);
	
	List<NewsCategory> findAllByLanguageIdAndStatusOrderByPriorityAsc(int languageId, int status);
	
	List<NewsCategory> findAllByStatusOrderByPriorityAsc(int status);
	
	NewsCategory findByCategoryName(String categoryName);
	
	NewsCategory findByCategoryNameAndStatus(String categoryName, int status);
	
}
