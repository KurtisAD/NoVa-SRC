package nova;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import nova.core.Saveable;
import nova.core.Util;
import nova.modules.ModuleBase;

import java.io.*;
import java.lang.reflect.Field;

/**
 * Created by Skeleton Man on 11/14/2016.
 */
public class Saver {
    // TODO: improve saver so it's private/public independent, maybe think of a better way?

    public static void saveModule(ModuleBase m) {
        //get location in module
        //get savables
        //parse savables to gson
        //save

        JsonObject json = new JsonObject();

        for (Field f : m.getFields()) {
            if (f.isAnnotationPresent(Saveable.class)) {

                checkAccessable(f, m);

                try {
                    json.add(f.getName(), Util.getGson().toJsonTree(f.get(m)));
                } catch (IllegalAccessException e) {
                    System.out.println("Something went terribly wrong in the Module Saver, this shouldn't happen.");
                    e.printStackTrace();
                }
            }
        }

        FileWriter file;
        try {
            file = new FileWriter(Nova.novaDir + File.separator + m.name + ".nova");
            file.write(Util.getGson().toJson(json));
            file.flush();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadModule(ModuleBase m) {
        JsonObject json = new JsonObject();
        try {
            json = Util.getGson().fromJson(new JsonReader(new FileReader(Nova.novaDir + File.separator + m.name + ".nova")), JsonObject.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();

            System.out.println("Module: " + m.getName() + " failed to load, saving default values...");
            saveModule(m);

            return;
        }

        for (Field f : m.getFields()) {
            if (f.isAnnotationPresent(Saveable.class)) {

                checkAccessable(f, m);

                try {
                    // TODO: make this call more effective
                    f.set(m, Util.getGson().fromJson(json.get(f.getName()), f.getType()) == null ? f.get(m) : Util.getGson().fromJson(json.get(f.getName()), f.getType()));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void checkAccessable(Field f, ModuleBase m) {
        // Check if we can access the field, we should be able to, but just in case
        if (!f.isAccessible()) {
            // if we can't access it, we log it so I can fix that shit and set the accessibility to true
            System.out.println("Field " + f.getName() + " of type " + f.getType() + " in class " + m.getName() + " is not accessible.");
            f.setAccessible(true);
        }
    }

}
