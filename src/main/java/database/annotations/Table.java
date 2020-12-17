package database.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Annotate Entity with database table name
 */
@Target(ElementType.TYPE)
public @interface Table {

    /**
     * @return The database table name of the tagged entity
     */
    String value();

}
