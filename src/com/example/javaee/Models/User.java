package com.example.javaee.Models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="ID")
	Long id;
	
	String user_name;
	String password;
	String email;
	@Temporal(TemporalType.TIMESTAMP)
	Date register_dateTime;
	@Temporal(TemporalType.TIMESTAMP)
	Date last_login_time;
	Boolean active;
	
	@OneToOne(fetch=FetchType.EAGER)
	Flat flat;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Date getRegister_dateTime() {
		return register_dateTime;
	}
	public void setRegister_dateTime(Date register_dateTime) {
		this.register_dateTime = register_dateTime;
	}
	public Date getLast_login_time() {
		return last_login_time;
	}
	public void setLast_login_time(Date last_login_time) {
		this.last_login_time = last_login_time;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	
	public Flat getFlat() {
		return flat;
	}
	public void setFlat(Flat flat) {
		this.flat = flat;
	}
	
	
	
}
