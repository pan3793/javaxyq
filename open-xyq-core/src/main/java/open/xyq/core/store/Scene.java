/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package open.xyq.core.store;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Scene implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String name;
    private String description;

    public Scene(Integer id) {
        this.id = id;
    }

    public Scene(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
