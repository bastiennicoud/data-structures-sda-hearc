package database;

import Entity.User;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class DataRepository {

    private final Connection dbConnection;

    /**
     * @param dbConnection an active connection to the db
     */
    public DataRepository(Connection dbConnection) {

        this.dbConnection = dbConnection;

    }

    /**
     * @param query The query statement you want to execute
     * @param entityClass The class you want to hydrate with the query results,
     *                    the query must be compatible with the Entity hydrate method
     * @param <E> Entity type
     * @return A collection of hydrated Entity
     */
    public <E> Collection<E> query(String query, Class<E> entityClass) {

        ArrayList<E> datas = new ArrayList<>();

        try {

            // SQL query execution
            Statement statement = dbConnection.createStatement();
            ResultSet results = statement.executeQuery(query);
            // Get the constructor for the corresponding Entity by reflexion
            Constructor<E> construct = entityClass.getConstructor(ResultSet.class);
            // Iterates trough the query results
            while (results.next()) {
                // Hydrate entities by calling the reflected entity constructor
                datas.add(construct.newInstance(results));
            }

        } catch (SQLException e) {

            System.out.println(e.getErrorCode() + e.getMessage());

        } catch (IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchMethodException e) {

            e.printStackTrace();

        }

        return datas;

    }

    /**
     * Find all elements from the table corresponding to the entity
     * @param entityClass
     * @return
     */
    public <E> Collection<E> findAll(Class<E> entityClass) {



    }
}
