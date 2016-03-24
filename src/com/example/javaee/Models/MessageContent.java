package com.example.javaee.Models;

import javax.persistence.*;

/*
 * class Message(models.Model):
    header_id = models.ForeignKey(MessageHeader)
    content = models.TextField()
    read = models.BooleanField()
 */

@Entity
public class MessageContent {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="ID")
	private Long id;
	
	@OneToOne(fetch=FetchType.EAGER)
	private MessageHeader header;
	
	@Column(columnDefinition="TEXT")
	private String content;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public MessageHeader getHeader() {
		return header;
	}

	public void setHeader(MessageHeader header) {
		this.header = header;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	
}
