package com.vocal.repos.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vocal.entities.HeaderTitle;

@Repository
public interface HeaderTitleRepo extends JpaRepository<HeaderTitle, Integer> {
	
	List<HeaderTitle> findAllByLanguageId(int languageId);
}
