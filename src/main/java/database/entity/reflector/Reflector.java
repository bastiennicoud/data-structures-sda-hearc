package database.entity.reflector;

import java.lang.annotation.Annotation;
import java.util.Objects;
import java.util.Optional;

public interface Reflector<T> {

    public static <T> Reflector<T> of(Class<T> t) {

        Objects.requireNonNull(t);

        return null;
    }

    <A extends Annotation> Reflector<T> is(Class<A> annotationType) throws RequiredAnnotationException;

    <A extends Annotation> Reflector<T> not(Class<A> annotationType) throws DisallowedAnnotationException;

    /**
     * Return an optional of the parameter annotation type value
     */
    <A extends Annotation> Optional<String> value(Class<A> annotationType) throws MissingAnnotationException;

}
