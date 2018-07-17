package commons.box.app.internal;

import commons.box.app.bean.ClassAccessAnnotation;
import commons.box.util.Collects;
import commons.box.util.Maps;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>创建作者：xingxiuyi </p>
 * <p>版权所属：xingxiuyi </p>
 */
public class InternalClassAccessAnnotation implements ClassAccessAnnotation {
    private static final Annotation[] EMPTY_ANNO = new Annotation[]{};
    private static final InternalClassAccessAnnotation EMPTY = new InternalClassAccessAnnotation(EMPTY_ANNO, EMPTY_ANNO);
    private final List<Class<?>> annotationTypes;
    private final List<Annotation> annotations;
    private final List<Class<?>> declaredAnnotationTypes;
    private final List<Annotation> declaredAnnotations;

    private final Map<String, List<Annotation>> annotationMap;
    private final Map<String, List<Annotation>> declaredAnnotationMap;

    public static InternalClassAccessAnnotation create(Class<?> type) {
        if (type == null) return EMPTY;
        final Annotation[] annoDeclareds = type.getDeclaredAnnotations();
        final Annotation[] annoAll = type.getAnnotations();

        return new InternalClassAccessAnnotation((annoDeclareds == null) ? EMPTY_ANNO : annoDeclareds, (annoAll == null) ? EMPTY_ANNO : annoAll);
    }

    public static InternalClassAccessAnnotation create(AccessibleObject ao) {
        if (ao == null) return EMPTY;
        final Annotation[] annoDeclareds = ao.getDeclaredAnnotations();
        final Annotation[] annoAll = ao.getAnnotations();

        return new InternalClassAccessAnnotation((annoDeclareds == null) ? EMPTY_ANNO : annoDeclareds, (annoAll == null) ? EMPTY_ANNO : annoAll);
    }

    public static InternalClassAccessAnnotation create(final List<Annotation> annoDeclareds, final List<Annotation> annoAll) {
        return new InternalClassAccessAnnotation((
                annoDeclareds == null) ? EMPTY_ANNO : annoDeclareds.toArray(new Annotation[annoDeclareds.size()]),
                (annoAll == null) ? EMPTY_ANNO : annoAll.toArray(new Annotation[annoAll.size()])
        );
    }

    public static InternalClassAccessAnnotation create(final Annotation[] annoDeclareds, final Annotation[] annoAll) {
        return new InternalClassAccessAnnotation((annoDeclareds == null) ? EMPTY_ANNO : annoDeclareds, (annoAll == null) ? EMPTY_ANNO : annoAll);
    }

    private InternalClassAccessAnnotation(final Annotation[] annoDeclareds, final Annotation[] annoAll) {
        final List<Class<?>> tmpAnnotationTypes = new ArrayList<>();
        final List<Annotation> tmpAnnotations = new ArrayList<>();
        final List<Class<?>> tmpDeclaredAnnotationTypes = new ArrayList<>();
        final List<Annotation> tmpDeclaredAnnotations = new ArrayList<>();

        final Map<String, List<Annotation>> tmpAnnotationMap = new HashMap<>();
        final Map<String, List<Annotation>> tmpDeclaredAnnotationMap = new HashMap<>();

        final Map<String, List<Annotation>> immAnnotationMap = new HashMap<>();
        final Map<String, List<Annotation>> immDeclaredAnnotationMap = new HashMap<>();

        // 初始化变量
        if (annoAll != null) for (Annotation a : annoAll) {
            if (a == null) continue;

            Class<?> cls = a.annotationType();
            if (cls == null) continue;

            String cname = cls.getName();
            if (cname == null) continue;

            tmpAnnotationTypes.add(cls);
            tmpAnnotations.add(a);
            List<Annotation> las = tmpAnnotationMap.get(cname);
            if (las == null) {
                las = new ArrayList<>();
                tmpAnnotationMap.put(cname, las);
            }
            las.add(a);

        }

        if (annoDeclareds != null) for (Annotation a : annoDeclareds) {
            if (a == null) continue;
            Class<?> cls = a.annotationType();
            if (cls == null) continue;

            String cname = cls.getName();
            if (cname == null) continue;

            tmpDeclaredAnnotationTypes.add(cls);
            tmpDeclaredAnnotations.add(a);
            List<Annotation> las = tmpDeclaredAnnotationMap.get(cname);
            if (las == null) {
                las = new ArrayList<>();
                tmpDeclaredAnnotationMap.put(cname, las);
            }
            las.add(a);
        }

        for (Map.Entry<String, List<Annotation>> me : tmpAnnotationMap.entrySet()) {
            if (me.getKey() == null || me.getValue() == null) continue;
            immAnnotationMap.put(me.getKey(), Collects.immlist(me.getValue()));
        }

        for (Map.Entry<String, List<Annotation>> me : tmpDeclaredAnnotationMap.entrySet()) {
            if (me.getKey() == null || me.getValue() == null) continue;
            immDeclaredAnnotationMap.put(me.getKey(), Collects.immlist(me.getValue()));
        }

        this.annotationTypes = Collects.immlist(tmpAnnotationTypes);
        this.annotations = Collects.immlist(tmpAnnotations);
        this.declaredAnnotationTypes = Collects.immlist(tmpDeclaredAnnotationTypes);
        this.declaredAnnotations = Collects.immlist(tmpDeclaredAnnotations);

        this.annotationMap = Maps.immmap(immAnnotationMap);
        this.declaredAnnotationMap = Maps.immmap(immDeclaredAnnotationMap);
    }


    @Override
    public final List<Class<?>> annotationTypes() {
        return this.annotationTypes;
    }

    @Override
    public final List<Annotation> annotations() {
        return this.annotations;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <T extends Annotation> List<T> annotations(Class<T> annoType) {
        if (annoType == null) return Collects.immlist();
        String cname = annoType.getName();
        if (cname == null) return Collects.immlist();

        List<Annotation> list = this.annotationMap.get(cname);

        return (list == null) ? Collects.immlist() : (List<T>) list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <T extends Annotation> T annotation(Class<T> annoType) {
        if (annoType == null) return null;
        String cname = annoType.getName();
        if (cname == null) return null;

        List<Annotation> list = this.annotationMap.get(cname);

        return (list == null || list.isEmpty()) ? null : (T) Collects.first(list);

    }

    @Override
    public final List<Class<?>> declaredAnnotationTypes() {
        return this.declaredAnnotationTypes;
    }

    @Override
    public final List<Annotation> declaredAnnotations() {
        return this.declaredAnnotations;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <T extends Annotation> List<T> declaredAnnotations(Class<T> annoType) {
        if (annoType == null) return Collects.immlist();
        String cname = annoType.getName();
        if (cname == null) return Collects.immlist();

        List<Annotation> list = this.declaredAnnotationMap.get(cname);

        return (list == null) ? Collects.immlist() : (List<T>) list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <T extends Annotation> T declaredAnnotation(Class<T> annoType) {
        if (annoType == null) return null;
        String cname = annoType.getName();
        if (cname == null) return null;

        List<Annotation> list = this.declaredAnnotationMap.get(cname);

        return (list == null || list.isEmpty()) ? null : (T) Collects.first(list);
    }


    @Override
    public String toString() {
        return "InternalClassAccessAnnotation{" + annotations + "}";
    }

    public String info() {
        return "InternalClassAccessAnnotation{\n" +
                "\tannotationTypes=" + annotationTypes + "\n" +
                "\tannotations=" + annotations + "\n" +
                "\tdeclaredAnnotationTypes=" + declaredAnnotationTypes + "\n" +
                "\tdeclaredAnnotations=" + declaredAnnotations + "\n" +
                "\tannotationMap=" + annotationMap + "\n" +
                "\tdeclaredAnnotationMap=" + declaredAnnotationMap + "\n" +
                '}';
    }
}
