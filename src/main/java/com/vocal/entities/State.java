package com.vocal.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "state")
public class State implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "state_id")
	private long stateId;

	@Column(name = "state_name")
	private String stateName;
	
	@Column(name = "category_id")
	private Integer categoryId;
	
	@Column(name = "language_id")
	private Integer languageId;

	@Column(name = "language_id1")
	private Integer languageId1;

	@Column(name = "language_id2")
	private Integer languageId2;

	@Column(name = "language_id3")
	private Integer languageId3;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getStateId() {
		return stateId;
	}

	public void setStateId(long stateId) {
		this.stateId = stateId;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public Integer getLanguageId() {
		return languageId;
	}

	public void setLanguageId(Integer languageId) {
		this.languageId = languageId;
	}

	public Integer getLanguageId1() {
		return languageId1;
	}

	public void setLanguageId1(Integer languageId1) {
		this.languageId1 = languageId1;
	}

	public Integer getLanguageId2() {
		return languageId2;
	}

	public void setLanguageId2(Integer languageId2) {
		this.languageId2 = languageId2;
	}

	public Integer getLanguageId3() {
		return languageId3;
	}

	public void setLanguageId3(Integer languageId3) {
		this.languageId3 = languageId3;
	}
}
