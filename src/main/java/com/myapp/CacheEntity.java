package com.myapp;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

@Indexed
public class CacheEntity implements java.io.Serializable{
    @Field(store = Store.YES, analyze = Analyze.NO)
	private String name;
    @Field(store = Store.YES, analyze = Analyze.NO)
	private String phone;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
}
