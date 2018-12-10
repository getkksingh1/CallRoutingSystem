package com.triyasoft.model;

import java.util.Date;

public class ContactModel {

	private int id;
	private String number;
	private int bloced;
	private Date created_at;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public int getBloced() {
		return bloced;
	}

	public void setBloced(int bloced) {
		this.bloced = bloced;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

}
