package com.tajine.domain;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Customer {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

	@Column(unique = true, nullable = false)
        private String name;

        @Column
        private String phone;

        @Column
        private  String address;

        public String getAddress() {
                return address;
        }

        public void setAddress(String address) {
                this.address = address;
        }

        @Column(nullable = true)
        private String points;

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

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public String getPhone() {
                return phone;
        }

        public void setPhone(String phone) {
                this.phone = phone;
        }

        public String getPoints() {
                return points;
        }

        public void setPoints(String points) {
                this.points = points;
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
}
