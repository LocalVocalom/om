package com.vocal.repos.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vocal.entities.UserRedeem;

@Repository
public interface UserRedeemRepo extends JpaRepository<UserRedeem, Long> {
	
	UserRedeem findFirstByUserIdAndStatusOrderByCreatedTimeDesc(Long userId, String status);
	
	UserRedeem findFirstByUserIdOrderByUpdatedTimeDesc(Long userId);
	
	boolean existsUserRedeemByUserId(long userId);
	
}
