package com.vocal.repos.jpa.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.vocal.viewmodel.NewsDtoInnerAll;
import com.vocal.viewmodel.NewsDtoInnerUser;
import com.vocal.viewmodel.PersonalizedDto;

@Repository
@SuppressWarnings("unchecked")
public class CustomizedRepo {
		
	@PersistenceContext
    EntityManager em;

	public List<NewsDtoInnerAll> getNewsForAll(long lastNewsId, long category, long langID, /*Date datetime,*/ int count) {
		Query q =  em.createNamedQuery("getNewsForAllQuery");
		q.setParameter("category", category);
		q.setParameter("langID", langID);
		//q.setParameter("datetime", datetime);
		q.setParameter("count", count);
		q.setParameter("lastNewsId", lastNewsId);
				
		List<NewsDtoInnerAll> properDtos = q.getResultList();
		return properDtos;
	}
	
	
	public List<NewsDtoInnerUser> getNewsForUser(long lastNewsId, long category, long userid, /*Date datetime,*/ int count) {
		Query q = em.createNamedQuery("getNewsForUserQuery");
		q.setParameter("category", category);
		q.setParameter("userid", userid);
		//q.setParameter("datetime", datetime);
		q.setParameter("count", count);
		q.setParameter("lastNewsId", lastNewsId);
		
		List<NewsDtoInnerUser> properDtos = q.getResultList();
		return properDtos;
	}
	
	public List<NewsDtoInnerAll> getLocalizedNewsForAll(int catagory, int langID, int count,
			double userLatitude, double userLongitude, double targetedDistance) {
		Query q = em.createNamedQuery("localizedNewsQuery");
		q.setParameter("userLatitude", userLatitude);
		q.setParameter("userLongitude", userLongitude);
		q.setParameter("catagory", catagory);
		q.setParameter("langID", langID);
		q.setParameter("targetedDistance", targetedDistance);
		q.setParameter("count", count);

		List<NewsDtoInnerAll> properDtos = q.getResultList();
		return properDtos;
	}
	
	public List<NewsDtoInnerAll> getLocalizedNewsForAllV1(int catagory, int langID, int count,
			double userLatitude, double userLongitude, double targetedDistance, double boundedDistance) {
		Query q = em.createNamedQuery("localizedNewsQueryV1");
		q.setParameter("userLatitude", userLatitude);
		q.setParameter("userLongitude", userLongitude);
		q.setParameter("catagory", catagory);
		q.setParameter("langID", langID);
		q.setParameter("targetedDistance", targetedDistance);
		q.setParameter("boundedDistance", boundedDistance);
		q.setParameter("count", count);
		
		List<NewsDtoInnerAll> properDtos = q.getResultList();
		return properDtos;
	}
	
	public double getDistance(double userLatitude, double userLongitude, long newsId) {
		Query q = em.createNamedQuery("distanceQuery");
		q.setParameter("userLatitude", userLatitude);
		q.setParameter("userLongitude", userLongitude);
		q.setParameter("newsID", newsId);
		
		
		double distance =  (double) q.getSingleResult();
		return distance;
	}
	
	public List<NewsDtoInnerAll> getLocalizedNewsForAllV2(long lastNewsId, int catagory, int langID, int count,
			double userLatitude, double userLongitude, double targetedDistance, double boundedDistance/*, Date datetime*/) {
		Query q = em.createNamedQuery("localizedNewsQueryV2");
		q.setParameter("userLatitude", userLatitude);
		q.setParameter("userLongitude", userLongitude);
		q.setParameter("catagory", catagory);
		q.setParameter("langID", langID);
		q.setParameter("targetedDistance", targetedDistance);
		q.setParameter("boundedDistance", boundedDistance);
		//q.setParameter("datetime", datetime);
		q.setParameter("lastNewsId", lastNewsId);
		q.setParameter("count", count);

		List<NewsDtoInnerAll> properDtos = q.getResultList();
		return properDtos;
	}
	
	public double getFlooredDistance(double userLatitude, double userLongitude, long newsId) {
		Query q = em.createNamedQuery("flooredDistanceQuery");
		q.setParameter("userLatitude", userLatitude);
		q.setParameter("userLongitude", userLongitude);
		q.setParameter("newsID", newsId);
				
		double flooredDistance =   (double) q.getSingleResult();
		return flooredDistance;
	}
	
	public List<NewsDtoInnerAll> getNewsForAllByReporter(long lastNewsId, long creatorId, boolean isReporter, int langId, /*Date datetime,*/ int count) {
		
		Query q;// = em.createNamedQuery("getNewsForAllByReporter");
		if(isReporter) {
			q = em.createNamedQuery("getNewsForAllByReporterByLanguage");
			q.setParameter("langID", langId);
		} else {
			q = em.createNamedQuery("getNewsForAllByReporter");
		}
		q.setParameter("creatorId", creatorId);
		q.setParameter("isReporter", isReporter);
		//q.setParameter("datetime", datetime);
		q.setParameter("count", count);
		q.setParameter("lastNewsId", lastNewsId);
		
		List<NewsDtoInnerAll> properDtos = q.getResultList();
		return properDtos;
	}
	
	public List<PersonalizedDto> getNewsForAllByCategoreis(List<Integer> categories, int langId, int count) {
		Query q = em.createNamedQuery("getNewsForAllByCategories");
		q.setParameter("category", categories);
		q.setParameter("langID", langId);
		q.setParameter("count", count);
		List<PersonalizedDto> properDtos = q.getResultList();
		return properDtos;
	}
	
	public List<NewsDtoInnerAll> getLocalizedNewsUptoPreviousNDays(int langID, int catagory, long lastNewsId, int count, double userLatitude, double userLongitude, double targetedDistance, double boundedDistance, Date datetime) {
		Query q = em.createNamedQuery("localizedNewsQueryV2GreaterThanDate");
		q.setParameter("userLatitude", userLatitude);
		q.setParameter("userLongitude", userLongitude);
		q.setParameter("catagory", catagory);
		q.setParameter("langID", langID);
		q.setParameter("targetedDistance", targetedDistance);
		q.setParameter("boundedDistance", boundedDistance);
		q.setParameter("datetime", datetime);
		q.setParameter("lastNewsId", lastNewsId);
		q.setParameter("count", count);
		
		List<NewsDtoInnerAll> properDtos = q.getResultList();
		return properDtos;
	}
	
}
