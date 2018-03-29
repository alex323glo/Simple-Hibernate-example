package com.alex323glo.hibernate.model;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * POJO ID Entity class, which is mapped as superclass.
 *
 * @author alex323glo
 * @version 1.0
 */
@MappedSuperclass
public abstract class IDEntity {
    @Id
    @GeneratedValue
    private int id;

    public IDEntity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IDEntity idEntity = (IDEntity) o;

        return id == idEntity.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

}
