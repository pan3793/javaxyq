package open.xyq.core.fmt.map;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MaskUnit {
    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final int[] data;
}
