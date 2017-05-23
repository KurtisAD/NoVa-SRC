package nova.saver;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Skeleton Man on 11/14/2016.
 */

// Should only be used on public fields
// TODO: this kinda breaks encapsulation, so maybe implement a better saver later?
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Saveable {
}
