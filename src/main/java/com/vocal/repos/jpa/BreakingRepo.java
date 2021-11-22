package com.vocal.repos.jpa;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vocal.entities.Breaking;

@Repository
public interface BreakingRepo extends JpaRepository<Breaking, Long> {

	//List<Breaking> findAllByCreatedTimeGreaterThanAndLanguageIdAndStatusByOrderByPriorityDescCreatedTimeDesc(Date createdTime, int languageId, int status);
	
	List<Breaking> findAllByLanguageIdAndStatusAndCreatedTimeGreaterThanOrderByPriorityDescCreatedTimeDesc(int languageId, int status, Date createdTime);

}
