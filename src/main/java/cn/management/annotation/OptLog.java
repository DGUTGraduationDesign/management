package cn.management.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 操作日志自定义注解
 * package: cn.management.annotation
 * project: management
 * </p>
 *
 * @author ZhouJiaKai <zhoujk@pvc123.com>
 * @version v1.0.0
 * @since v1.0.p>
 * date 2018/6/19 9:06
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OptLog {

    String msg() default "";

}
