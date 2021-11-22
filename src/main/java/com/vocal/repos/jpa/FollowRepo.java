package com.vocal.repos.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vocal.entities.Follow;
import com.vocal.entities.Reporter;
import com.vocal.entities.UserProfile;

@Repository
public interface FollowRepo extends JpaRepository<Follow, Long> {
	
	boolean existsFollowByUserIdAndFollower(long userId, long follower);
	
	boolean existsFollowByUserIdAndFollowerAndFollowType(long userId, long follower, long followType);
	
	List<Follow> findAllByUserId(long userId);
	
	List<Follow> findAllByFollower(long follower);
	
	//int deleteByUserIdAndFollower(long userId, long follower);
	
	@Modifying
	@Query("delete from Follow f where f.userId=:userId and f.follower=:follower")
	int deleteByUserIdAndFollower(@Param("userId") long userId, @Param("follower") long follower); // works
	
	@Query("select p from UserProfile p, Follow f where f.userId=:userId  and f.follower=p.userId and f.followType=:followType")
	List<UserProfile> findAllUserFollowers(@Param("userId") long userId, @Param("followType") long followType); // works
	
	@Query("select p from UserProfile p, Follow f where f.follower=:follower and f.userId=p.userId and f.followType=:followType")
	List<UserProfile> findAllUserFollowing(@Param("follower") long userId, @Param("followType") long followType);
	
	@Query("select r from Reporter r, Follow f where f.follower=:follower and f.userId=r.id and f.followType=:followType")
	List<Reporter> findAllReporterFollowing(@Param("follower") long follower, @Param("followType") long followType);
	
	@Query("select p from UserProfile p, Follow f where f.userId=:id and f.follower=p.userId and f.followType=:followType")
	List<UserProfile> findAllReporterFollowers(@Param("id") long userId, @Param("followType") long followType); // unused

}
