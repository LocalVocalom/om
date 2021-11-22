package com.vocal.mapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.vocal.entities.AppPopup;
import com.vocal.entities.City;
import com.vocal.entities.HeaderContent;
import com.vocal.entities.Language;
import com.vocal.entities.MoreNews;
import com.vocal.entities.News;
import com.vocal.entities.NewsCategory;
import com.vocal.entities.NewsDetails;
import com.vocal.entities.Report;
import com.vocal.entities.ReportReasons;
import com.vocal.entities.State;
import com.vocal.entities.UserInsurance;
import com.vocal.entities.UserLocation;
import com.vocal.utils.Utils;
import com.vocal.viewmodel.AddressModel;
import com.vocal.viewmodel.AppPopupDto;
import com.vocal.viewmodel.CityDto;
import com.vocal.viewmodel.ContentDto;
import com.vocal.viewmodel.InsuranceDto;
import com.vocal.viewmodel.LanguageDto;
import com.vocal.viewmodel.LanguageDtoV1;
import com.vocal.viewmodel.MoreNewsDto;
import com.vocal.viewmodel.NewsCategoryDto;
import com.vocal.viewmodel.NewsCategoryDtoV1;
import com.vocal.viewmodel.NewsDtoInnerAll;
import com.vocal.viewmodel.ReportDto;
import com.vocal.viewmodel.StateDto;

@Component
public class Mapper {
	
	@Value("${HOST_URL}")
	private String HOST_URL;
	
	@Value("${CDN_ENABLED}")
	private boolean cdnEnabled;
	
	@Value("${KYC_BUCKET}")
	private String kycBucketUrl;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Mapper.class);

	public NewsCategoryDto newsCategoryToNewCategoryDto(NewsCategory newsCategory) {
		NewsCategoryDto newsCategoryDto = new NewsCategoryDto();
		newsCategoryDto.setCategoryId(newsCategory.getCategoryId());
		newsCategoryDto.setCategoryName(newsCategory.getCategoryName());
		newsCategoryDto.setAction("News");
		newsCategoryDto.setActionUrl("");
		return newsCategoryDto; 
	}
	
	public NewsCategoryDtoV1 newsCategoryToNewCategoryDtoV1(NewsCategory newsCategory) {
		NewsCategoryDtoV1 newsCategoryDto = new NewsCategoryDtoV1();
		newsCategoryDto.setCategoryId(newsCategory.getCategoryId());
		newsCategoryDto.setCategoryName(newsCategory.getCategoryName());
		newsCategoryDto.setAction("News");
		newsCategoryDto.setActionUrl("");
		newsCategoryDto.setColor(newsCategory.getColor());
		newsCategoryDto.setIcon(newsCategory.getIcon());
		return newsCategoryDto;
	}
	
	public List<NewsCategoryDto> newsCategoryListToNewsCategoryDtoList(List<NewsCategory> newsCategoryList) {
		List<NewsCategoryDto> dtoList = new ArrayList<NewsCategoryDto>(newsCategoryList.size());
		
		for(NewsCategory n: newsCategoryList) {
			dtoList.add( newsCategoryToNewCategoryDto(n));
		}
		
		return dtoList;
	}
	
	
	//Overloaded to add a category for state
	public List<NewsCategoryDto> newsCategoryListToNewsCategoryDtoList(List<NewsCategory> newsCategory,
			State state) {
		NewsCategoryDto newsCategoryDto = new NewsCategoryDto();
		newsCategoryDto.setCategoryId(state.getCategoryId());
		newsCategoryDto.setCategoryName(state.getStateName());
		newsCategoryDto.setAction("News"); 
		newsCategoryDto.setActionUrl("");
		
		List<NewsCategoryDto> dtoList = new ArrayList<NewsCategoryDto>();
		
		List<NewsCategoryDto> tempDtoList = newsCategoryListToNewsCategoryDtoList(newsCategory);
		int pos = 1;
		for(NewsCategoryDto dto : tempDtoList) {
			if(pos == 1 && state != null) {
			//if(pos == 3 && state != null) { SK Comment
				dtoList.add(newsCategoryDto);
			}
			dtoList.add(dto);
			pos++;
		}			
		return dtoList;
	}
	
	public StateDto stateToStateDto(State state) {
		StateDto dto = new StateDto();
		dto.setId(state.getStateId());
		dto.setName(state.getStateName());
		
		return dto;
	}
	
	public List<StateDto> stateListToStateDtoList(List<State> stateList) {
		List<StateDto> dtoList = new ArrayList<StateDto>(stateList.size());
		
		for(State s: stateList) {
			dtoList.add( stateToStateDto(s));
		}
		
		return dtoList;
	}
	
	public CityDto cityToCityDto(City city) {
		CityDto dto = new CityDto();
		dto.setId(city.getCityId());
		dto.setCity(city.getCityName());
		return dto;
	}
	
	public List<CityDto> cityListToCityDtoList(List<City> cityList) {
		List<CityDto> dtoList = new ArrayList<CityDto>(cityList.size());
		
		for(City c: cityList) {
			dtoList.add(cityToCityDto(c));
		}
		
		return dtoList;
	}
	
	public LanguageDto languageToLanguageDto(Language lang) {
		LanguageDto dto = new LanguageDto();
		dto.setId(lang.getLanguageId());
		dto.setLanguage(lang.getLanguageName());
		dto.setCode(lang.getLanguageCode());
		return dto;
	}
	
	public LanguageDtoV1 languageToLanguageDtoV1(Language lang) {
		LanguageDtoV1 dto = new LanguageDtoV1();
		dto.setId(lang.getLanguageId());
		dto.setLanguage(lang.getLanguageName());
		dto.setCode(lang.getLanguageCode());
		dto.setColor(lang.getColor());
		return dto;
	}
	
	public List<LanguageDto> languageListToLanguageDtoList(List<Language> langList) {
		List<LanguageDto> dtoList = new ArrayList<>(langList.size());
		
		for(Language l : langList) {
			dtoList.add(languageToLanguageDto(l));
		}
		
		return dtoList;
	}
	
	public MoreNewsDto moreNewsToMoreNewsDto(MoreNews moreNews) {
		MoreNewsDto dto = new MoreNewsDto();
		dto.setAction_url(moreNews.getActionUrl());
		dto.setImage_url(moreNews.getImageUrl());
		return dto;
	}
	
	public List<MoreNewsDto> moreNewsListToMoreNewsDtoList(List<MoreNews> moreNewsList) {
		List<MoreNewsDto> dtoList = new ArrayList<MoreNewsDto>(moreNewsList.size());
		for(MoreNews m : moreNewsList) {
			dtoList.add( moreNewsToMoreNewsDto(m));
		}
		return dtoList;
	}
	
	public ReportDto reportReasonToReportDto(ReportReasons reportReasons) {
		return new ReportDto(reportReasons.getId(), reportReasons.getReasonText());
	}
	
	public ReportDto reportToReportDto(Report report) {
		ReportDto dto = new ReportDto(report.getId(), report.getText());
		return dto;
	}
	
	public List<ReportDto> reportListToReportDtoList(List<Report> reports) {
		List<ReportDto> dtos = new ArrayList<ReportDto>(reports.size());
		for(Report r : reports) {
			dtos.add(reportToReportDto(r));
		}
		return dtos;
	}

	public List<ContentDto> contentListToContentDtoList(List<HeaderContent> contentList) {
		List<ContentDto> dtoList = new ArrayList<ContentDto>(contentList.size());
		
		for(HeaderContent con: contentList) {
			dtoList.add(new ContentDto(con.getId(), con.getText(), con.isStatus()));
		}
		
		return dtoList;
	}
	
	public UserLocation addressModelToUserLocation(long userId, AddressModel address) {
		UserLocation newLocation = new UserLocation(userId, address.getCountry(), address.getState(), new Date(),
				address.getCity(), address.getLatitude(), address.getLongitude(), address.getPostalCode());
		return newLocation;
	}
	
	public InsuranceDto userInsurenceToInsuranceDto(UserInsurance userInsurence, String termsLink,
			List<String> termsPoints, String policyDetailUrl, String policyNumber, String helplineNumber,
			String helplineEmail) {
		String profilePic;
		if(cdnEnabled) {
			profilePic = userInsurence.getProfilePic();
		} else {
			profilePic = HOST_URL + "kyc_files/" + Utils.getFileName(userInsurence.getProfilePic());
		}
		
		LOGGER.info("ProfilePicUrl={}", profilePic);
		InsuranceDto dto = new InsuranceDto(profilePic, userInsurence.getNominee(), userInsurence.getStatus(),
				userInsurence.getEmail(), userInsurence.getDob(), userInsurence.getName(),
				userInsurence.getInsurenceStartDate());
		
		dto.setUserid(userInsurence.getUserId());
		dto.setGender(userInsurence.getGender());
		
		dto.setTermsLink(termsLink);
		dto.setTermsPoints(termsPoints);
		dto.setPolicyDetailUrl(policyDetailUrl);
		dto.setPolicyNumber(policyNumber);
		dto.setHelplineNumber(helplineNumber);
		dto.setHelplineEmail(helplineEmail);

		return dto;
	}

	public UserLocation updateUserLocationWithAddressModel(UserLocation userLocation, AddressModel address) {
		userLocation.setCity(address.getCity());
		userLocation.setCountry(address.getCountry());
		userLocation.setDateTime(new Date());
		userLocation.setLatitude(address.getLatitude());
		userLocation.setLongitude(address.getLongitude());
		userLocation.setPostalCode(address.getPostalCode());
		userLocation.setState(address.getState());
		return userLocation;
	}

	public List<ReportDto> propertyListToReportDtoList(List<String> reportList) {
		List<ReportDto> dtos = new ArrayList<ReportDto>(reportList.size());
		int idStart = 1;
		for(String r : reportList) {
			ReportDto dto = new ReportDto(idStart++, r);
			dtos.add(dto);
		}
		return dtos;
	}

	public NewsDtoInnerAll createSingleNewsDto(News news, NewsDetails newsDetails) {
		NewsDtoInnerAll newsDtoForAll = new NewsDtoInnerAll(news.getNewsId(), news.getDateTime(), news.getViews(),
				news.getLikes(), news.getNewsLocation(), newsDetails.getNewsHeadline(),
				newsDetails.getNewsDiscriptionText(), newsDetails.getNewsDiscriptionAudioUrl(),
				newsDetails.getNewsImageUrl(), newsDetails.getNewsVideoUrl(), newsDetails.getNewsCreator(),
				newsDetails.getDislikes(), newsDetails.getShare(), newsDetails.getComment(), newsDetails.getFlage(),
				newsDetails.getNewsUrl(), newsDetails.getOpenAction());
		return newsDtoForAll;
	}

	public AppPopupDto getAppPopupDto(AppPopup appPopup) {
		AppPopupDto dto = new AppPopupDto();
		dto.setAction_value(appPopup.getActionValue());
		dto.setCancel_button(appPopup.getCancelButton());
		dto.setIsDismissable(appPopup.isDismissable());
		dto.setOk_button(appPopup.getOkButton());
		dto.setPopup_action(appPopup.getPopupAction());
		dto.setPopup_desc(appPopup.getPopupDesc());
		dto.setPopup_id(String.valueOf(appPopup.getPopupId()));
		dto.setPopup_session(String.valueOf(appPopup.getPopupSession()));
		dto.setPopup_title(appPopup.getPopupTitle());
		return dto;
	}
	
}
