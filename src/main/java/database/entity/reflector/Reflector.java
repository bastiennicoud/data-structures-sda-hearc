package database.entity.reflector;

import database.entity.reflector.exceptions.DisallowedAnnotationException;
import database.entity.reflector.exceptions.MissingAnnotationException;
import database.entity.reflector.exceptions.RequiredAnnotationException;

import java.lang.annotation.Annotation;
import java.util.Objects;
import java.util.Optional;

public interface Reflector<T> {

    /**
     * Fabric a reflector from a Class
     * @param t The Class representation of the class or object instance on which you want to perform reflexion.
     * @param <T> The type of the represented class
     * @return New reflector instance
     */
    static <T> Reflector<T> of(Class<T> t) {

        Objects.requireNonNull(t);

        return new ClassReflector<T>(t);
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
     * @param annotationType The Class that models the annotation, ex MyAnnotation.class
     * @param <A> The annotation type
     * @return The current reflector for fluent chaining
     */
    <A extends Annotation> Reflector<T> not(Class<A> annotationType);

    /**
     * Terminal operation
     * Return an optional of the parameter annotation type value
     */
    <A extends Annotation> Optional<String> value(Class<A> annotationType)
    throws RequiredAnnotationException, DisallowedAnnotationException, MissingAnnotationException;
}
