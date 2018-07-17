package commons.box.app.internal;

import commons.box.app.AppResource;
import org.springframework.core.io.Resource;

/**
 * <p>创建作者：xingxiuyi </p>
 * <p>版权所属：xingxiuyi </p>
 */
public interface InternalSpringResource extends AppResource, Resource {
    default boolean isFile() {
        return false;
    }
}
