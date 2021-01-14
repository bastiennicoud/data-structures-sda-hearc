package ch.edulearn.database.entity.reflector;

import ch.edulearn.database.entity.Entity;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Objects;
import java.util.stream.Stream;

public interface Reflector<T extends Entity> {

    /**
     * Fabric a reflector from a Class
     *
     * @param t   The Class representation of the class or object instance on which you want to perform reflexion.
     * @param <T> The type of the represented class
     * @return New reflector instance
     */
    static <T extends Entity> EntityReflector<T> of(Class<T> t) {

        Objects.requireNonNull(t);

        return new EntityReflector<>(t);
    }

    /**
     * Get a stream with all the fields of the
     */
    Stream<Field> stream();

    /**
     * Intermediate operation.
     * Register that the tested class must be annotated with the specified annotation type
     *
     * @param annotationType The Class that models the annotation, ex MyAnnotation.class
     * @param <A>            The annotation type
     * @return The current reflector for fluent chaining
     */
    <A extends Annotation> Reflector<T> is(Class<A> annotationType);

    /**
     * Intermediate operation.
     * Register that the tested class must be annotated with the specified annotation type
     *
     * @param annotationType The Class that models the annotation, ex MyAnnotation.class
     * @param <A>            The annotation type
     * @return The current reflector for fluent chaining
     */
    <A extends Annotation> Reflector<T> not(Class<A> annotationType);

    /**
     * Terminal operation
     * Get the value of the specified annotation if it remain in the stream
     *
     * @param annotationType The Class that models the annotation, ex MyAnnotation.class
     * @param <A>            The annotation type
     * @return A stream of the values of each annotations
     */
    <A extends Annotation> Stream<String> names(Class<A> annotationType);

    /**
     * Terminal operation
     * Get the value of each fields reamining in the stream for the given entity
     *
     * @param entity The entity where to get the fields values
     * @param <E>    The entity type
     * @return A stream of the values of each annotations
     */
    <E extends Entity> Stream<String> values(E entity);

}
