package nova.event;

import nova.Nova;
import nova.event.events.ChatSentEvent;
import nova.module.ModuleBase;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Skeleton Man on 6/18/2016.
 */
public class Events {
    Map<Class, ArrayList<MethodHolder>> handlers;

    public Events() {
        handlers = new HashMap<>();
        getEventHandlers();
    }

    private void getEventHandlers() {
        ArrayList<MethodHolder> tmp;

        for (ModuleBase module : Nova.getModules()) {
            for (Method m : module.getClass().getDeclaredMethods()) {
                if (m.isAnnotationPresent(EventHandler.class)) {
                    Class<?>[] params = m.getParameterTypes();

                    if (params.length == 1) {

                        if (Event.class.isAssignableFrom(params[0])) {
                            if (this.handlers.containsKey(params[0])) {
                                this.handlers.get(params[0]).add(new MethodHolder(module, m));
                            } else {
                                tmp = new ArrayList<>();
                                tmp.add(new MethodHolder(module, m));
                                this.handlers.put(params[0], tmp);

                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * @return is canceled
     */
    // Does this actually return if canceled?
    public boolean onEvent(Event o)
    {

        boolean isCancelled = false;

        try {

            Class type = o.getClass();
            Object res;

            if(this.handlers.get(type) == null)
            {
                return false;
            }

            for(MethodHolder mh : this.handlers.get(type))
            {
                res = mh.getMethod().invoke(mh.getObject(), type.cast(o));

                if(res != null)
                    if (!(Boolean) res)
                        isCancelled = true;
            }


        } catch (IllegalArgumentException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return isCancelled;
    }

    /**
     * @return  if message started with delimter
     */
    public boolean onChat(String message){
        if (message.startsWith(Nova.delimiter)) {
            onCommand(message.substring(1));
            return true;
        }
        return onEvent(new ChatSentEvent(message));
    }

    public boolean onCommand(String command)
    {
        if(command.length() < 1)
            return false;

        List<String> argv = new ArrayList<String>();
        Matcher ma = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(command);
        while (ma.find())
            argv.add(ma.group(1).replace("\"", ""));


        for (ModuleBase m : Nova.getModules()) {
            if (m.getAliases().contains(argv.get(0))) {
                m.command.parseArgs(argv.subList(1, argv.size()).toArray(new String[argv.size() - 1]));
                return true;
            }
        }

        return false;
    }
}
