package ch.edulearn.database.entity.reflector;

import ch.edulearn.database.entity.Entity;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Provides a fluent api to get values form annotations and from the content of an
 * annotated field using reflection.
 * <p>
 * Work on Entity classes, the entity class need to be correctly annotated with some
 * of the annotations in ch.edulearn.database.entity.annotations
 *
 * @param <T> The entyty on wich you want to make reflection
 */
public class EntityReflector<T extends Entity> implements Reflector<T> {

    // The Class that models the rntity on wich we want to perform reflexion
    private final Class<T> entityClass;

    // The underlying stream to perform map/filter/reduce on the filds of the reflected entity
    private Stream<Field> fieldsStream;

    protected EntityReflector(Class<T> entityClass) {

        this.entityClass = entityClass;
        fieldsStream = Arrays.stream(entityClass.getDeclaredFields());

    }

    /**
     * @return Get the underlying stream if you want to perform more surgical filtering and transformations
     */
    public Stream<Field> stream() {

        return fieldsStream;
    }

    /**
     * @return Get the underlying stream as an array
     */
    public Field[] toArray() {

        return fieldsStream.toArray(Field[]::new);
    }

    /**
     * Get the value of an annotation declared on the entity class
     *
     * @param annotationClass The class that mdelize the annotation you whant to find
     * @param <A>             Type of the annotation (Table)
     * @return An optional of string, the otional is empty if the annotation cant be reached
     */
    public <A extends Annotation> Optional<String> getClassAnnotationValue(Class<A> annotationClass) {

        return getAnnotationValue(entityClass, annotationClass);

    }

    /**
     * Add a predicate on the fields strem that filter the strem checking if the provided annotation
     * type is present
     *
     * @param annotationClass The class that mdelize the annotation you whant to use for filtering
     * @param <A>             Type of the annotation (Table)
     * @return This EntityReflector for fluent method chaining
     */
    public <A extends Annotation> EntityReflector<T> is(Class<A> annotationClass) {

        fieldsStream = fieldsStream.filter(field -> field.isAnnotationPresent(annotationClass));

        return this;

    }

    /**
     * Add a predicate on the fields strem that filter the strem checking if the provided annotation
     * type isnt present
     *
     * @param annotationClass The class that mdelize the annotation you whant to use for filtering
     * @param <A>             Type of the annotation (Table)
     * @return This EntityReflector for fluent method chaining
     */
    public <A extends Annotation> EntityReflector<T> not(Class<A> annotationClass) {

        fieldsStream = fieldsStream.filter(field -> !field.isAnnotationPresent(annotationClass));

        return this;

    }

    /**
     * Get the value of the given annotation for each fields remaining in the stream
     *
     * @param annotationClass The class that mdelize the annotation you whant to use for filtering
     * @param <A>             Type of the annotation (Table)
     * @return A stream with the values of the annotations that contains a value, if there
     * is no value for the annotation the stream will ignore it
     */
    public <A extends Annotation> Stream<String> names(Class<A> annotationClass) {

        return fieldsStream
                // Get each annotation value
                .map(field -> getAnnotationValue(field, annotationClass))
                .filter(Optional::isPresent)
                .map(Optional::get);

    }

    /**
     * Get the first field in the list of fields of the Entity, and try to get the value for
     * the annotation specified.
     *
     * @param annotationClass The class that mdelize the annotation you whant to use for filtering
     * @param <A>             Type of the annotation (Table)
     * @return An optionnal
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
     * Get a stream of values of the fields remaining in the stream for a specified entity
     *
     * @param entity The entity on wich you want to get values by reflexion
     * @param <E>    Type of the entity
     * @return A stream with the values of each entity fields
     */
    public <E extends Entity> Stream<String> values(E entity) {

        return fieldsStream
                .map(f -> getFieldValue(f, entity))
                .map(o -> o.orElse(""));

    }

    /**
     * Get the value of an annotation for the specified element
     *
     * @param el              The element on wich you vant to retrieve the annotation value
     * @param annotationClass The class that modelise the annotation
     * @param <A>             Type of thr annotation
     * @return An Optionnl containing the value of the annotation if present
     */
    private <A extends Annotation> Optional<String> getAnnotationValue(
            AnnotatedElement el, Class<A> annotationClass
    ) {

        try {

            // Acces the value method on the annotation by reflexion
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

    /**
     * Get the value of a field for a specified entity
     *
     * @param f      The field on wich you vant to retrieve the value
     * @param entity The entity instance where you want to perform the reflexion for the specified field
     * @return An optionnal of the value if there is a value
     */
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
