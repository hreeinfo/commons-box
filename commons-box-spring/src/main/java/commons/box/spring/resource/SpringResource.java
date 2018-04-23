package commons.box.spring.resource;

import commons.box.app.AppResource;
import org.springframework.core.io.Resource;

/**
 * <p>创建作者：xingxiuyi </p>
 * <p>创建日期：2018/4/22 </p>
 * <p>版权所属：xingxiuyi </p>
 */
public interface SpringResource extends AppResource, Resource {
    default boolean isFile() {
        return false;
    }
}
