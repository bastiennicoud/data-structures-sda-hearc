package database;

import database.exceptions.HydrationException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Provides methods to hydrate Entities from SQL tuples using reflected entities annotations.
 */
public class EntityHydrator {

    public <E> Collection<E> hydrate(
            ResultSet results,
            Class<E> entityClass
    ) throws HydrationException {

        ArrayList<E> datas = new ArrayList<>();

        try {

            // Get the constructor for the corresponding Entity by reflexion
            Constructor<E> construct = entityClass.getConstructor(ResultSet.class);

            // Iterates trough the query results
            while (results.next()) {
                // Hydrate entities by calling the reflected entity constructor
                datas.add(construct.newInstance(results));
            }

        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {

            throw new HydrationException(
                    "Reflexion error, Impossible to get the constructor of the Entity.",
                    e
            );

        } catch (SQLException e) {

            throw new HydrationException(
                    "Hydration error, Impossible to get the datas for hydration.",
                    e
            );

        }

        return datas;
    }

}
