package database.entity.reflector;

import database.entity.reflector.exceptions.DisallowedAnnotationException;
import database.entity.reflector.exceptions.MissingAnnotationException;
import database.entity.reflector.exceptions.RequiredAnnotationException;

import java.util.Optional;

/**
 * @param <T> The type of the class on which we make reflection
 */
public class ClassReflector<T> implements Reflector<T> {

    private final Class<T> typeClass;

    protected ClassReflector(Class<T> typeClass) {

        this.typeClass = typeClass;
    }

    @Override
    public Reflector is(Class annotationType) {

        return null;
    }

    @Override
    public Reflector not(Class annotationType) {

        return null;
    }

    @Override
    public Optional<String> value(Class annotationType)
    throws RequiredAnnotationException, DisallowedAnnotationException, MissingAnnotationException {

        return Optional.empty();
    }

}
