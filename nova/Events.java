package nova;

import nova.events.EventHandler;
import nova.modules.ModuleBase;
import nova.events.ChatSentEvent;

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
    Nova Nova;
    Map<Class, ArrayList<MethodHolder>> handlers;

    public Events(Nova Nova) {
        this.Nova = Nova;
        handlers = new HashMap<>();
        getEventHandlers();
    }

    private void getEventHandlers() {
        ArrayList<MethodHolder> tmp;

        for (ModuleBase module : Nova.modules) {
            for (Method m : module.getClass().getDeclaredMethods()) {
                if (m.isAnnotationPresent(EventHandler.class)) {
                    Class<?>[] params = m.getParameterTypes();

                    if (params.length == 1) {

                        if (params[0].getPackage().getName().endsWith("events")) {
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

    /*
     * @return is canceled
     */
    public boolean onEvent(Object o)
    {

        boolean isCancelled = false;

        try {

            Class type = o.getClass();
            Object res = null;

            if(this.handlers.get(type) == null)
            {
                return false;
            }

            for(MethodHolder mh : this.handlers.get(type))
            {
                res = mh.getMethod().invoke(mh.getObject(), type.cast(o));

                if(res != null)
                    if(!((Boolean)res).booleanValue())
                        isCancelled = true;
            }


        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return isCancelled;
    }

    /**
     * @return  if message started with delimter
     */
    public boolean onChat(String message){
        if(message.startsWith(StaticNova.Nova.delimeter)){
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



        for(ModuleBase m : Nova.modules) {
            if(m.aliases.contains(argv.get(0))) {
                m.command.parseArgs(argv.subList(1, argv.size()).toArray(new String[argv.size() - 1]));
                return true;
            }
        }

        return false;
    }

    // pretty much a place holder, tick event fucks up due to player being set to null
    // this will eventually be removed with better logic, just a quickfix
    /*
    public void onTick(){
        if(Nova.mc.thePlayer == null){
            return;
        }
        onEvent(new TickEvent());
    }
    */
}
