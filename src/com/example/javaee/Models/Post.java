package com.example.javaee.Models;

import java.util.Date;

import javax.persistence.*;

/*
 * class Post(models.Model):
    author = models.ForeignKey(User)
    add_date = models.DateTimeField()
    text = models.TextField()
 */

@Entity
public class Post {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="ID")
	private Long id;
	
	@OneToOne(fetch=FetchType.EAGER)
	private User user;
	
	@OneToOne(fetch=FetchType.EAGER)
	private Flat flat;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date add_date;
	
	@Column(columnDefinition="TEXT")
	private String text;
	
	private String type;
	
	private String title;

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

	public Date getAdd_date() {
		return add_date;
	}

	public void setAdd_date(Date add_date) {
		this.add_date = add_date;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String text) {
		this.type= text;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String text) {
		this.title= text;
	}
	
	
	
}
