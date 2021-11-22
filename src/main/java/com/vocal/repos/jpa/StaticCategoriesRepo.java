package com.vocal.repos.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vocal.entities.StaticCategories;

@Repository
public interface StaticCategoriesRepo extends JpaRepository<StaticCategories, Integer> {
	
}
