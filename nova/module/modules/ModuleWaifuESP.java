package nova.module.modules;

import net.minecraft.block.material.MapColor;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.MapData;
import nova.Nova;
import nova.event.RegisterArgument;
import nova.module.ModuleBase;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;


/**
 * Created by Skeleton Man on 1/7/2017.
 */
public class ModuleWaifuESP extends ModuleBase {
    // This will eventually port over to the fake inventory class


    public ModuleWaifuESP() {
        super();
        this.isToggleable = false;
        this.aliases.add("waifu");
        this.description = ("Shows your waifu, save waifus from URL, or load them from the Nova folder.");
    }

    @RegisterArgument(name = "load", description = "Loads waifu from file, enter waifu name")
    public void loadWaifu(String name) {
        new ModuleWaifuESPTask(Nova.novaDir + File.separator + name, false).run();
    }

    @RegisterArgument(name = "fetch", description = "Gets waifu from URL")
    public void fetchWaifu(String url) {
        new ModuleWaifuESPTask(url, true).run();
    }

    public void getWaifu(BufferedImage bi) {
        ItemStack theMap = ItemMap.func_190906_a(mc.world, Double.MAX_VALUE, Double.MAX_VALUE, (byte) 1, false, false);
        theMap.setStackDisplayName("Do it for her.");

        MapData md = ((ItemMap) theMap.getItem()).getMapData(theMap, mc.world);


        // Getting RGB values from the image
        C3 colors[] = new C3[16384];
        for (int x = 0; x < 128; x++) {
            for (int y = 0; y < 128; y++) {
                colors[(y << 7) + x] = new C3(bi.getRGB(x, y));
            }
        }

        // Floyd-Steinberg Dithering
        // Source: http://stackoverflow.com/questions/5940188/how-to-convert-a-24-bit-png-to-3-bit-png-using-floyd-steinberg-dithering
        for (int x = 0; x < 128; x++) {
            for (int y = 0; y < 128; y++) {
                C3 oldColor = colors[(y << 7) + x];
                int mapCode = getClosetColor(oldColor.toRGB());
                C3 newColor = new C3(MapColor.COLORS[mapCode >> 2].getMapColor(mapCode & 0x3));
                md.colors[(y << 7) + x] = (byte) mapCode;

                C3 err = oldColor.sub(newColor);

                if (x + 1 < 128) colors[(y << 7) + x + 1] = colors[(y << 7) + x + 1].add(err.mul(7. / 16));
                if (x - 1 >= 0 && y + 1 < 128)
                    colors[(y + 1 << 7) + x - 1] = colors[(y + 1 << 7) + x - 1].add(err.mul(3. / 16));
                if (y + 1 < 128) colors[(y + 1 << 7) + x] = colors[(y + 1 << 7) + x].add(err.mul(5. / 16));
                if (x + 1 < 128 && y + 1 < 128)
                    colors[(y + 1 << 7) + x + 1] = colors[(y + 1 << 7) + x + 1].add(err.mul(1. / 16));
            }
        }


        // Saving the map data, may need to be tweaked
        this.mc.entityRenderer.func_190564_k(); // reset data

        mc.world.setItemData("map_" + theMap.getMetadata(), md);
        mc.entityRenderer.getMapItemRenderer().updateMapTexture(md);

        mc.player.inventory.mainInventory.set(mc.player.inventory.currentItem, theMap);
    }

    private class ModuleWaifuESPTask implements Runnable {
        String path;
        boolean isUrl;


        ModuleWaifuESPTask(String path, boolean isUrl) {
            this.path = path;
            this.isUrl = isUrl;
        }

        @Override
        public void run() {
            try {
                BufferedImage bi = isUrl ? ImageIO.read(new URL(path)) : ImageIO.read(new File(path));
                if (bi.getHeight() != 128 || bi.getWidth() != 128) {
                    Nova.errorMessage("Image file height and width do not match map size (" + bi.getWidth() + "," + bi.getHeight() + ") instead of (128,128)");
                    return;
                }
                getWaifu(bi);
            } catch (IOException e) {
                Nova.errorMessage("Could not open: " + path);
            }
        }
    }

    int getClosetColor(int colorIn) {
        double distance = 450;
        int pos = 0;
        for (int j = 1; j < 36; j++) {
            for (int k = 0; k < 4; k++) {
                double newDist = distanceInt(colorIn, MapColor.COLORS[j].getMapColor(k));
                if (newDist < distance) {
                    distance = newDist;
                    pos = ((j << 2) + k);

                    if ((byte) pos < 0) {
                        int i = 0;
                    }
                }
            }
        }

        return pos;
    }

    double distanceInt(int color1, int color2) {
        return distance((color1 >> 16) & 0xFF, (color1 >> 8) & 0xFF, color1 & 0xFF, (color2 >> 16) & 0xFF, (color2 >> 8) & 0xFF, color2 & 0xFF);
    }

    double distance(int r1, int g1, int b1, int r2, int g2, int b2) {
        return Math.sqrt(Math.pow(r1 - r2, 2) + Math.pow(g1 - g2, 2) + Math.pow(b1 - b2, 2));
    }

    static class C3 {
        int r, g, b;

        public C3(int c) {
            Color color = new Color(c);
            this.r = color.getRed();
            this.g = color.getGreen();
            this.b = color.getBlue();
        }

        public C3(int r, int g, int b) {
            this.r = r;
            this.g = g;
            this.b = b;
        }

        public C3 add(C3 o) {
            return new C3(r + o.r, g + o.g, b + o.b);
        }

        public C3 sub(C3 o) {
            return new C3(r - o.r, g - o.g, b - o.b);
        }

        public C3 mul(double d) {
            return new C3((int) (d * r), (int) (d * g), (int) (d * b));
        }

        public int diff(C3 o) {
            return Math.abs(r - o.r) + Math.abs(g - o.g) + Math.abs(b - o.b);
        }

        public int toRGB() {
            return toColor().getRGB();
        }

        public Color toColor() {
            return new Color(clamp(r), clamp(g), clamp(b));
        }

        public int clamp(int c) {
            return Math.max(0, Math.min(255, c));
        }
    }
}
