package nova.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.Util.EnumOS;
import nova.modules.ModuleBase;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Skeleton Man on 6/22/2016.
 */
public class Util {
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    //TODO: implement minecraft code for this, no need to rewrite
    public static File getAppDir(String par0Str) {
        String var1 = System.getProperty("user.home", ".");
        File var2;

        switch (getOs()) {
            case LINUX:
            case SOLARIS:
                var2 = new File(var1, '.' + par0Str + '/');
                break;

            case WINDOWS:
                String var3 = System.getenv("APPDATA");

                if (var3 != null) {
                    var2 = new File(var3, "." + par0Str + '/');
                } else {
                    var2 = new File(var1, '.' + par0Str + '/');
                }

                break;

            case OSX:
                var2 = new File(var1, "Library/Application Support/" + par0Str);
                break;

            default:
                var2 = new File(var1, par0Str + '/');
        }

        if (!var2.exists() && !var2.mkdirs()) {
            throw new RuntimeException("The working directory could not be created: " + var2);
        } else {
            return var2;
        }
    }

    public static EnumOS getOs()
    {
        String var0 = System.getProperty("os.name").toLowerCase();
        return var0.contains("win") ? EnumOS.WINDOWS : (var0.contains("mac") ? EnumOS.OSX : (var0.contains("solaris") ? EnumOS.SOLARIS : (var0.contains("sunos") ? EnumOS.SOLARIS : (var0.contains("linux") ? EnumOS.LINUX : (var0.contains("unix") ? EnumOS.LINUX : EnumOS.UNKNOWN)))));
    }

    public static void saveModule(ModuleBase module){
        FileWriter file;
        try {
            file = new FileWriter(module.filepath);
            file.write(gson.toJson(module));
            file.flush();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    /*
     * Must only be called when NoV is intialized
     */
    /*
    public static ModuleBase loadModule(ModuleBase module, Nova Nova, Minecraft mc){
        try {
            module = gson.fromJson(new JsonReader(new FileReader(module.filepath)),module.getClass());
            module.initializeNonsavables(Nova, mc);
            return module;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        saveModule(module);
        return module;
    }
*/
    public static String capitalize(String str)
    {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static String formatArmorDurability(double percent)
    {
        percent = Math.ceil(percent);
        boolean red = percent < 10.0D;
        String color = red ? "\247c" : "";
        if(percent == 100)
            return color + Integer.toString((int)percent) + "\247r";

        if(percent >= 10)
            return color + "0" + Integer.toString((int)percent) + "\247r";

        return color + "00" + Integer.toString((int)percent) + "\247r";
    }

    public static String hash(String str)
    {
        long hash = 0L;

        hash = 2166136261L;
        for(char c : str.toCharArray())
        {
            hash *= 16777619L;
            hash ^= c;
        }


        return Long.toHexString(hash).substring(8);
    }

    public static String toFull(String s)
    {
        String ret = "";
        char[] c = s.toCharArray();

        for(char h : c)
        {

            if(h != ' ')
                ret += (char)((h | 0x10000) + 0xFFEE0);
            else
                ret += ' ';
        }

        System.out.print(ret);

        return ret;
    }

    public static String getItemNameAndEnchantments(ItemStack is) {
        if(is != null) {
            if(is.isItemEnchanted()) {
                NBTTagList e = is.getEnchantmentTagList();
                String enchants = "";
                int i, i1, i2;
                for(i = 0; i < e.tagCount(); i++) {
                    // ??????
                    i1 = ((NBTTagCompound)e.getCompoundTagAt(i)).getShort("id");
                    i2 = ((NBTTagCompound)e.getCompoundTagAt(i)).getShort("lvl");
                    enchants = enchants + (i == 0 ? "" : ", ") + Enchantment.getEnchantmentByID(i1).getTranslatedName(i2);
                }
                return  is.getDisplayName() + " (" + Integer.toString(is.stackSize) + "/" + Integer.toString(is.getMaxStackSize()) + ")" +  " [" + enchants +"]";
            } else
                return is.getDisplayName() + " (" + Integer.toString(is.stackSize) + "/" + Integer.toString(is.getMaxStackSize()) + ")";

        } else
            return "Nothing";
    }

    public static Gson getGson(){
        return gson;
    }

    public static String join(ArrayList<String> strs, String glue) {
        String ret = "";
        for(int i = 0; i < strs.size(); i++) {
            if(i > strs.size() - 2) {
                ret += strs.get(i);
            } else {
                ret += strs.get(i) + glue;
            }
        }
        return ret;
    }

    public static double distance(Location pos, double x, double z){
        return Math.sqrt(Math.pow((x - pos.x),2) + Math.pow((z - pos.z),2));
    }
}
