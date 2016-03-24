package com.example.javaee.Models;

import java.util.*;

import javax.persistence.*;

/*
 * class MessageHeader(models.Model):
    from_id = models.ForeignKey(User, related_name='from_id')
    to_id = models.ForeignKey(User, related_name='to_id')
    subject = models.CharField(max_length=50)
    time = models.DateTimeField()
 */

@Entity
public class MessageHeader {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="ID")
	private Long id;
	
	@OneToOne(fetch=FetchType.EAGER)
	private User from;
	
	@OneToOne(fetch=FetchType.EAGER)
	private User to;
	
	private String subject;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getFrom() {
		return from;
	}

	public void setFrom(User from) {
		this.from = from;
	}

	public User getTo() {
		return to;
	}

	public void setTo(User to) {
		this.to = to;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	
}
