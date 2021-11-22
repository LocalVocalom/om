package com.vocal.viewmodel;

import java.util.List;

public class TopicStatus {

	private List<Long> succeeded;
	
	private List<Long> failed;
	
	public TopicStatus() {
		super();
	}

	public TopicStatus(List<Long> succeeded, List<Long> failed) {
		super();
		this.succeeded = succeeded;
		this.failed = failed;
	}

	public List<Long> getSucceeded() {
		return succeeded;
	}

	public void setSucceeded(List<Long> succeeded) {
		this.succeeded = succeeded;
	}

	public List<Long> getFailed() {
		return failed;
	}

	public void setFailed(List<Long> failed) {
		this.failed = failed;
	}
}
