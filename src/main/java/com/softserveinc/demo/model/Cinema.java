package com.softserveinc.demo.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Cinema {

    public Cinema(String name, boolean isOpen, int halls, String address) {
        this.name = name;
        this.isOpen = isOpen;
        this.halls = halls;
        this.movies = new ArrayList<Movie>();
        this.address = address;
    }

    public Cinema() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private boolean isOpen;
    private int halls;
    private String address;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Movie> movies;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getIsOpen() {
        return isOpen;
    } //helps with JSON conversion

    public void setIsOpen(boolean open) {
        isOpen = open;
    }

    public int getHalls() {
        return halls;
    }

    public void setHalls(int halls) {
        this.halls = halls;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
