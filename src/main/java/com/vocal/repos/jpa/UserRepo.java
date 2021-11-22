package com.vocal.repos.jpa;


import org.springframework.data.jpa.repository.JpaRepository;

import com.vocal.entities.User;

public interface UserRepo extends JpaRepository<User, Long>{
	
	User findByEmailId(String email);

	//@Query
	User findByUserId(long userId);
	
	User findByMobile(String mobile);
	
}
