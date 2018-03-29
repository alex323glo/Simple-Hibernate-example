package com.alex323glo.hibernate;

import com.alex323glo.hibernate.model.City;
import com.alex323glo.hibernate.model.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Date;

/**
 * Created by alex323glo on 10.02.18.
 */
public class TestRelations {

    public static void main(String[] args) {

        City city = new City("Kiev");
        User user1 = new User("Alex", 10000, new Date());
        User user2 = new User("Mike", 2500, new Date());
        User user3 = new User("Serhii", 20000, new Date());
        User user4 = new User("Sam", 3500, new Date());

        user1.setCity(city);
        user2.setCity(city);
        user3.setCity(city);
        user4.setCity(city);

        EntityManagerFactory managerFactory =
                Persistence.createEntityManagerFactory("test-hibernate-h2-unit");
        EntityManager manager = managerFactory.createEntityManager();
        EntityTransaction transaction = manager.getTransaction();

        try {
            transaction.begin();
            manager.persist(city);
            manager.persist(user1);
            manager.persist(user2);
            manager.persist(user3);
            manager.persist(user4);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
        } finally {
            manager.close();
        }

        managerFactory.close();
    }

}
