package com.zf.util;

import java.util.List;
import java.util.Map;

public class ReportInfo {
	
	private String name;
	
	private Map<String, String> parameters;
	
	private long spendTime;
	
	private String status;
	
	private List<String> log;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}

	public long getSpendTime() {
		return spendTime;
	}

	public void setSpendTime(long spendTime) {
		this.spendTime = spendTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<String> getLog() {
		return log;
	}

	public void setLog(List<String> log) {
		this.log = log;
	}
	
}
