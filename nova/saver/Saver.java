package nova.saver;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import nova.Nova;
import nova.module.ModuleBase;
import nova.util.Util;

import java.io.*;
import java.lang.reflect.Field;

/**
 * @author Kurt Dee
 * @since 11/14/2016
 */
public class Saver {
    // TODO: maybe think of a better way of implementing it?

    public static <T extends ModuleBase> void saveModule(T m) {

        JsonObject json = new JsonObject();

        for (Field f : m.getClass().getFields()) {
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

    public static <T extends ModuleBase> void loadModule(T m) {
        JsonObject json;
        try {
            json = Util.getGson().fromJson(new JsonReader(new FileReader(Nova.novaDir + File.separator + m.name + ".nova")), JsonObject.class);
        } catch (FileNotFoundException e) {
            System.out.println("Module: " + m.getName() + " failed to load, saving default values...");
            saveModule(m);

            return;
        }

        for (Field f : m.getClass().getFields()) {
            if (f.isAnnotationPresent(Saveable.class)) {

                f.setAccessible(true);

                try {
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
