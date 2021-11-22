package com.vocal.services.impl;

import java.util.Date;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vocal.entities.EDR;
import com.vocal.entities.News;
import com.vocal.entities.NewsDetails;
import com.vocal.exceptions.RecordNotFoundException;
import com.vocal.repos.jpa.EDRRepo;
import com.vocal.repos.jpa.NewsDetailsRepo;
import com.vocal.repos.jpa.NewsRepo;
import com.vocal.services.EdrService;
import com.vocal.viewmodel.StatusDto;

@Service
public class EdrServiceImpl implements EdrService {

	@Autowired
	private NewsRepo newsRepo;
	
	@Autowired
	private NewsDetailsRepo newsDetailsRepo;
	
	@Autowired
	private EDRRepo edrRepo;
	
	private static final  Logger LOGGER = LoggerFactory.getLogger(EdrServiceImpl.class);
	
	// type 1 is for views counter and type 2 is for likes counter
	public boolean handleViewsAndLikesEdr(long userId, long newsId, int type) {
		Optional<News> optional = newsRepo.findById(newsId);
		boolean hasSucceeded = false;
		if(optional.isPresent()) {
			News news = optional.get();
			switch (type) {
			case 1:
				news.setViews(news.getViews() + 1);
				newsRepo.save(news);	
				hasSucceeded = true;
				LOGGER.debug("incremented news views for newsId={}", newsId);
				break;
			case 2:
				news.setLikes(news.getLikes() + 1);
				newsRepo.save(news);	
				hasSucceeded = true;
				LOGGER.debug("incremented news likes for newsId={}", newsId);
				break;
			}
		} else {
			LOGGER.error("The NEWS record with newsId = {} doesn't exist", newsId);
		}
		return hasSucceeded;
	}
	
	// type 3 is for shares counter, type 4 is for dislikes counter and type 5 is for flag counter
	public boolean handleShareDislikesAndFlagsEdr(long userId, long newsId, int type) {
		Optional<NewsDetails> optional = newsDetailsRepo.findById(newsId);
		boolean hasSucceeded = false;
		/*
		if(optional.isPresent()) {
			NewsDetails news = optional.get();
			switch (type) {
			case 1:
				news.setViews(news.getViews() + 1);
				newsRepo.save(news);	
				hasSucceeded = true;
				LOGGER.debug("incremented news views for newsId={}", newsId);
				break;
			case 2:
				news.setLikes(news.getLikes() + 1);
				newsRepo.save(news);	
				hasSucceeded = true;
				LOGGER.debug("incremented news likes for newsId={}", newsId);
				break;
			}
		} else {
			LOGGER.error("The NEWS record with newsId = {} doesn't exist", newsId);
		}
		*/
		return hasSucceeded;
	}
	
	
	// TODO : Optimization with in-memory(for some constant time) and persist in batches 
	@Override
	public StatusDto handleEdr(long userId, long newsId, int type, String reportId) {
		if(!newsRepo.existsById(newsId)) {
			throw new RecordNotFoundException("Illegal EDR, news with newsId=" + newsId + " doesn't exist");
		}
		
		EDR edr = new EDR();
		edr.setUserid(userId);
		edr.setNewsid(newsId);
		edr.setType(type);
		edr.setDateTime(new Date());
		if(reportId != null && !reportId.equals("")) {
			edr.setRemarks(Integer.parseInt(reportId));
		}
	
		boolean isSuccess = false;
		StatusDto statusDto = new StatusDto();

		try {
			edrRepo.save(edr);
			isSuccess = true;
		} catch (Exception e) {
			LOGGER.error("This operation is allowed once per user per news");
			//throw new ConstraintViolationException("This operation is allowed once / { per userId, newsId, type }", null);
		}
		

		if (isSuccess) {
			switch (type) {
			case 1:
				incrementNewsViews(newsId);
				break;
			case 2:
				incrementNewsLikes(newsId);
				break;
			case 3:
				incrementNewsShares(newsId);
				break;
			case 4:
				incrementNewsDislikes(newsId);
				break;
			case 5:
				incrementNewsFlags(newsId, reportId);
				break;
			case 6:
				break;
			default:
				break;
			}
		}	
		LOGGER.debug("EDR Operation {}", isSuccess ? "succedded" : "failed");
		statusDto.setStatus(isSuccess ? "success" : "fail");
		return statusDto;
	}
	
	private boolean incrementNewsViews(long newsId) {
		Optional<News> optional = newsRepo.findById(newsId);
		if(optional.isPresent()) {
			News news = optional.get();
			news.setViews(news.getViews() + 1);
			newsRepo.save(news);
			LOGGER.debug("incrementing news views");
			return true;
		}
		return false;
	}
	
	private boolean incrementNewsLikes(long newsId) {
		Optional<News> optional = newsRepo.findById(newsId);
		if(optional.isPresent()) {
			News news = optional.get();
			news.setLikes(news.getLikes() + 1);
			newsRepo.save(news);
			LOGGER.debug("incrementing news likes");
			return true;
		}
		return false;
	}
	
	private boolean incrementNewsShares(long newsId) {
		Optional<NewsDetails> optional = newsDetailsRepo.findById(newsId);
		if(optional.isPresent()) {
			NewsDetails newsDetails = optional.get();
			newsDetails.setShare(newsDetails.getShare() + 1);
			newsDetailsRepo.save(newsDetails);
			LOGGER.debug("incrementing news shares");
			return true;
		}
		return false;
	}
	
	private boolean incrementNewsDislikes(long newsId) {
		Optional<NewsDetails> optional = newsDetailsRepo.findById(newsId);
		if(optional.isPresent()) {
			NewsDetails newsDetails = optional.get();
			newsDetails.setDislikes(newsDetails.getDislikes() + 1);
			newsDetailsRepo.save(newsDetails);
			LOGGER.debug("incrementing news dislikes");
			return true;
		}
		return false;
	}
	
	private boolean incrementNewsFlags(long newsId, String reportId) {
		Optional<NewsDetails> optional = newsDetailsRepo.findById(newsId);
		if(optional.isPresent()) {
			NewsDetails newsDetails = optional.get();
			newsDetails.setFlage(newsDetails.getFlage() + 1);
			newsDetailsRepo.save(newsDetails);
			LOGGER.debug("incrementing news flags");
			return true;
		}
		return false;
	}

}
