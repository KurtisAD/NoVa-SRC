package nova.event;


import nova.Nova;

import java.lang.reflect.Method;

/**
 * Created by Skeleton Man on 6/18/2016.
 */

public class Argument {

    private String id;
    private Class sets[];
    private Method method;
    private String usage;

    public Argument(String id, Method method, String use) {
        this.id = id;
        this.sets = method.getParameterTypes();
        this.method = method;

        this.usage = "of format: \247o";

        for(Class clazz : sets)
        {
            this.usage += clazz.getSimpleName() + " ";
        }

        this.usage += "\247r\n" + use;    }

    public Object[] matches(String argv[])
    {

        if(sets == null || sets.length == 0)
            return new Object[]{};

        Object toReturn[]= new Object[argv.length];
        for(int i = 0; i < argv.length; i++) {

            try {
                if (sets[i] == boolean.class) { // TODO: Is boolean ever used? Consider removing
                    toReturn[i] = Boolean.parseBoolean(argv[i]);
                } else if (sets[i] == char.class) {
                    toReturn[i] = argv[i].charAt(0);
                } else if (sets[i] == String.class) {
                    toReturn[i] = argv[i];
                } else if (sets[i] == int.class) {
                    toReturn[i] = Integer.parseInt(argv[i]);
                } else if (sets[i] == float.class) {
                    toReturn[i] = Float.parseFloat(argv[i]);
                } else if (sets[i] == double.class) {
                    toReturn[i] = Double.parseDouble(argv[i]);
                } else {
                    return null;
                }

            } catch (ClassCastException | NumberFormatException e) {
                Nova.errorMessage("Invalid type on argument " + i + "; should be of type: " + sets[i].getSimpleName());
                return null;
            }

        }

        return toReturn;
    }

    public String getId()
    {
        return this.id;
    }

    public int getArgc()
    {
        return sets.length;
    }

    public String getUsage()
    {
        return this.usage;
    }

    public Method getMethod() {
        return method;
    }
}

