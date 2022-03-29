package contract.structures;

import java.util.Objects;

public class Endpoint {
    public enum Method {MISSING, GET, PUT, POST, DELETE, PATCH, OPTIONS, HEAD, TRACE }

    public final String path;
    public final Method method;

    public Endpoint(String path, Method method) {
        this.path = path;
        this.method = method;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Endpoint)) return false;
        Endpoint that = (Endpoint) o;
        return path.equals(that.path) && method == that.method;
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, method);
    }
}
