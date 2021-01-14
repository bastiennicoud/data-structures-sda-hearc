package ch.edulearn.database.entity.hydrator;

import ch.edulearn.database.entity.Entity;
import ch.edulearn.database.entity.hydrator.exceptions.HydrationException;

import java.sql.ResultSet;
import java.util.List;
import java.util.Objects;

public interface Hydrator<T> {

    /**
     * Factory to create a new hydrator for the desired entity
     *
     * @param entityClass The class that modelise the entity you whant to hydrate
     * @param <E>         The tyle of the Entity
     * @return A fresh entity hydrator thet modelise the provided entity type
     */
    static <E extends Entity> EntityHydrator<E> of(Class<E> entityClass) {

        Objects.requireNonNull(entityClass);

        return new EntityHydrator<>(entityClass);
    }

    List<T> hydrate(ResultSet source) throws HydrationException;

}
