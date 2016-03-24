package com.example.javaee.Models;

import java.sql.Date;

import javax.persistence.*;

/*
 * class UserProfile(models.Model):
    user_id = models.ForeignKey(User)
    firstname = models.CharField(max_length=50)
    lastname = models.CharField(max_length=50)
    birth_date = models.DateTimeField()
    street = models.CharField(max_length=50)
    flat_number = models.CharField(max_length=50)
    city = models.CharField(max_length=50)
    post_code = models.CharField(max_length=50)
 */

@Entity
public class UserProfile {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="ID")
	private Long id;
	
	@OneToOne(fetch=FetchType.EAGER)
	private User user;
	
	private String firstname;
	private String lastname;
	private Date birth_date;
	private String street;
	private String flat_number;
	private String city;
	private String post_code;
	private String phone;

	@Lob 
	@Column(name="PIC")
	private byte[] picture;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public Date getBirth_date() {
		return birth_date;
	}

	public void setBirth_date(Date birth_date) {
		this.birth_date = birth_date;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getFlat_number() {
		return flat_number;
	}

	public void setFlat_number(String flat_number) {
		this.flat_number = flat_number;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPost_code() {
		return post_code;
	}

	public void setPost_code(String post_code) {
		this.post_code = post_code;
	}
	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public byte[] getPicture() {
		return picture;
	}

	public void setPicture(byte[] picture) {
		this.picture = picture;
	}
}
