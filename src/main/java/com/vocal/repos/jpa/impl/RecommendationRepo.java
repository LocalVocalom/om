package com.vocal.repos.jpa.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.vocal.viewmodel.RecommendationDto;

@Repository
public class RecommendationRepo {

	@PersistenceContext
    EntityManager em;
	
	public List<RecommendationDto> getNewsByViewsWithinTime(int recordsNum, int categoryId, int languageId, long newsId, Date createdTime) {
		List<RecommendationDto> dtos = em.createQuery(
				"SELECT new com.vocal.viewmodel.RecommendationDto(nw.newsHeadline, nw.newsImageUrl, nw.newsId, nw.newsVideoUrl, n.dateTime, nw.newsUrl) "
				+ "FROM News n, NewsDetails nw "
				+ "WHERE nw.newsId = n.newsId "
				+ "AND n.status = 'ACTIVE' "
				+ "AND n.newsId != :newsId "
				+ "AND n.catagoryId = :categoryId "
				+ "AND n.dateTime > :createdTime "
				+ "AND n.languageId = :languageId "
				+ "ORDER BY n.views DESC "
				, RecommendationDto.class
				)
				.setParameter("newsId", newsId)
				.setParameter("categoryId", categoryId)
				.setParameter("languageId", languageId)
				.setParameter("createdTime", createdTime)
				.setMaxResults(recordsNum)
				.getResultList();
		return dtos;
	}
	
	public List<RecommendationDto> getNewsByViews(int recordsNum, int categoryId, int languageId, long newsId) {
		List<RecommendationDto> dtos = em.createQuery(
				"SELECT new com.vocal.viewmodel.RecommendationDto(nw.newsHeadline, nw.newsImageUrl, nw.newsId, nw.newsVideoUrl, n.dateTime, nw.newsUrl) "
				+ "FROM News n, NewsDetails nw "
				+ "WHERE nw.newsId = n.newsId "
				+ "AND n.status = 'ACTIVE' "
				+ "AND n.newsId != :newsId "
				+ "AND n.catagoryId = :categoryId "
				+ "AND n.languageId = :languageId "
				+ "ORDER BY n.views DESC "
				, RecommendationDto.class
				)
				.setParameter("newsId", newsId)
				.setParameter("categoryId", categoryId)
				.setParameter("languageId", languageId)
				.setMaxResults(recordsNum)
				.getResultList();
		return dtos;
	}
	
	public List<RecommendationDto> getNewsByLikesWithinTime(int recordsNum, int categoryId, int languageId, long newsId, Date createdTime) {
		List<RecommendationDto> dtos = em.createQuery(
				"SELECT new com.vocal.viewmodel.RecommendationDto(nw.newsHeadline, nw.newsImageUrl, nw.newsId, nw.newsVideoUrl, n.dateTime, nw.newsUrl) "
				+ "FROM News n, NewsDetails nw "
				+ "WHERE nw.newsId = n.newsId "
				+ "AND n.status = 'ACTIVE' "
				+ "AND n.newsId != :newsId "
				+ "AND n.catagoryId = :categoryId "
				+ "AND n.dateTime > :createdTime "
				+ "AND n.languageId = :languageId "
				+ "ORDER BY n.likes DESC "
				, RecommendationDto.class
				)
				.setParameter("newsId", newsId)
				.setParameter("categoryId", categoryId)
				.setParameter("languageId", languageId)
				.setParameter("createdTime", createdTime)
				.setMaxResults(recordsNum)
				.getResultList();
		return dtos;
	}
	
	public List<RecommendationDto> getNewsByLikes(int recordsNum, int categoryId, int languageId, long newsId) {
		List<RecommendationDto> dtos = em.createQuery(
				"SELECT new com.vocal.viewmodel.RecommendationDto(nw.newsHeadline, nw.newsImageUrl, nw.newsId, nw.newsVideoUrl, n.dateTime, nw.newsUrl) "
				+ "FROM News n, NewsDetails nw "
				+ "WHERE nw.newsId = n.newsId "
				+ "AND n.status = 'ACTIVE' "
				+ "AND n.newsId != :newsId "
				+ "AND n.catagoryId = :categoryId "
				+ "AND n.languageId = :languageId "
				+ "ORDER BY n.likes DESC "
				, RecommendationDto.class
				)
				.setParameter("newsId", newsId)
				.setParameter("categoryId", categoryId)
				.setParameter("languageId", languageId)
				.setMaxResults(recordsNum)
				.getResultList();
		return dtos;
	}
	
	public List<RecommendationDto> getNewsByCommentWithinTime(int recordsNum, int categoryId, int languageId, long newsId, Date createdTime) {
		List<RecommendationDto> dtos = em.createQuery(
				"SELECT new com.vocal.viewmodel.RecommendationDto(nw.newsHeadline, nw.newsImageUrl, nw.newsId, nw.newsVideoUrl, n.dateTime, nw.newsUrl) "
				+ "FROM News n, NewsDetails nw "
				+ "WHERE nw.newsId = n.newsId "
				+ "AND n.status = 'ACTIVE' "
				+ "AND n.newsId != :newsId "
				+ "AND n.catagoryId = :categoryId "
				+ "AND n.dateTime > :createdTime "
				+ "AND n.languageId = :languageId "
				+ "ORDER BY nw.comment DESC "
				, RecommendationDto.class
				)
				.setParameter("newsId", newsId)
				.setParameter("categoryId", categoryId)
				.setParameter("languageId", languageId)
				.setParameter("createdTime", createdTime)
				.setMaxResults(recordsNum)
				.getResultList();
		return dtos;
	}
	
	public List<RecommendationDto> getNewsByComment(int recordsNum, int categoryId, int languageId, long newsId) {
		List<RecommendationDto> dtos = em.createQuery(
				"SELECT new com.vocal.viewmodel.RecommendationDto(nw.newsHeadline, nw.newsImageUrl, nw.newsId, nw.newsVideoUrl, n.dateTime, nw.newsUrl) "
				+ "FROM News n, NewsDetails nw "
				+ "WHERE nw.newsId = n.newsId "
				+ "AND n.status = 'ACTIVE' "
				+ "AND n.newsId != :newsId "
				+ "AND n.catagoryId = :categoryId "
				+ "AND n.languageId = :languageId "
				+ "ORDER BY nw.comment DESC "
				, RecommendationDto.class
				)
				.setParameter("newsId", newsId)
				.setParameter("categoryId", categoryId)
				.setParameter("languageId", languageId)
				.setMaxResults(recordsNum)
				.getResultList();
		return dtos;
	}
	
	public List<RecommendationDto> getNewsBySharesWithinTime(int recordsNum, int categoryId, int languageId, long newsId, Date createdTime) {
		List<RecommendationDto> dtos = em.createQuery(
				"SELECT new com.vocal.viewmodel.RecommendationDto(nw.newsHeadline, nw.newsImageUrl, nw.newsId, nw.newsVideoUrl, n.dateTime, nw.newsUrl) "
				+ "FROM News n, NewsDetails nw "
				+ "WHERE nw.newsId = n.newsId "
				+ "AND n.status = 'ACTIVE' "
				+ "AND n.newsId != :newsId "
				+ "AND n.catagoryId = :categoryId "
				+ "AND n.dateTime > :createdTime "
				+ "AND n.languageId = :languageId "
				+ "ORDER BY nw.share DESC "
				, RecommendationDto.class
				)
				.setParameter("newsId", newsId)
				.setParameter("categoryId", categoryId)
				.setParameter("languageId", languageId)
				.setParameter("createdTime", createdTime)
				.setMaxResults(recordsNum)
				.getResultList();
		return dtos;
	}
	
	public List<RecommendationDto> getNewsByShares(int recordsNum, int categoryId, int languageId, long newsId) {
		List<RecommendationDto> dtos = em.createQuery(
				"SELECT new com.vocal.viewmodel.RecommendationDto(nw.newsHeadline, nw.newsImageUrl, nw.newsId, nw.newsVideoUrl, n.dateTime, nw.newsUrl) "
				+ "FROM News n, NewsDetails nw "
				+ "WHERE nw.newsId = n.newsId "
				+ "AND n.status = 'ACTIVE' "
				+ "AND n.newsId != :newsId "
				+ "AND n.catagoryId = :categoryId "
				+ "AND n.languageId = :languageId "
				+ "ORDER BY nw.share DESC "
				, RecommendationDto.class
				)
				.setParameter("newsId", newsId)
				.setParameter("categoryId", categoryId)
				.setParameter("languageId", languageId)
				.setMaxResults(recordsNum)
				.getResultList();
		return dtos;
	}
	
	public List<RecommendationDto> getRecentlyUploadedNewsWithinTime(int recordsNum, int categoryId, int languageId, long newsId, Date createdTime) {
		List<RecommendationDto> dtos = em.createQuery(
				"SELECT new com.vocal.viewmodel.RecommendationDto(nw.newsHeadline, nw.newsImageUrl, nw.newsId, nw.newsVideoUrl, n.dateTime, nw.newsUrl) "
				+ "FROM News n, NewsDetails nw "
				+ "WHERE nw.newsId = n.newsId "
				+ "AND n.status = 'ACTIVE' "
				+ "AND n.newsId != :newsId "
				+ "AND n.catagoryId = :categoryId "
				+ "AND n.dateTime > :createdTime "
				+ "AND n.languageId = :languageId "
				+ "ORDER BY n.dateTime DESC "
				, RecommendationDto.class
				)
				.setParameter("newsId", newsId)
				.setParameter("categoryId", categoryId)
				.setParameter("languageId", languageId)
				.setParameter("createdTime", createdTime)
				.setMaxResults(recordsNum)
				.getResultList();
		return dtos;
	}
	
	public List<RecommendationDto> getRecentlyUploadedNews(int recordsNum, int categoryId, int languageId, long newsId) {
		List<RecommendationDto> dtos = em.createQuery(
				"SELECT new com.vocal.viewmodel.RecommendationDto(nw.newsHeadline, nw.newsImageUrl, nw.newsId, nw.newsVideoUrl, n.dateTime, nw.newsUrl) "
				+ "FROM News n, NewsDetails nw "
				+ "WHERE nw.newsId = n.newsId "
				+ "AND n.status = 'ACTIVE' "
				+ "AND n.newsId != :newsId "
				+ "AND n.catagoryId = :categoryId "
				+ "AND n.languageId = :languageId "
				+ "ORDER BY n.dateTime DESC "
				, RecommendationDto.class
				)
				.setParameter("newsId", newsId)
				.setParameter("categoryId", categoryId)
				.setParameter("languageId", languageId)
				.setMaxResults(recordsNum)
				.getResultList();
		return dtos;
	}
}
