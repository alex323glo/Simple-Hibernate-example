package com.alex323glo.hibernate.model;

import javax.persistence.*;
import java.util.List;

/**
 * Game POJO model, annotated with JPA.
 *
 * @author alex323glo
 * @version 1.0
 *
 * @see IDEntity
 */
@Entity
@Table(name = "games")
public class Game extends IDEntity {

    @Column(nullable = false)
    private String name;

    @ManyToMany
    private List<User> userList;

    public Game() {
    }



    public Game(String name) {
        this.name = name;
    }

    public Game(String name, List<User> userList) {
        this.name = name;
        this.userList = userList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

}
