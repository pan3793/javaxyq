package open.xyq.core.fmt.map;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MapUnit {
    private final int[] maskIndexs;
    private final byte[] cellData;
}
