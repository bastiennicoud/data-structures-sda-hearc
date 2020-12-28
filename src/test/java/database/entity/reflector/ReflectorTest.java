package database.entity.reflector;

import entities.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ReflectorTest {

    @Test
    void fabricShouldReturnNewReflector() {
        assertThat(Reflector.of(User.class)).isInstanceOf(Reflector.class);
    }

}