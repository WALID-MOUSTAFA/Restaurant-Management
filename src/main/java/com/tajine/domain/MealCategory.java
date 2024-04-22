package com.tajine.domain;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class MealCategory {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        //TODO(WALID): SEE WHAT EXCEPTION THAT CAN THROW, AND HOW TO INFORM THE USER OF IT;
        @Column(unique = true)
        private String title;

        public List<Meal> getMeals() {
                return meals;
        }

        public void setMeals(List<Meal> meals) {
                this.meals = meals;
        }

        @OneToMany(mappedBy = "category")
        private List<Meal> meals;

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

        public String getTitle() {
                return title;
        }

        public void setTitle(String title) {
                this.title = title;
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
