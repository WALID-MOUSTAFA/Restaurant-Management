package com.tajine.domain;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Entity
//to prevent hibernate from naming the table "order", because order is somehow a reserved keyword in SQL;
@Table(name= "orders")
public class Order {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne
        @JoinColumn(name = "customer_id")
        private Customer customer;

        @ManyToOne
        @JoinColumn(name="admin_id")
        private Admin admin;

        @Column
        private int totalPrice;

        @Column
        private int totalPaid;

        @Column(nullable = true)
        private String address;

	@Column(columnDefinition = "int default 0")
	private int deliveryFees;
	
        @Column
        private int orderType;

	@OneToMany(mappedBy="order", cascade = CascadeType.ALL)
	private Set<OrderContent> orderContents = new HashSet<>();
	
	
	
        @CreationTimestamp
        @Temporal(TemporalType.TIMESTAMP)
        Date createdAt;

        @UpdateTimestamp
        @Temporal(TemporalType.TIMESTAMP)
        Date updatedAt;

        public Date getCreatedAt() {
                return createdAt;
        }

	public void setDeliveryFees(int deliveryFees) {
		this.deliveryFees = deliveryFees;
	}

	public int getDeliveryFees() {
		return this.deliveryFees;
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

        public Long getId() {
                return id;
        }

        public void setId(Long id) {
                this.id = id;
        }

        public Customer getCustomer() {
                return customer;
        }

        public void setCustomer(Customer customer) {
                this.customer = customer;
        }

        public Admin getAdmin() {
                return admin;
        }

        public void setAdmin(Admin admin) {
                this.admin = admin;
        }

        public int getTotalPrice() {
                return totalPrice;
        }

        public void setTotalPrice(int totalPrice) {
                this.totalPrice = totalPrice;
        }

        public int getTotalPaid() {
                return totalPaid;
        }

        public void setTotalPaid(int totalPaid) {
                this.totalPaid = totalPaid;
        }

        public String getAddress() {
                return address;
        }

        public void setAddress(String address) {
                this.address = address;
        }

        public int getOrderType() {
                return orderType;
        }

        public void setOrderType(int orderType) {
                this.orderType = orderType;
        }

        public Set<OrderContent> getOrderContents() {
                return orderContents;
        }

        public void setOrderContents(Set<OrderContent> orderContents) {
                this.orderContents = orderContents;
        }
}
