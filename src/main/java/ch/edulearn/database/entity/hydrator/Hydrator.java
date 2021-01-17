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

    /**
     * Hydrate the entity type form a result set
     *
     * @param source   A result set from the ch.edulearn.database that match the entity you want to hydrate
     * @param capacity The number of rows returned by the DB
     * @return An ArrayList of the hydrated ch.edulearn.entities
     * @throws HydrationException If there is an error during the hydration process
     */
    List<T> hydrate(ResultSet source, int capacity) throws HydrationException;

}
