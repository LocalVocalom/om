package com.vocal.repos.jpa;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vocal.entities.SponsorContent;

@Repository
public interface SponsorContentRepo extends JpaRepository<SponsorContent, Long> {
	
	List<SponsorContent> findAllByEnabledAndStartTimeLessThanAndEndTimeGreaterThan(boolean enabled, Date startTime, Date endTime);
	
}
