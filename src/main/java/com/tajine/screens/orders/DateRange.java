package com.tajine.screens.orders;

public class DateRange {

	private String from;
	private String to;


	//I use it to check if the from and the to date is the default placeholder or the actuall one.
	private boolean isNew;
	
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}

	public void setIsNew(boolean isNew) {
		this.isNew = isNew;
	}

	public boolean getIsNew(){
		return this.isNew;
	}

	
}
