package database.entity;

import database.entity.annotations.Column;
import database.exceptions.HydrationException;
import database.exceptions.HydrationFieldException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Provides methods to hydrate Entities from SQL tuples using reflected entities annotations.
 */
public class EntityHydrator {

    public static <E extends Entity> ArrayList<E> hydrate(
            ResultSet results,
            Class<E> entityClass
    ) throws HydrationException {

        var datas = new ArrayList<E>();

        try {

            // Iterates trough the query results
            while (results.next()) {
                // Hydrate entities by calling the reflected entity constructor
                datas.add(
                        // Get the entity constructor and instantiate a
                        // new entity with the result set for hydration
                        entityClass.getConstructor(ResultSet.class)
                                   .newInstance(results)
                );
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

    /**
     * Return the value for a specific field with the corresponding type
     * @return The value of the corresponding column for the field
     * @throws SQLException Throws if its impossible to get the column ata from the result set
     * @throws HydrationFieldException Throws if the type is unsupported by the hydrator
     */
    public static Object hydrateField(Field field, ResultSet resultSet)
    throws SQLException, HydrationFieldException {

        // Check the field type to use the correct method to retrieve the column from the result set
        var columnName = field.getDeclaredAnnotation(Column.class).value();

        // Check the field type to call the correct getter on the result set
        return switch (field.getType().getName()) {
            case "int" -> resultSet.getInt(columnName);
            case "java.lang.String" -> resultSet.getString(columnName);
            // Throws a exception to indicate that the hydrator dont know hot to hydrate this field type
            default -> throw new HydrationFieldException(
                    "Cannot get field, type unsupported by hydrator, custom implementation required"
            );
        };
    }

}
