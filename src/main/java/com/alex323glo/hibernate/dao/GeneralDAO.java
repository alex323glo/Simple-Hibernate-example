package com.alex323glo.hibernate.dao;

import com.alex323glo.hibernate.exception.DAOException;
import com.alex323glo.hibernate.model.User;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Implementation of DAO. Used to work with User Entity.
 *
 * WARNING! For more information on methods' logic see implemented interface contracts!
 *
 * @author alex323glo
 * @version 1.0
 *
 * @see DAO
 * @see User
 */
public class GeneralDAO<ID, V> implements DAO<ID, V> {

    private static final Logger LOG = Logger.getLogger(GeneralDAO.class);

    private final EntityManagerFactory factory;
    private final Class<ID> idClass;
    private final Class<V> valueClass;

    public GeneralDAO(EntityManagerFactory factory, Class<ID> idClass, Class<V> valueClass) {
        this.factory = factory;
        this.idClass = idClass;
        this.valueClass = valueClass;
    }

    public EntityManagerFactory getFactory() {
        return factory;
    }

    public Class<ID> getIdClass() {
        return idClass;
    }

    public Class<V> getValueClass() {
        return valueClass;
    }

    /**
     * Saves new instance of Entity to Persistence unit.
     * @see DAO#create(Object)
     */
    @Override
    public V create(V element) throws DAOException {
        LOG.trace("Trying to write (create) new " + valueClass.getSimpleName() + "...");

        checkIfFactoryIsClosed(factory);

        EntityManager manager = factory.createEntityManager();
        EntityTransaction transaction = manager.getTransaction();

        try {
            transaction.begin();
            manager.persist(element);
            transaction.commit();

            LOG.trace("New " + valueClass.getSimpleName() + " instance was successfully wrote (created).");
            return element;
        } catch (Exception e) {
            transaction.rollback();

            LOG.error("Can't write (create) new " + valueClass.getSimpleName() + ". " + e.getMessage(), e);
            throw new DAOException(e);
        } finally {
            manager.close();
        }
    }

    /**
     * Searches for needed instance of Entity, which was stored in Persistence.
     * @see DAO#readById(Object)
     */
    @Override
    public V readById(ID id) throws DAOException {
        LOG.trace("Trying to read (find) " + valueClass.getSimpleName() + " by ID...");

        checkIfFactoryIsClosed(factory);

        EntityManager manager = factory.createEntityManager();

        try {
            V searchedValue = manager.find(valueClass, id);

            LOG.trace("Searched " + valueClass.getSimpleName() + " instance was successfully found.");
            return searchedValue;
        } catch (Exception e) {
            LOG.error("Can't read (find) " + valueClass.getSimpleName() + " by ID. " + e.getMessage(), e);
            throw new DAOException(e);
        } finally {
            manager.close();
        }
    }

    /**
     * Updates stored instance of Entity with new data.
     * @see DAO#update(Object, Object)
     */
    @Override
    public V update(ID id, V newElement) throws DAOException {
        LOG.trace("Trying to update existent " + valueClass.getSimpleName() + "...");

        V oldValue = this.readById(id);

        checkIfFactoryIsClosed(factory);

        EntityManager manager = factory.createEntityManager();
        EntityTransaction transaction = manager.getTransaction();

        try {
            transaction.begin();

            if (oldValue == null) {
                manager.persist(newElement);
                LOG.trace("No " + valueClass.getSimpleName() +
                        " instance stored in Persistence by such ID. Creating (storing) new " +
                        valueClass.getSimpleName() + " instance...");
            } else {
                manager.find(valueClass, id);
                manager.merge(newElement);
                LOG.trace("Updating existent " + valueClass.getSimpleName() + " instance...");
            }

            transaction.commit();

            LOG.trace(valueClass.getSimpleName() + " instance was successfully updated.");
            return oldValue;
        } catch (Exception e) {
            transaction.rollback();

            LOG.error("Can't update " + valueClass.getSimpleName() + " instance. " + e.getMessage(), e);
            throw new DAOException(e);
        } finally {
            manager.close();
        }
    }

    /**
     * Removes stored instance of Entity from Persistence.
     * @see DAO#delete(Object)
     */
    @Override
    public V delete(ID id) throws DAOException {
        LOG.trace("Trying to delete existent " + valueClass.getSimpleName() + " by ID...");
        checkIfFactoryIsClosed(factory);

        EntityManager manager = factory.createEntityManager();
        EntityTransaction transaction = manager.getTransaction();

        try {
            transaction.begin();

            V existentValue = manager.find(valueClass, id);
            if (existentValue == null) {
                throw new DAOException("Attempts to remove nonexistent record from Persistent Unit.");
            } else {
                manager.remove(existentValue);
            }

            transaction.commit();

            LOG.trace("Existent " + valueClass.getSimpleName() + " instance was successfully removed.");
            return existentValue;
        } catch (Exception e) {
            transaction.rollback();

            LOG.error("Can't remove existent " + valueClass.getSimpleName() + ". " + e.getMessage(), e);
            throw new DAOException(e);
        } finally {
            manager.close();
        }
    }

    /**
     * Lists all instances of Entity, which are stored in persistence.
     * @see DAO#getAll(int)
     */
    @Override
    public List<V> getAll(int maxResultsNumber) throws DAOException {
        LOG.trace("Trying to list all " +
                (maxResultsNumber == DAO.NO_LIMIT ? "" : maxResultsNumber + " ") +
                valueClass.getSimpleName() +  "...");

        checkIfFactoryIsClosed(factory);

        EntityManager manager = factory.createEntityManager();

        try {

            TypedQuery<V> typedQuery =
                    manager.createQuery("select e from " + valueClass.getSimpleName() + " e", valueClass);

            if (maxResultsNumber != DAO.NO_LIMIT) {
                typedQuery.setMaxResults(maxResultsNumber);
                LOG.trace("Limiting result List to " + maxResultsNumber + " records...");
            } else {
                LOG.trace("Listing results without any limit...");
            }

            List<V> resultList = typedQuery.getResultList();

            LOG.trace("All needed " + valueClass.getSimpleName() + " instances were successfully listed.");
            return resultList;
        } catch (Exception e) {
            LOG.error("Can't list all needed " + valueClass.getSimpleName() +
                    " instances. " + e.getMessage(), e);
            throw new DAOException(e);
        } finally {
            manager.close();
        }
    }

    /**
     * Checks if proposed EntityManagerFactory is closed.
     *
     * @param targetFactory factory, which will be checked.
     * @throws DAOException if factory was closed at the time of check (oe earlier).
     */
    private void checkIfFactoryIsClosed(EntityManagerFactory targetFactory) throws DAOException {
        if (!targetFactory.isOpen()) {
            DAOException exception =
                    new DAOException("EntityManagerFactory instance was closed before the end of operation.");
            LOG.error(exception.getMessage(), exception);
            throw exception;
        }
    }

    //    private static final Logger LOGGER = Logger.getLogger(UserDAO.class);
//
//    private static final int MAX_QUERIED_USERS_NUM = 100;
//
//    private EntityManagerFactory managerFactory;
//
//    public UserDAO(EntityManagerFactory managerFactory) {
//        this.managerFactory = managerFactory;
//        LOGGER.trace("Create new instance of UserDAO");
//    }
//
//    @Override
//    public User create(User element) {
//        if (!managerFactory.isOpen()) {
//            LOGGER.error("managerFactory is closed");
//            return null;
//        }
//        if (element == null) {
//            LOGGER.error("element is null");
//            return null;
//        }
//        LOGGER.info("create new User");
//
//        EntityManager manager = managerFactory.createEntityManager();
//        EntityTransaction transaction = manager.getTransaction();
//
//        try {
//            transaction.begin();
//            manager.persist(element);
//            transaction.commit();
//
//            LOGGER.info("new User was saved");
//            return manager.find(User.class, element.getId());
//        } catch (Exception e) {
//            LOGGER.error("can't save new User", e);
//            transaction.rollback();
//        } finally {
//            manager.close();
//        }
//
//        return null;
//    }
//
//    @Override
//    public User readById(Object id) {
//        if (!managerFactory.isOpen()) {
//            LOGGER.error("managerFactory is closed");
//            return null;
//        }
//        if (id == null) {
//            LOGGER.error("id is null");
//            return null;
//        }
//        LOGGER.info("search for existent User");
//
//        EntityManager manager = managerFactory.createEntityManager();
//
//        try {
//            User searchResult = manager.find(User.class, id);
//
//            if (searchResult == null) {
//                LOGGER.info("User wasn't found");
//            } else {
//                LOGGER.info("User was found");
//            }
//
//            return searchResult;
//        } catch (Exception e) {
//            LOGGER.error("can't search for existent User", e);
//        } finally {
//            manager.close();
//        }
//
//        return null;
//    }
//
//    @Override
//    public User update(User element) {
//        if (!managerFactory.isOpen()) {
//            LOGGER.error("managerFactory is closed");
//            return null;
//        }
//        if (element == null) {
//            LOGGER.error("element is null");
//            return null;
//        }
//        LOGGER.info("update existent User");
//
//        EntityManager manager = managerFactory.createEntityManager();
//        EntityTransaction transaction = manager.getTransaction();
//
//        try {
//            User oldUser = (User) manager.find(User.class, element.getId()).clone();
//
//            transaction.begin();
//            manager.merge(element);
//            transaction.commit();
//
//            if (oldUser == null) {
//                LOGGER.info("User wasn't updated");
//            } else {
//                LOGGER.info("User was updated");
//            }
//
//            return oldUser;
//        } catch (Exception e) {
//            LOGGER.error("can't update existent User", e);
//            transaction.rollback();
//        } finally {
//            manager.close();
//        }
//
//        return null;
//    }
//
//    @Override
//    public User delete(User element) {
//        if (!managerFactory.isOpen()) {
//            LOGGER.error("managerFactory is closed");
//            return null;
//        }
//        if (element == null) {
//            LOGGER.error("element is null");
//            return null;
//        }
//        LOGGER.info("delete existent User");
//
//        EntityManager manager = managerFactory.createEntityManager();
//        EntityTransaction transaction = manager.getTransaction();
//
//        try {
//            User removedUser = manager.find(User.class, element.getId());
//
//            transaction.begin();
//            manager.merge(removedUser);
//            transaction.commit();
//
//            if (removedUser == null) {
//                LOGGER.info("User wasn't deleted");
//            } else {
//                LOGGER.info("User was deleted");
//            }
//
//            return removedUser;
//        } catch (Exception e) {
//            LOGGER.error("can't delete existent User", e);
//            transaction.rollback();
//        } finally {
//            manager.close();
//        }
//
//        return null;
//    }
//
//    @Override
//    public List<User> getAll() {
//        if (!managerFactory.isOpen()) {
//            LOGGER.error("managerFactory is closed");
//            return null;
//        }
//        LOGGER.info("get all existent Users");
//
//        EntityManager manager = managerFactory.createEntityManager();
//
//        try {
//            TypedQuery<User> typedQuery = manager.createQuery("SELECT u FROM User u", User.class);
//            typedQuery.setMaxResults(MAX_QUERIED_USERS_NUM);
//
//            List<User> resultList = typedQuery.getResultList();
//
//            if (resultList == null) {
//                LOGGER.info("no Users were found");
//            } else {
//                LOGGER.info("all Users were found");
//            }
//
//            return resultList;
//        } catch (Exception e) {
//            LOGGER.error("can't get all existent Users", e);
//        } finally {
//            manager.close();
//        }
//
//        return null;
//    }
}
