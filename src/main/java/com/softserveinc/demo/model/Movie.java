package com.softserveinc.demo.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Entity
@Getter
@Setter
public class Movie {

    public Movie() {
    }

    public Movie(String title, boolean released, int budget) {
        this.title = title;
        this.released = released;
        this.budget = budget;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @NotBlank
    private String title;
    private boolean released;
    @Min(0)
    private int budget; //USD millions
}
