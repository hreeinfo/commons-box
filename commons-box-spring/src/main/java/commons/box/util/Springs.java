package commons.box.util;

import commons.box.app.AppResource;
import commons.box.spring.resource.SpringAppResourceLoader;
import commons.box.spring.resource.SpringAppResourceWrapper;
import commons.box.spring.resource.SpringResource;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>创建作者：xingxiuyi </p>
 * <p>创建日期：2018/4/22 </p>
 * <p>版权所属：xingxiuyi </p>
 */
public final class Springs {
    private Springs() {
    }

    /**
     * 此方法必须在 Spring ApplicationContext 初始化时调用，否则将无法使用基于 Spring Resource 的资源Loader机制
     */
    public static void applySpring(ApplicationContext context) {
        if (context == null) return;
        applySpringResourceLoader(context, true);
    }

    public static void applySpringResourceLoader(ResourcePatternResolver context, boolean overwrite) {
        if (context == null) return;
        try {
            Resources.addLoader(new SpringAppResourceLoader(context), overwrite);
        } catch (Throwable ignored) {
        }
    }

    /**
     * 获取定义的资源 返回找到的首个资源文件
     *
     * @param pattern
     * @return
     */
    public static SpringResource resource(String pattern) {
        AppResource ap = Resources.resource(pattern);
        if (ap == null) return null;
        else if (ap instanceof SpringResource) return (SpringResource) ap;
        else return new SpringAppResourceWrapper(ap);
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
    public static List<SpringResource> resources(String... patterns) {
        List<SpringResource> resources = new ArrayList<>();

        List<AppResource> aps = Resources.resources(patterns);
        if (aps != null) for (AppResource ap : aps) {
            if (ap == null) continue;

            if (ap instanceof SpringResource) resources.add((SpringResource) ap);
            else resources.add(new SpringAppResourceWrapper(ap));
        }

        return Collects.immlist(resources);
    }

    @SuppressWarnings("unchecked")
    public static List<AppResource> appResources(String... patterns) {
        List<SpringResource> sprs = resources(patterns);
        if (sprs == null) return Collects.immlist(new ArrayList<>());
        return (List) sprs;
    }
}
