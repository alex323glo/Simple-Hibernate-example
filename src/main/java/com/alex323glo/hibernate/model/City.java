package com.alex323glo.hibernate.model;

import javax.persistence.*;
import java.util.List;

/**
 * City POJO model, annotated with JPA.
 *
 * @author alex323glo
 * @version 1.0
 *
 * @see IDEntity
 */
@Entity
@Table(name = "cities")
public class City extends IDEntity {

    @Column(nullable = false)
    private String name;

    // mappedBy = fieldName, describe reference between column join configurations
    @OneToMany(mappedBy = "city", fetch = FetchType.LAZY)
    private List<User> userList;

    public City() {
    }

    public City(String name) {
        this.name = name;
    }

    public City(String name, List<User> userList) {
        this.name = name;
        this.userList = userList;
    }

    public City(int id, String name, List<User> userList) {
        setId(id);
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

    @Override
    public String toString() {
        return "City{" +
                "id='" + getId() + '\'' +
                ", userList=" + userList +
                ", name=" + name +
                '}';
    }

}
