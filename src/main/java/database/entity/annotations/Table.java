package database.entity.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate Entity with database table name
 * Allows DataRepository to use shortcuts query methods with this entity
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {

    /**
     * @return The database table name of the tagged entity
     */
    String value();

}
