package com.vocal.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "app_popup")
public class AppPopup implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "popup_id")
	private int popupId;
	
	@Column(name = "popup_title")
	private String popupTitle;
	
	@Column(name = "popup_desc")
	private String popupDesc;
	
	@Column(name = "ok_button")
	private String okButton;
	
	@Column(name = "cancel_button")
	private String cancelButton;
	
	@Column(name = "action_value")
	private String actionValue;
	
	@Column(name = "popup_action")
	private String popupAction;
	
	private boolean isDismissable;
	
	@Column(name = "popup_session")
	private int popupSession;
	
	private int languageId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPopupId() {
		return popupId;
	}

	public void setPopupId(int popupId) {
		this.popupId = popupId;
	}

	public String getPopupTitle() {
		return popupTitle;
	}

	public void setPopupTitle(String popupTitle) {
		this.popupTitle = popupTitle;
	}

	public String getPopupDesc() {
		return popupDesc;
	}

	public void setPopupDesc(String popupDesc) {
		this.popupDesc = popupDesc;
	}

	public String getOkButton() {
		return okButton;
	}

	public void setOkButton(String okButton) {
		this.okButton = okButton;
	}

	public String getCancelButton() {
		return cancelButton;
	}

	public void setCancelButton(String cancelButton) {
		this.cancelButton = cancelButton;
	}

	public String getActionValue() {
		return actionValue;
	}

	public void setActionValue(String actionValue) {
		this.actionValue = actionValue;
	}

	public String getPopupAction() {
		return popupAction;
	}

	public void setPopupAction(String popupAction) {
		this.popupAction = popupAction;
	}

	public boolean isDismissable() {
		return isDismissable;
	}

	public void setDismissable(boolean isDismissable) {
		this.isDismissable = isDismissable;
	}

	public int getPopupSession() {
		return popupSession;
	}

	public void setPopupSession(int popupSession) {
		this.popupSession = popupSession;
	}

	public int getLanguageId() {
		return languageId;
	}

	public void setLanguageId(int languageId) {
		this.languageId = languageId;
	}
}
