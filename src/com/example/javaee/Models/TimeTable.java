package com.example.javaee.Models;


import java.util.Date;

import javax.persistence.*;



/*class Flat(models.Model):
    flat_name = models.CharField(max_length=50)
    flat_password = models.CharField(max_length=50)
    flatmates = models.ManyToManyField(User)
    street = models.CharField(max_length=50)
    flat_number = models.CharField(max_length=50)
    city = models.CharField(max_length=50)
    post_code = models.CharField(max_length=50)*/

@Entity
public class TimeTable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="ID")
	private Long id;
	
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

	public Flat getFlat() {
		return flat;
	}

	public void setFlat(Flat flat) {
		this.flat = flat;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getBegin() {
		return begin;
	}

	public void setBegin(Date begin) {
		this.begin = begin;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	@OneToOne(fetch=FetchType.LAZY)
	private User user;
	
	@OneToOne(fetch=FetchType.LAZY)
	private Flat flat;
	
	private String title;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date begin;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date end;
}
