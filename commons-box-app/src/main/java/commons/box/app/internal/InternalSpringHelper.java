package commons.box.app.internal;

import commons.box.util.Resources;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

/**
 * 为了保证非 Spring 的服务能够正确运行，此处提供最小的spring 操作集合
 */
public class InternalSpringHelper {
    public static boolean ENABLED = springEnabled();

    private static boolean springEnabled() {
        try {
            Class<?> cls = Class.forName("org.springframework.core.io.Resource");
            if (cls != null) return true;
        } catch (Throwable ignored) {
        }

        return false;
    }

    public static <A extends Annotation> A findAnnotation(Class<?> clazz, Class<A> annotationType) {
        if (clazz == null || annotationType == null) return null;
        return AnnotationUtils.findAnnotation(clazz, annotationType);
    }

    public static <A extends Annotation> A findAnnotation(AnnotatedElement element, Class<A> annotationType) {
        if (element == null || annotationType == null) return null;
        return AnnotationUtils.findAnnotation(element, annotationType);
    }

    public static void applyResourceLoader(ResourcePatternResolver context, boolean overwrite) {
        if (context == null) return;
        try {
            Resources.addLoader(new InternalSpringAppResourceLoader(context), overwrite);
        } catch (Throwable ignored) {
        }
    }
}
