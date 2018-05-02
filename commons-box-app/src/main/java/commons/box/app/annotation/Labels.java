package commons.box.app.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 集中定义message，注意此方法定义的message仅向消息源增加消息，而不参与property消息解析labelName的重写<br/>
 * 规则：<br/>
 *      一、定义在类中：如果Message的key为空则定义key为类名；<br/>
 *      二、定义在方法或字段上（property），如果Message的key唯恐则定义key为property名，此规则与Message相同；<br/>
 *      三、如果key以+开头，以默认基础key扩展后续键，此规则与Message相同；<br/>
 * @author xingxiuyi 2013-1-8
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD, ElementType.METHOD, ElementType.TYPE })
public @interface Labels {
    Label[] value() default {};
}
