package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Task name required")
    @Size(min = 4)
    private String header;

    @NotBlank(message = "Description must be present")
    @Size(min = 2, message = "Must be at least one word")
    private String description;

    @NotNull(message = "Payout can not be empty")
    @PositiveOrZero(message = "Payout must be zero or positive")
    @Column(nullable = false)
    private BigDecimal payout;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "task_optional_skills",
            joinColumns = @JoinColumn(name = "task_id")
    )

    @Column(name = "skill_name")
    private List<String> optionalSkills = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPayout() {
        return payout;
    }

    public void setPayout(BigDecimal payout) {
        this.payout = payout;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<String> getOptionalSkills() {
        return optionalSkills;
    }

    public void addSkill(String skill){
        optionalSkills.add(skill);
    }
}
