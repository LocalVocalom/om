package com.vocal.repos.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.vocal.entities.UserLocation;

@Repository
public interface UserLocationRepo extends JpaRepository<UserLocation, Integer> {
	
	UserLocation findUserLocationByUserId(long userId);
	
	//@Query(value = "select date(date_time) , count(1) from user_location where date_time>( CURDATE() - INTERVAL :n DAY ) and  state='Uttar Pradesh' and city='Ghaziabad' group by date(date_time)", nativeQuery = true)
	@Query(value = "select date(DATE_ADD(date_time,INTERVAL 19800 SECOND)) , count(1) from user_location where date_time>(DATE_ADD(CURDATE(),INTERVAL 19800 SECOND) - INTERVAL :n DAY) and  state='Uttar Pradesh' AND city in :exceptionalCities group by date(DATE_ADD(date_time,INTERVAL 19800 SECOND))", nativeQuery = true)
	List<Object[]> countUserLocationByDateTimeAndCity(int n, List<String> exceptionalCities);
	
}
