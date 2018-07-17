package commons.box.app.bean;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * 可访问注解
 * <p>创建作者：xingxiuyi </p>
 * <p>版权所属：南京恒立信息科技有限公司 </p>
 */
public interface ClassAccessAnnotation {
    /**
     * 获取此元素声明注解的类型
     *
     * @return
     */
    public List<Class<?>> annotationTypes();

    /**
     * 获取此元素的所有注解
     *
     * @return
     */
    public List<Annotation> annotations();

    /**
     * 根据类型获取对应的注解
     *
     * @param annoType
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends Annotation> List<T> annotations(Class<T> annoType);

    /**
     * 根据类型获取对应的注解，如果存在多个相同的注解那么只返回第一个（也就是相对于元素来说最近的一个）
     *
     * @param annoType
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends Annotation> T annotation(Class<T> annoType);

    /**
     * 返回直接存在于此元素上的注释类型。与此接口中的其他方法不同，该方法将忽略继承的注释。
     *
     * @return
     */
    public List<Class<?>> declaredAnnotationTypes();

    /**
     * 返回直接存在于此元素上的所有注释。与此接口中的其他方法不同，该方法将忽略继承的注释。
     *
     * @return
     */
    public List<Annotation> declaredAnnotations();

    /**
     * 返回直接存在于此元素上的注释。与此接口中的其他方法不同，该方法将忽略继承的注释。
     *
     * @param annoType
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends Annotation> List<T> declaredAnnotations(Class<T> annoType);

    /**
     * 返回直接存在于此元素上的注释。与此接口中的其他方法不同，该方法将忽略继承的注释。如果存在多个相同的注解那么只返回第一个（也就是相对于元素来说最近的一个）
     *
     * @param annoType
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends Annotation> T declaredAnnotation(Class<T> annoType);


}
