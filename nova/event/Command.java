package nova.event;

import nova.Nova;
import nova.module.ModuleBase;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Skeleton Man on 6/18/2016.
 */
public class Command {
    private ModuleBase module;
    public Map<String, Argument> args;

    public Command(ModuleBase module)
    {
        this.module = module;
        this.args = new HashMap<>();

        getRegisteredArguments();
    }


    private void getRegisteredArguments() {

        for (Method m : this.module.getClass().getDeclaredMethods()) {
            if (m.isAnnotationPresent(RegisterArgument.class)) {
                RegisterArgument ra = m.getAnnotation(RegisterArgument.class);
                args.put(ra.name(), new Argument(ra.name(), m, ra.description()));
            }
        }
    }

    public boolean parseArgs(String argv[])
    {
        Argument arg;
        String subarray[];
        boolean foundFlag = false;

        if(args.size() == 0 && argv.length > 0)
        {
            Nova.errorMessage("This command has no arguments (clich\247 sad face)");
            return false;
        }

        for(int i = 0; i < argv.length; i++) {

            if(args.containsKey(argv[i])) {
                foundFlag = true;
                arg = args.get(argv[i]);

                if(arg.getArgc() > argv.length - (i + 1)) {
                    Nova.errorMessage("Not enough arguments for flag: " + argv[i] + "; calls for " + arg.getArgc() + " arguments");
                    return false;
                }

                subarray = Arrays.copyOfRange(argv, i + 1, i + 1 + arg.getArgc());

                if(!shouldInvoke(arg,arg.matches(subarray))) {
                    return false;
                }

                i += arg.getArgc();
            }
        }

        if(!foundFlag && argv.length > 0)
        {
            // If no flag was found, use the default flag. This is absolutely bloat.

            if(this.module.defaultArg.equals(""))
            {
                Nova.errorMessage("Invalid argument");
                return false;
            }

            arg = args.get(this.module.defaultArg);

            if(arg.getArgc() > argv.length)
            {
                Nova.errorMessage("Not enough arguments for flag; calls for " + arg.getArgc() + " arguments");
                return false;
            }

            subarray = Arrays.copyOfRange(argv, 0, arg.getArgc());

            if(!shouldInvoke(arg,arg.matches(subarray))) {
                return false;
            }

        } else if(!foundFlag) {
            if(this.module.isToggleable)
            {
                this.module.toggleState();
            } else {
                Nova.errorMessage("You have to provide arguments for this command");
                return false;
            }
        }

        this.module.saveModule();
        return true;
    }

    private boolean shouldInvoke(Argument arg, Object... param){
        if (param == null){
            return false;
        }

        try {
            arg.getMethod().invoke(module, param);
        } catch (IllegalAccessException e) {
            Nova.errorMessage("IllegalAccessException when invoking method on command.");
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            Nova.errorMessage("InvocationTargetException when invoking method on command.");
            e.printStackTrace();
        }
        return true;
    }
}
