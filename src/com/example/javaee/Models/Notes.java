package com.example.javaee.Models;

import java.util.Date;

import javax.persistence.*;

/*
 * class Notes(models.Model):
    note_type = models.BooleanField()
    sender = models.ForeignKey(User, related_name='sender')
    reciver = models.ForeignKey(User, related_name='reciver')
    describe = models.TextField()
    date = models.DateTimeField()
 */

@Entity
public class Notes {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="ID")
	private Long id;
	
	private String note_type;
	
	@OneToOne(fetch=FetchType.EAGER)
	private User sender;

	@OneToOne(fetch=FetchType.EAGER)
	private User reciver;
	
	@Column(columnDefinition="TEXT")
	private String descrbe;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNote_type() {
		return note_type;
	}

	public void setNote_type(String note_type) {
		this.note_type = note_type;
	}

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	public User getReciver() {
		return reciver;
	}

	public void setReciver(User reciver) {
		this.reciver = reciver;
	}

	public String getDescrbe() {
		return descrbe;
	}

	public void setDescrbe(String descrbe) {
		this.descrbe = descrbe;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	
	
}
