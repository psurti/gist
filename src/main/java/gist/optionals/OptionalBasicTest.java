package gist.optionals;

import java.util.Optional;

public class OptionalBasicTest {
    public static void main(String[] args) {
        Optional<String> empty = Optional.of("");
        String s = empty.get();
        assert s != null;
    }
}
