package ch.edulearn.database.entity.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate an entity field that can be used for search
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Searchable {

    /**
     * Specifie the field weight in the sqlite search (see https://www.sqlite.org/fts5.html#the_bm25_function)
     *
     * @return The wheight of the annotated filed on the search
     */
    int value();

}
