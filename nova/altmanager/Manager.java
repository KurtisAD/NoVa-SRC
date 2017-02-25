package nova.altmanager;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import net.minecraft.client.gui.GuiScreen;
import nova.Nova;
import nova.util.Util;

import java.io.*;
import java.util.ArrayList;

public class Manager
{
	// TODO: Make it work like every other gui (maybe? having static doesn't really change anything)
	// TODO: encrypt saved passwords because fuck plaintext

	public static ArrayList<Alt> altList = new ArrayList<>();
	public static GuiAltList altScreen = new GuiAltList();
	public static final int slotHeight = 25;

	public static GuiScreen parentScreen;

	public static JsonObject json = new JsonObject();


	public static GuiAltList getAltScreen(GuiScreen parentScreen){
		Manager.parentScreen = parentScreen;
		return altScreen;
	}

	public static void saveAlts()
	{
		
		FileWriter file;
		try {
			file = new FileWriter(Nova.novaDir + File.separator + "alts.nova");
			json.add("altList", Util.getGson().toJsonTree(altList));
			file.write(Util.getGson().toJson(json));
			file.flush();
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void loadAlts()
	{
		try {
			json = new JsonObject();
			json = Util.getGson().fromJson(new JsonReader(new FileReader(Nova.novaDir + File.separator + "alts.nova")), JsonObject.class);

			altList = Util.getGson().fromJson(json.get("altList"),new TypeToken<ArrayList<Alt>>(){}.getType());
			altList = altList == null ? new ArrayList<>() : altList;
		} catch (FileNotFoundException e){
			saveAlts();
		}
	}
}