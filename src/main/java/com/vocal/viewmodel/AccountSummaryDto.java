package com.vocal.viewmodel;

import java.util.List;

public class AccountSummaryDto {
	
	private List<SummaryDto> summaries;
	
	private double total;
	
	public AccountSummaryDto() {
		super();
	}

	public AccountSummaryDto(List<SummaryDto> summaries, double total) {
		super();
		this.summaries = summaries;
		this.total = total;
	}

	public List<SummaryDto> getSummaries() {
		return summaries;
	}

	public void setSummaries(List<SummaryDto> summaries) {
		this.summaries = summaries;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}
	
}
