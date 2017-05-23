package nova.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.Util.EnumOS;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Kurt Dee
 * @since 6/22/2016
 */
public class Util {
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static Map<String, Class> validEntities = addEntities();


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

    public static EnumOS getOs() {
        String var0 = System.getProperty("os.name").toLowerCase();
        return var0.contains("win") ? EnumOS.WINDOWS : (var0.contains("mac") ? EnumOS.OSX : (var0.contains("solaris") ? EnumOS.SOLARIS : (var0.contains("sunos") ? EnumOS.SOLARIS : (var0.contains("linux") ? EnumOS.LINUX : (var0.contains("unix") ? EnumOS.LINUX : EnumOS.UNKNOWN)))));
    }

    public static String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static String formatArmorDurability(double percent) {
        percent = Math.ceil(percent);
        boolean red = percent < 10.0D;
        String color = red ? "\247c" : "";
        if(percent == 100)
            return color + Integer.toString((int)percent) + "\247r";

        if(percent >= 10)
            return color + "0" + Integer.toString((int)percent) + "\247r";

        return color + "00" + Integer.toString((int)percent) + "\247r";
    }

    public static String hash(String str) {
        long hash = 2166136261L;

        for(char c : str.toCharArray()) {
            hash *= 16777619L;
            hash ^= c;
        }


        return Long.toHexString(hash).substring(8);
    }

    /**
     * Converts a string of text into its fullwidth equivalent
     *
     * @param s The string of normal characters to convert
     * @return The fullwidth string
     */
    public static String toFull(String s) {
        String ret = "";
        char[] c = s.toCharArray();

        for(char h : c) {

            if(h != ' ')
                ret += (char)((h | 0x10000) + 0xFFEE0);
            else
                ret += ' ';
        }

        System.out.print(ret);

        return ret;
    }

    public static String getItemNameAndEnchantments(ItemStack is) {
        if (is != ItemStack.field_190927_a) {
            if(is.isItemEnchanted()) {
                NBTTagList e = is.getEnchantmentTagList();
                String enchants = "";
                int i, i1, i2;
                for(i = 0; i < e.tagCount(); i++) {
                    // ??????
                    i1 = e.getCompoundTagAt(i).getShort("id");
                    i2 = e.getCompoundTagAt(i).getShort("lvl");
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

    /**
     * Concatenates a collection of String objects with a string in between each object
     *
     * @param strs The collection of String objects to connect
     * @param glue The string to connect each object with
     * @return The concatenated string
     */
    public static String join(Collection<String> strs, String glue) {
        return join(strs.toArray(new String[0]), glue);
    }

    /**
     * Concatenates an array of String objects with a string in between each object
     *
     * @param strsArray The array of String objects to connect
     * @param glue      The string to connect each object with
     * @return The concatenated string
     */
    public static String join(String[] strsArray, String glue) {
        String ret = "";
        for(int i = 0; i < strsArray.length; i++) {
            if (i > strsArray.length - 2) {
                ret += strsArray[i];
            } else {
                ret += strsArray[i] + glue;
            }
        }
        return ret;
    }


    /**
     * Sets the value of a private field, usually used for packets
     *
     * @param classToAccess The class of the instance
     * @param instance      The instance of the class to modify
     * @param value         The value to set the field to
     * @param fieldNames    The field names to set, multiple names in the case of obfuscation
     */
    public static <T, E> void setPrivateValue(Class<? super T> classToAccess, T instance, E value, String... fieldNames) {
        try {
            findField(classToAccess, fieldNames).set(instance, value);
        } catch (Exception e) {
            //throw new UnableToAccessFieldException(fieldNames, e);
        }
    }

    private static Field findField(Class<?> clazz, String... fieldNames) {
        Exception failed = null;
        for (String fieldName : fieldNames) {
            try {
                Field f = clazz.getDeclaredField(fieldName);
                f.setAccessible(true);
                return f;
            } catch (Exception e) {
                failed = e;
            }
        }
        return null;
        //throw new UnableToFindFieldException(fieldNames, failed);
    }

    public static Map<String, Class> getValidEntities() {
        return validEntities;
    }

    // TODO: rename all this shit properly
    private static void addToValidEntities(Map<String, Class> mapIn, Class entityClass) {
        mapIn.put(getEntityName(entityClass), entityClass);
    }

    public static String getEntityName(Class entityClass) {
        return entityClass.getSimpleName().replaceFirst("Entity", "").toLowerCase();
    }

    private static Map<String, Class> addEntities() {
        Map<String, Class> toReturn = new HashMap<>();

        addToValidEntities(toReturn, EntityDragon.class);
        addToValidEntities(toReturn, EntityWither.class);

        addToValidEntities(toReturn, EntityArmorStand.class);
        addToValidEntities(toReturn, EntityBoat.class);
        addToValidEntities(toReturn, EntityEnderCrystal.class);
        addToValidEntities(toReturn, EntityEnderEye.class);
        addToValidEntities(toReturn, EntityEnderPearl.class);
        addToValidEntities(toReturn, EntityExpBottle.class);
        addToValidEntities(toReturn, EntityFallingBlock.class);
        addToValidEntities(toReturn, EntityFireworkRocket.class);
        addToValidEntities(toReturn, EntityItem.class);
        addToValidEntities(toReturn, EntityItemFrame.class);
        addToValidEntities(toReturn, EntityMinecartChest.class);
        addToValidEntities(toReturn, EntityMinecartCommandBlock.class);
        addToValidEntities(toReturn, EntityMinecartEmpty.class);
        addToValidEntities(toReturn, EntityMinecartFurnace.class);
        addToValidEntities(toReturn, EntityMinecartHopper.class);
        addToValidEntities(toReturn, EntityMinecartMobSpawner.class);
        addToValidEntities(toReturn, EntityMinecartTNT.class);
        addToValidEntities(toReturn, EntityPainting.class);
        addToValidEntities(toReturn, EntityTNTPrimed.class);
        addToValidEntities(toReturn, EntityXPOrb.class);

        addToValidEntities(toReturn, EntityBlaze.class);
        addToValidEntities(toReturn, EntityCaveSpider.class);
        addToValidEntities(toReturn, EntityCreeper.class);
        addToValidEntities(toReturn, EntityElderGuardian.class);
        addToValidEntities(toReturn, EntityEnderman.class);
        addToValidEntities(toReturn, EntityEndermite.class);
        addToValidEntities(toReturn, EntityEvoker.class);
        addToValidEntities(toReturn, EntityGhast.class);
        addToValidEntities(toReturn, EntityGiantZombie.class);
        addToValidEntities(toReturn, EntityGuardian.class);
        addToValidEntities(toReturn, EntityHusk.class);
        addToValidEntities(toReturn, EntityIronGolem.class);
        addToValidEntities(toReturn, EntityMagmaCube.class);
        addToValidEntities(toReturn, EntityPigZombie.class);
        addToValidEntities(toReturn, EntityPolarBear.class);
        addToValidEntities(toReturn, EntityShulker.class);
        addToValidEntities(toReturn, EntitySilverfish.class);
        addToValidEntities(toReturn, EntitySkeleton.class);
        addToValidEntities(toReturn, EntitySlime.class);
        addToValidEntities(toReturn, EntitySnowman.class);
        addToValidEntities(toReturn, EntitySpider.class);
        addToValidEntities(toReturn, EntityStray.class);
        addToValidEntities(toReturn, EntityVex.class);
        addToValidEntities(toReturn, EntityVindicator.class);
        addToValidEntities(toReturn, EntityWitch.class);
        addToValidEntities(toReturn, EntityWitherSkeleton.class);
        addToValidEntities(toReturn, EntityZombie.class);
        addToValidEntities(toReturn, EntityZombieVillager.class);

        addToValidEntities(toReturn, EntityBat.class);
        addToValidEntities(toReturn, EntityChicken.class);
        addToValidEntities(toReturn, EntityCow.class);
        addToValidEntities(toReturn, EntityDonkey.class);
        addToValidEntities(toReturn, EntityHorse.class);
        addToValidEntities(toReturn, EntityLlama.class);
        addToValidEntities(toReturn, EntityMooshroom.class);
        addToValidEntities(toReturn, EntityMule.class);
        addToValidEntities(toReturn, EntityOcelot.class);
        addToValidEntities(toReturn, EntityPig.class);
        addToValidEntities(toReturn, EntityRabbit.class);
        addToValidEntities(toReturn, EntitySheep.class);
        addToValidEntities(toReturn, EntitySkeletonHorse.class);
        addToValidEntities(toReturn, EntitySquid.class);
        addToValidEntities(toReturn, EntityVillager.class);
        addToValidEntities(toReturn, EntityWolf.class);
        addToValidEntities(toReturn, EntityZombieHorse.class);

        addToValidEntities(toReturn, EntityOtherPlayerMP.class);

        addToValidEntities(toReturn, EntityDragonFireball.class);
        addToValidEntities(toReturn, EntityEgg.class);
        addToValidEntities(toReturn, EntityEvokerFangs.class);
        addToValidEntities(toReturn, EntityFishHook.class);
        addToValidEntities(toReturn, EntityLargeFireball.class);
        addToValidEntities(toReturn, EntityLlamaSpit.class);
        addToValidEntities(toReturn, EntityPotion.class);
        addToValidEntities(toReturn, EntityShulkerBullet.class);
        addToValidEntities(toReturn, EntitySmallFireball.class);
        addToValidEntities(toReturn, EntitySnowball.class);
        addToValidEntities(toReturn, EntitySpectralArrow.class);
        addToValidEntities(toReturn, EntityTippedArrow.class);
        addToValidEntities(toReturn, EntityWitherSkull.class);

        return toReturn;
    }

}
