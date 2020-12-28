package database.entity.reflector;

import database.entity.reflector.exceptions.DisallowedAnnotationException;
import database.entity.reflector.exceptions.MissingAnnotationException;

import java.lang.annotation.Annotation;
import java.util.Objects;
import java.util.Optional;

/**
 * @param <T> The type of the class on which we make reflection
 */
public class ClassReflector<T> implements Reflector<T> {

    // Class representation of the type that parameterize the Reflector
    private final Class<T> typeClass;

    // Predicate chain of tests we want to perform on the typeClass
    private ChainedBooleanSupplier predicateChain;

    protected ClassReflector(Class<T> typeClass) {

        this.typeClass = typeClass;
    }

    private void chainPredicate(ChainedBooleanSupplier test) {

        if (predicateChain == null) {
            predicateChain = test;
        } else {
            predicateChain = predicateChain.and(test);
        }
    }

    private boolean performPredicateChain() {

        if (predicateChain != null) {
            return predicateChain.getAsBoolean();
        }
        return true;
    }

    @Override
    public <A extends Annotation> Reflector<T> is(Class<A> annotationType) {

        Objects.requireNonNull(annotationType);
        chainPredicate(() -> typeClass.isAnnotationPresent(annotationType));

        return this;
    }

    @Override
    public <A extends Annotation> Reflector<T> not(Class<A> annotationType) {

        Objects.requireNonNull(annotationType);
        chainPredicate(() -> !typeClass.isAnnotationPresent(annotationType));

        return this;
    }

    @Override
    public <A extends Annotation> Optional<A> value(Class<A> annotationType)
    throws DisallowedAnnotationException, MissingAnnotationException {

        if (!typeClass.isAnnotationPresent(annotationType)) {
            throw new MissingAnnotationException();
        }

        if (performPredicateChain()) {
            return Optional.ofNullable(typeClass.getDeclaredAnnotation(annotationType));
        } else {
            throw new DisallowedAnnotationException();
        }
    }

}
