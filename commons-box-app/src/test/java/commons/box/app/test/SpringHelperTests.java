package commons.box.app.test;

import commons.box.app.internal.InternalSpringHelper;
import commons.box.util.Types;

import javax.annotation.Nonnull;

/**
 *
 */
public class SpringHelperTests {
    public static void main(String[] args) {
        System.out.println(InternalSpringHelper.ENABLED);
        System.out.println(Types.findAnnotation(Object.class, Nonnull.class));
    }
}
