package OmGU.IMIT;

import java.util.Objects;

/**
 * Class for saved two elements in pair
 */
public final class Pair<T, U> {

    private final T first;
    private final U second;

    /**
     * Basic constructor saved type
     *
     * @param first  type first element
     * @param second type second element
     */
    Pair(final T first, final U second) {
        this.first = first;
        this.second = second;
    }

    T getFirst() {
        return first;
    }

    U getSecond() {
        return second;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Pair pair = (Pair) o;
        return Objects.equals(first, pair.first) &&
                Objects.equals(second, pair.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
}

