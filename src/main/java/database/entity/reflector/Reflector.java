package database.entity.reflector;

import database.entity.Entity;

import java.lang.annotation.Annotation;
import java.util.Objects;

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

        return new EntityReflector<T>(t);
    }

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

}
