package ua.mibal.minervaTest.gui.console;

import ua.mibal.minervaTest.model.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public class DataBundle<T extends Entity> {

    private List<String> headers;
    private List<Integer> sizes;
    private List<Function<T, Object>> fields;
    private Function<T, Set<? extends Entity>> function;

    public DataBundle(List<String> headers,
                      List<Integer> sizes,
                      List<Function<T, Object>> fields,
                      Function<T, Set<? extends Entity>> function) {
        this.headers = headers;
        this.sizes = sizes;
        this.fields = new ArrayList<>(fields);
        this.function = function;
    }

    public DataBundle(List<String> headers,
                      List<Integer> sizes,
                      List<Function<T, Object>> fields) {
        this(headers, sizes, fields, null);
    }

    public DataBundle(List<String> headers,
                      List<Function<T, Object>> fields) {
        this(headers, List.of(), fields, null);
    }

    public DataBundle(List<String> headers,
                      List<Function<T, Object>> fields,
                      Function<T, Set<? extends Entity>> function) {
        this(headers, List.of(), fields, function);
    }

    public List<String> getHeaders() {
        return headers;
    }

    public List<Integer> getSizes() {
        return sizes;
    }

    public List<Function<T, Object>> getFields() {
        return fields;
    }

    public Optional<Function<T, Set<? extends Entity>>> getFunction() {
        return Optional.ofNullable(function);
    }
}
