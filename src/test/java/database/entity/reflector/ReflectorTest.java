package database.entity.reflector;

import database.entity.annotations.Table;
import entities.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ReflectorTest {

    @Test
    void fabricShouldReturnNewReflector() {

        assertThat(Reflector.of(User.class)).isInstanceOf(Reflector.class);
    }

    @Test
    void shouldReturnTableName() {

        assertEquals("users", Reflector.of(User.class).value(Table.class).orElseThrow().value());
    }

}