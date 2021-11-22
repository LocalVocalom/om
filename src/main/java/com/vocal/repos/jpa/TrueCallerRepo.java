package com.vocal.repos.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vocal.entities.TrueCaller;

@Repository
public interface TrueCallerRepo extends JpaRepository<TrueCaller, Long>{
	
}
