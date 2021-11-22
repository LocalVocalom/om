package com.vocal.repos.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vocal.entities.ReportReasons;

@Repository
public interface ReportReasonsRepo extends JpaRepository<ReportReasons, Integer> {

	
}
