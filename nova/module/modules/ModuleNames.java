package nova.module.modules;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import nova.Nova;
import nova.event.RegisterArgument;
import nova.module.ModuleBase;
import nova.util.Util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Skeleton Man on 12/20/2016.
 */
public class ModuleNames extends ModuleBase {
    // TODO: colorcode names again
    JsonParser parser = new JsonParser();

    public ModuleNames() {
        super();
        this.isToggleable = false;

        this.description = "Returns name history from current ingame name, player need not be online. \u00A72Green\u00A7r names are current, \u00A79blue\u00A7r names are open, \u00A74red\u00A7r names are taken, and \u00A76gold\u00A7r is Legacy. WARNING: name requests are capped at 600 every 10 minutes, don't spam use.";
        this.defaultArg = "get";
    }

    private enum NameType {
        LEGACY,
        CURRENT,
        OPEN,
        TAKEN
    }

    @RegisterArgument(name = "get", description = "Get name history for current name")
    public void getNames(String name) {
        try {
            JsonElement json = getJsonFromName(name);
            NameObject names[] = Util.getGson().fromJson(json, NameObject[].class);

            Nova.message("Name history for " + name + ":");
            for (NameObject n : names) {
                Nova.message("  >" + n.getName());
            }
            Nova.message("End of name history.");
        } catch (IOException e) {
            Nova.errorMessage("IOException thrown.");
            e.printStackTrace();
        }
    }

    private JsonElement getJsonFromName(String name) throws IOException {
        long unixTime = System.currentTimeMillis();
        URL url = null;

        url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name + "?at=" + unixTime);

        String json = getSourceFromURL(url);

        // maybe make it return a tuple with the enum
        String uuid = Util.getGson().fromJson(parser.parse(json).getAsJsonObject().get("id"), String.class);
        // holder

        url = new URL("https://api.mojang.com/user/profiles/" + uuid + "/names");

        return parser.parse(getSourceFromURL(url));
    }

    // maybe move this to Util?
    private String getSourceFromURL(URL url) throws IOException {

        InputStreamReader isr = null;
        URLConnection urlConnection = url.openConnection();
        InputStream is = urlConnection.getInputStream();
        isr = new InputStreamReader(is);


        int numCharsRead;
        char[] charArray = new char[1024];
        StringBuilder sb = new StringBuilder();
        while ((numCharsRead = isr.read(charArray)) > 0) {
            sb.append(charArray, 0, numCharsRead);
        }
        String result = sb.toString();

        isr.close();

        return result;
    }

    private class NameObject {
        private String name;
        private long changedToAt;

        public String getName() {
            return this.name;
        }

        public long getChangedToAt() {
            return this.changedToAt;
        }
    }
}
