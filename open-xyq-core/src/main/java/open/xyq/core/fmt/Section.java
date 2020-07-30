package open.xyq.core.fmt;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class Section {
    @Getter
    @Setter
    private int start;
    @Getter
    @Setter
    private int end;

    private final List<ColorationScheme> schemes = new ArrayList<>();

    public Section(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public void addScheme(ColorationScheme scheme) {
        this.schemes.add(scheme);
    }

    public void setScheme(int index, ColorationScheme scheme) {
        this.schemes.set(index, scheme);
    }

    public ColorationScheme getScheme(int index) {
        return this.schemes.get(index);
    }

    public int getSchemeCount() {
        return this.schemes.size();
    }
}
