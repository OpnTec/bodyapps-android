/*
 * Copyright (c) 2014, Fashiontec (http://fashiontec.org)
 * Licensed under LGPL, Version 3
 */

package org.fashiontec.bodyapps.models;

import java.io.Serializable;

/**
 * Model object for user
 */
public class User implements Serializable {
	private String email;
	private String name;
	private String pic;
	private String id;
	private Boolean isCurrent;

	public User(String email, String name, String id) {
		super();
		this.email = email;
		this.name = name;
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Boolean getIsCurrent() {
		return isCurrent;
	}

	public void setIsCurrent(Boolean isCurrent) {
		this.isCurrent = isCurrent;
	}

}
