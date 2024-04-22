//TOOD(walid): set null on delete, try preRemove annotation;
package com.tajine.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
public class Meal {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	public List<OrderContent> getOrderContents() {
		return orderContents;
	}

	public void setOrderContents(List<OrderContent> orderContents) {
		this.orderContents = orderContents;
	}

	@Column(unique = true)
	private String title;
	
	@Column
	private int price;
	
	@Column(nullable = true)
	private String image;
	
	@ManyToOne
	@JoinColumn(name = "category_id", nullable = true)
	private MealCategory category;

	@OneToMany(mappedBy = "meal", cascade= CascadeType.ALL)
        private List<OrderContent> orderContents;

	@Column(nullable = false, columnDefinition = "boolean default true")
	private boolean active;



	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	Date createdAt;

	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	Date updatedAt;

	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public MealCategory getCategory() {
		return category;
	}

	public void setCategory(MealCategory category) {
		this.category = category;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}
