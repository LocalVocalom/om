package com.vocal.viewmodel;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AppPopupDto {
	
	private String popup_id;
	
	private String popup_title;
	
	private String popup_desc;
	
	private String ok_button;
	
	private String cancel_button;
	
	private String action_value;
	
	private String popup_action;
	
	private boolean isDismissable;
	
	private String popup_session;

	public String getPopup_id() {
		return popup_id;
	}

	public void setPopup_id(String popup_id) {
		this.popup_id = popup_id;
	}

	public String getPopup_title() {
		return popup_title;
	}

	public void setPopup_title(String popup_title) {
		this.popup_title = popup_title;
	}

	public String getPopup_desc() {
		return popup_desc;
	}

	public void setPopup_desc(String popup_desc) {
		this.popup_desc = popup_desc;
	}

	public String getOk_button() {
		return ok_button;
	}

	public void setOk_button(String ok_button) {
		this.ok_button = ok_button;
	}

	public String getCancel_button() {
		return cancel_button;
	}

	public void setCancel_button(String cancel_button) {
		this.cancel_button = cancel_button;
	}

	public String getAction_value() {
		return action_value;
	}

	public void setAction_value(String action_value) {
		this.action_value = action_value;
	}

	public String getPopup_action() {
		return popup_action;
	}

	public void setPopup_action(String popup_action) {
		this.popup_action = popup_action;
	}

	public boolean isIsDismissable() {
		return isDismissable;
	}

	@JsonProperty("isDismissable")
	public void setIsDismissable(boolean isDismissable) {
		this.isDismissable = isDismissable;
	}

	public String getPopup_session() {
		return popup_session;
	}

	public void setPopup_session(String popup_session) {
		this.popup_session = popup_session;
	}

	@Override
	public String toString() {
		return "AppPopupDto [popup_id=" + popup_id + ", popup_title=" + popup_title + ", popup_desc=" + popup_desc
				+ ", ok_button=" + ok_button + ", cancel_button=" + cancel_button + ", action_value=" + action_value
				+ ", popup_action=" + popup_action + ", isDismissable=" + isDismissable + ", popup_session="
				+ popup_session + "]";
	}
}
