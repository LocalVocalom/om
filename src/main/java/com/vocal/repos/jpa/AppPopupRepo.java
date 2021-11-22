package com.vocal.repos.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vocal.entities.AppPopup;

@Repository
public interface AppPopupRepo extends JpaRepository<AppPopup, Integer> {

	AppPopup findByPopupIdAndLanguageId(int popupId, int languageId);
	
}
