package com.example.javaee.Models;

import java.util.Date;

import javax.persistence.*;

/*
 * class Notify(models.Model):
    reciver = models.ForeignKey(User)
    subject = models.CharField(max_length=50)
    text = models.TextField()
    data = models.DateTimeField()
 */

@Entity
public class Notify {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="ID")
	private Long id;
	
	@OneToOne(fetch=FetchType.LAZY)
	private User reciver;
	
	private String subject;
	
	@Column(columnDefinition="TEXT")
	private String text;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getReciver() {
		return reciver;
	}

	public void setReciver(User reciver) {
		this.reciver = reciver;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	
}
