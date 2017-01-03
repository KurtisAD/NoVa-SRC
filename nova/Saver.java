package nova;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
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

                f.setAccessible(true);

                try {
                    json.add(f.getName(), Util.getGson().toJsonTree(f.get(m)));
                } catch (IllegalAccessException e) {
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
        JsonObject json;
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

                f.setAccessible(true);

                try {
                    // TODO: make this call more effective
                    f.set(m, Util.getGson().fromJson(json.get(f.getName()), f.getGenericType()) == null ?
                            f.get(m) : Util.getGson().fromJson(json.get(f.getName()), f.getGenericType()));

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (JsonSyntaxException e) {
                    System.out.println("JsonSyntaxException at Module " + m.getName() + " field " + f.getName() + "; saving module with default settings.");
                    saveModule(m);
                    return;
                }
            }
        }
    }

}
