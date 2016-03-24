package com.example.javaee.Models;


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
public class Flat {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="ID")
	private Long id;
	
	private String flat_name;
	private String flat_password;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFlat_name() {
		return flat_name;
	}
	public void setFlat_name(String flat_name) {
		this.flat_name = flat_name;
	}
	public String getFlat_password() {
		return flat_password;
	}
	public void setFlat_password(String flat_password) {
		this.flat_password = flat_password;
	}
}
