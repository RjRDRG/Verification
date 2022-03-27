package validation.http;

import java.util.Objects;

public class EndpointKey {
    public final String path;
    public final Method method;

    public EndpointKey(String path, Method method) {
        this.path = path;
        this.method = method;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EndpointKey)) return false;
        EndpointKey that = (EndpointKey) o;
        return path.equals(that.path) && method == that.method;
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, method);
    }
}
