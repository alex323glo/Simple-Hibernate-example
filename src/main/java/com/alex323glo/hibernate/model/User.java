package com.alex323glo.hibernate.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User POJO model, annotated with JPA.
 *
 * @author alex323glo
 * @version 1.0
 *
 * @see IDEntity
 */
@Entity
@Table(name = "users")
public class User extends IDEntity {

    @Column(nullable = false)
    private String name;

    @Column(precision = 2)
    private double amount;

    @Temporal(TemporalType.DATE)
    private Date birthday;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", referencedColumnName = "id")
    private City city;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name = "user_games",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "game_id", referencedColumnName = "id")})
    private List<Game> gameList;

    public User() {
        gameList = new ArrayList<>();
    }

    public User(String name, double amount, Date birthday, City city) {
        this.name = name;
        this.amount = amount;
        this.birthday = birthday;
        this.city = city;
        gameList = new ArrayList<>();
    }

    public User(String name, double amount, Date birthday) {
        this.name = name;
        this.amount = amount;
        this.birthday = birthday;
    }

    public User(String name, double amount, Date birthday, City city, List<Game> gameList) {
        this.name = name;
        this.amount = amount;
        this.birthday = birthday;
        this.city = city;
        this.gameList = gameList;
    }

    private User(int id, String name, double amount, Date birthday, City city, List<Game> gameList) {
        setId(id);
        this.name = name;
        this.amount = amount;
        this.birthday = birthday;
        this.city = city;
        this.gameList = gameList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public List<Game> getGameList() {
        return gameList;
    }

    public void setGameList(List<Game> gameList) {
        this.gameList = gameList;
    }

}
