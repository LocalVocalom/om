package com.vocal.repos.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vocal.entities.UserAccountSummary;

@Repository
public interface UserAccountSummaryRepo extends JpaRepository<UserAccountSummary, Long> {

	List<UserAccountSummary> findAllByUserIdOrderByCreatedTimeDesc(long userId);
		
	List<UserAccountSummary> findTop10ByUserIdOrderByCreatedTimeDesc(long userId);
	
}
