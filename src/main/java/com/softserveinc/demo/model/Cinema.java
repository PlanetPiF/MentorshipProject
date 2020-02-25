package com.softserveinc.demo.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Cinema {

    public Cinema(String name, boolean isOpen, int halls, String address) {
        this.name = name;
        this.open = isOpen;
        this.halls = halls;
        this.movies = new ArrayList<Movie>();
        this.address = address;
    }

    public Cinema() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @NotBlank
    private String name;
    private boolean open;
    @Min(1)
    private int halls;
    private String address;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Movie> movies;
}
