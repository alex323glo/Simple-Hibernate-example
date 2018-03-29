package com.alex323glo.hibernate.dao;

import com.alex323glo.hibernate.exception.DAOException;

import java.util.List;

/**
 * General DAO interface.
 *
 * @param <ID> type of Primary Key of stored Entity.
 * @param <V> type of stored Entity.
 *
 * Methods could throw DAOException (kind of RuntimeException), if
 * any of them had some problems during execution.
 *
 * @author alex323glo
 * @version 1.0
 *
 * @see DAOException
 */
public interface DAO<ID, V> {

    static final int NO_LIMIT = -1;

    /**
     * Saves new instance of Entity to Persistence unit.
     *
     * @param element target instance.
     * @return saved (managed) instance of Entity, if operation was successful.
     * @throws DAOException if operation wasn't successful.
     */
    V create(V element) throws DAOException;

    /**
     * Searches for needed instance of Entity, which was stored in Persistence.
     *
     * @param id unique identifier of stored instance.
     * @return needed (with unique 'id' key) instance, if it was stored in Persistence,
     * or null, if Persistence doesn't contain such instance.
     * @throws DAOException if operation wasn't successful.
     */
    V readById(ID id) throws DAOException;

    /**
     * Updates stored instance of Entity with new data.
     *
     * @param id unique identifier of stored instance.
     * @param element new version of instance.
     *
     * @return old version of stored instance, if it was stored in Persistence,
     * or null, if Persistence doesn't contain such instance (in this case it will store
     * proposed version of instance as new record).
     * @throws DAOException if operation wasn't successful.
     */
    V update(ID id, V element) throws DAOException;

    /**
     * Removes stored instance of Entity from Persistence.
     *
     * @param id unique identifier of target instance, which was stored in Persistence.
     * @return instance of Entity, which, as result, was removed from Persistence,
     * if operation was successful.
     * @throws DAOException if operation wasn't successful.
     */
    V delete(ID id) throws DAOException;

    /**
     * Lists all instances of Entity, which are stored in persistence.
     *
     * @param maxResultsNumber max number of records, which will be listed (used for pagination).
     *                         Use constant 'NO_LIMIT' to ignore pagination.
     * @return List of stored instances, if operation was successful.
     * @throws DAOException if operation wasn't successful.
     *
     * @see DAO#NO_LIMIT
     */
    List<V> getAll(int maxResultsNumber) throws DAOException;

}
