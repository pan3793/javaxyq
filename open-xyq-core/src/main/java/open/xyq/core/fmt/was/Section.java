package open.xyq.core.fmt.was;

import lombok.Getter;
import lombok.Setter;
import open.xyq.core.fmt.TintScheme;

import java.util.ArrayList;
import java.util.List;

public class Section {
    @Getter
    @Setter
    private int start;
    @Getter
    @Setter
    private int end;

    private final List<TintScheme> schemes = new ArrayList<>();

    public Section(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public void addScheme(TintScheme scheme) {
        this.schemes.add(scheme);
    }

    public void setScheme(int index, TintScheme scheme) {
        this.schemes.set(index, scheme);
    }

    public TintScheme getScheme(int index) {
        return this.schemes.get(index);
    }

    public int getSchemeCount() {
        return this.schemes.size();
    }
}
