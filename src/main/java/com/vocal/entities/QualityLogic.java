package com.vocal.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class QualityLogic {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private double percentStart;
	
	private double percentEnd;
	
	private long quality;
	
	private int amount;

	public double getPercentStart() {
		return percentStart;
	}

	public void setPercentStart(double percentStart) {
		this.percentStart = percentStart;
	}

	public double getPercentEnd() {
		return percentEnd;
	}

	public void setPercentEnd(double percentEnd) {
		this.percentEnd = percentEnd;
	}

	public long getQuality() {
		return quality;
	}

	public void setQuality(long quality) {
		this.quality = quality;
	}
	
	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "QualityLogic [id=" + id + ", percentStart=" + percentStart + ", percentEnd=" + percentEnd + ", quality="
				+ quality + ", amount=" + amount + "]";
	}
	
}
