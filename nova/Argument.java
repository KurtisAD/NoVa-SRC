package nova;


import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.concurrent.Callable;

/**
 * Created by Skeleton Man on 6/18/2016.
 */

public class Argument {

    private String id;
    public Class sets[];
    private Command c;
    private Method method;
    private ArrayList<String> errors;
    String usage;
    private Callable ca;

    public Argument(Command c, String id, Method method, String use){
        this.c = c;
        this.id = id;
        this.sets = method.getParameterTypes();
        this.method = method;
        this.errors = new ArrayList<String>();

        this.usage = "of format: \247o";

        for(Class clazz : sets)
        {
            this.usage += clazz.getSimpleName() + " ";
        }

        this.usage += "\247r\n" + use;    }

    public Object[] matches(String argv[])
    {

        this.errors.clear();

        if(sets == null || sets.length == 0)
            return new Object[]{};

        Object toReturn[]= new Object[argv.length];
        for(int i = 0; i < argv.length; i++) {

            try {
                Object o = sets[i];
                if (sets[i].getName().equals("boolean")) {
                    toReturn[i] = Boolean.parseBoolean(argv[i]);
                } else if(sets[i].getName().equals("char")) {
                    toReturn[i] = argv[i].charAt(0);
                } else if(sets[i].getName().equals("java.lang.String")) {
                    toReturn[i] = argv[i];
                } else if(sets[i].getName().equals("int")) {
                    toReturn[i] = Integer.parseInt(argv[i]);
                } else if(sets[i].getName().equals("float")) {
                    toReturn[i] = Float.parseFloat(argv[i]);
                } else if(sets[i].getName().equals("double")) {
                    toReturn[i] = Double.parseDouble(argv[i]);
                } else {
                    return null;
                }

            } catch(ClassCastException e) {
                this.c.Nova.errorMessage("Invalid type on argument " + i + "; should be of type: " + sets[i].getSimpleName());
                return null;
            } catch(NumberFormatException e) {
                this.c.Nova.errorMessage("Invalid type on argument " + i + "; should be of type: " + sets[i].getSimpleName());
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

