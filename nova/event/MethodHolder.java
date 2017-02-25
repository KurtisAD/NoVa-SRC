package nova.event;

/**
 * Created by Skeleton Man on 6/18/2016.
 * From V
 */
import java.lang.reflect.Method;

public class MethodHolder {

    private Method method;
    private Object object;

    public MethodHolder(Object o, Method m)
    {
        this.object = o;
        this.method = m;
    }

    public Method getMethod()
    {
        return method;
    }

    public Object getObject()
    {
        return object;
    }

}