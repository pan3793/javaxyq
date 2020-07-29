package open.xyq.core;

public interface NamedComponent {

    default String getName() {
        return this.getClass().getCanonicalName();
    }
}
