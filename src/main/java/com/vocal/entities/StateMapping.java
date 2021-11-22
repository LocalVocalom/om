package com.vocal.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "state_mapping")
@Entity
public class StateMapping implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "state_id")
	private long stateId;
	
	@Column(name = "alternate_name")
	private String alternateNames;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public long getStateId() {
		return stateId;
	}

	public void setStateId(long stateId) {
		this.stateId = stateId;
	}

	public String getAlternateNames() {
		return alternateNames;
	}

	public void setAlternateNames(String alternateNames) {
		this.alternateNames = alternateNames;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append('(')
		.append("id=").append(id).append(',')
		.append(" stateId=").append(stateId).append(',')
		.append(" alternateNames=").append(alternateNames)
		.append(')');
		return sb.toString();
	}
}
