package commons.box.util;

import commons.box.app.AppResource;
import commons.box.app.internal.InternalSpringAppResourceLoader;
import commons.box.app.internal.InternalSpringAppResourceWrapper;
import commons.box.app.internal.InternalSpringHelper;
import commons.box.app.internal.InternalSpringResource;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>创建作者：xingxiuyi </p>
 * <p>版权所属：xingxiuyi </p>
 */
public final class Springs {


    private Springs() {
    }

    /**
     * 此方法必须在 Spring ApplicationContext 初始化时调用，否则将无法使用基于 Spring Resource 的资源Loader机制
     */
    public static void apply(ApplicationContext context) {
        if (context == null) return;
        InternalSpringHelper.applyResourceLoader(context, true);
    }

    public static void applyResourceLoader(ResourcePatternResolver context, boolean overwrite) {
        if (context == null) return;
        InternalSpringHelper.applyResourceLoader(context, overwrite);
    }

    /**
     * 获取定义的资源 返回找到的首个资源文件
     *
     * @param pattern
     * @return
     */
    public static Resource resource(String pattern) {
        AppResource ap = Resources.resource(pattern);
        if (ap == null) return null;
        else if (ap instanceof InternalSpringResource) return (InternalSpringResource) ap;
        else return new InternalSpringAppResourceWrapper(ap);
    }

    /**
     * 获取定义的资源 返回找到的全部文件
     * <p>
     * 注意本方法中，未找到资源是返回 Empty List
     * <p>
     * 此方法支持 * 匹配
     *
     * @param patterns
     * @return 找到的资源，未找到返回空List，总是不返回null
     */
    @Nonnull
    public static List<Resource> resources(String... patterns) {
        List<Resource> resources = new ArrayList<>();

        List<AppResource> aps = Resources.resources(patterns);
        for (AppResource ap : aps) {
            if (ap == null) continue;

            if (ap instanceof InternalSpringResource) resources.add((InternalSpringResource) ap);
            else resources.add(new InternalSpringAppResourceWrapper(ap));
        }

        return Collects.immlist(resources);
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    public static List<AppResource> appResources(String... patterns) {
        return Collects.immlist(Resources.resources(patterns));
    }
}
