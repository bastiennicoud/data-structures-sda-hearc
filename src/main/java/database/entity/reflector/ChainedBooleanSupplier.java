package database.entity.reflector;

import java.util.Objects;
import java.util.function.BooleanSupplier;

/**
 * An extension of the BooleanSupplier that allows to chain them
 */
@FunctionalInterface
public interface ChainedBooleanSupplier extends BooleanSupplier {

    default ChainedBooleanSupplier and(ChainedBooleanSupplier other) {

        Objects.requireNonNull(other);
        return () -> getAsBoolean() && other.getAsBoolean();
    }

}
