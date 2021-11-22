package com.vocal.repos.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vocal.entities.UserAccount;

@Repository
public interface UserAccountRepo extends JpaRepository<UserAccount, Long> {
	
	UserAccount findByUserId(long userId);
	
}
