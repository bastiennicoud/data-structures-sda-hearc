package database.entity.reflector;

import database.entity.Entity;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

public class EntityReflector<T extends Entity> implements Reflector<T> {

    // The Class that models the rntity on wich we want to perform reflexion
    private final Class<T> entityClass;

    // The underlying stream to perform map/filter/reduce on the filds of the reflected entity
    private Stream<Field> fieldsStream;

    protected EntityReflector(Class<T> entityClass) {

        this.entityClass = entityClass;
        fieldsStream = Arrays.stream(entityClass.getDeclaredFields());

    }

    public Stream<Field> stream() {

        return fieldsStream;
    }

    public <A extends Annotation> Optional<String> getClassAnnotationValue(Class<A> annotationClass) {

        return getAnnotationValue(entityClass, annotationClass);

    }

    /**
     * @return Get the fields of the entity annotated with de Column annotation
     */
    public <A extends Annotation> EntityReflector<T> is(Class<A> annotationClass) {

        fieldsStream = fieldsStream.filter(field -> field.isAnnotationPresent(annotationClass));

        return this;

    }

    /**
     * @return Get the fields of the entity annotated with de Column annotation
     */
    public <A extends Annotation> EntityReflector<T> not(Class<A> annotationClass) {

        fieldsStream = fieldsStream.filter(field -> !field.isAnnotationPresent(annotationClass));

        return this;

    }

    /**
     * @return Get the fields of the entity annotated with de Column annotation
     */
    public <A extends Annotation> Stream<String> names(Class<A> annotationClass) {

        return fieldsStream
                .map(field -> getAnnotationValue(field, annotationClass))
                .filter(Optional::isPresent)
                .map(Optional::get);

    }

    /**
     * @return Get the fields of the entity annotated with de Column annotation
     */
    public <A extends Annotation> Optional<String> firstName(Class<A> annotationClass) {

        Optional<Field> field = fieldsStream.findFirst();

        // Check if there is a identity field retrieved by the find first
        if (field.isPresent()) {
            return getAnnotationValue(field.get(), annotationClass);
        }

        return Optional.empty();

    }

    /**
     * @return Get the fields of the entity annotated with de Column annotation
     */
    public <E extends Entity> Stream<String> values(E entity) {

        return fieldsStream
                .map(f -> getFieldValue(f, entity))
                .map(o -> o.orElse(""));

    }

    private <A extends Annotation> Optional<String> getAnnotationValue(AnnotatedElement el, Class<A> annotationClass) {

        try {

            return Optional.of(el.getDeclaredAnnotation(annotationClass)
                                 .getClass()
                                 .getMethod("value")
                                 .invoke(el.getDeclaredAnnotation(annotationClass))
                                 .toString());

        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {

            System.out.println("Impossible to get the value from the required annotation :");
            e.printStackTrace();

            return Optional.empty();

        }
    }

    private <E extends Entity> Optional<String> getFieldValue(Field f, E entity) {

        try {

            return Optional.of(f.get(entity).toString());

        } catch (IllegalAccessException | IllegalArgumentException e) {

            System.out.println("Impossible to get the value from the required field :");
            e.printStackTrace();

            return Optional.empty();

        }
    }

}
