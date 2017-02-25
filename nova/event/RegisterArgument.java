package nova.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Skeleton Man on 8/25/2016.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)


public @interface RegisterArgument {
    String name() default "UNNAMED";

    String description() default "NO DESCRIPTION";
}