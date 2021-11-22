package com.vocal.repos.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vocal.entities.UserBlackList;

@Repository
public interface UserBlackListRepo extends JpaRepository<UserBlackList, Long> {
	
	UserBlackList findByUserId(long userId);
	
	boolean existsUserBlackListByUserId(long userId);

}
