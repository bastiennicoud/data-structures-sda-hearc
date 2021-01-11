package database.entity.reflector;

import database.entity.annotations.Searchable;
import database.entity.annotations.Table;
import database.entity.reflector.exceptions.MissingAnnotationException;
import entities.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReflectorTest {

    @Test
    void fabricShouldReturnNewReflector() {

        assertThat(Reflector.of(User.class)).isInstanceOf(Reflector.class);
    }

    @Test
    void shouldReturnTableName() {

        assertEquals(
                "users",
                Reflector.of(User.class).value(Table.class).orElseThrow().value()
        );
    }

    @Test
    void shouldThrowOnMissingAnnotation() {

        assertThrows(
                MissingAnnotationException.class,
                () -> Reflector.of(User.class).value(Searchable.class)
        );
    }

}