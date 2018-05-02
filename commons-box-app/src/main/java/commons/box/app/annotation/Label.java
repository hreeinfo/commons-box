package commons.box.app.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 声明消息，消息子串是一个数组，例子："default", "zh_CN=adfasdfadsf"<br/>
 * 如果声明了key，那么该key应用于该字段，如果value为空则仅仅是变更了声明，如果不为空表示该健值的消息同时会应用到消息系统中<br/>
 * 注意：如果要更改key定义必须使用Message标签，如果要定义多个扩展值或者额外定义消息但不应用，可使用Messages组合<br/>
 * @author xingxiuyi 2012-10-20
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD, ElementType.METHOD, ElementType.TYPE })
public @interface Label {
    String[] value() default {};

    /**
     * 为空则使用类名加字段名的形式，<br/>
     * 不为空则表示该字段使用key所声明的值，如果key以+开头则表示使用默认值并扩展该值：<br/>
     * 例如：name字段默认为com.hreeinfo.dto.Uesr.name，<br/>
     * 当key为op.othername的时候，查找消息op.othername
     * 当key为+othername的时候，查找消息com.hreeinfo.dto.Uesr.name.othername
     * @return key
     */
    String key() default "";
}
