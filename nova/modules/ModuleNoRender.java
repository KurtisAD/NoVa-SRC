package nova.modules;

import net.minecraft.client.Minecraft;
import nova.core.RegisterArgument;
import nova.core.Saveable;
import nova.core.Util;
import nova.events.EntityRenderEvent;
import nova.events.EventHandler;

import java.util.ArrayList;

/**
 * Created by Skeleton Man on 1/12/2017.
 */
public class ModuleNoRender extends ModuleBase {

    @Saveable
    public ArrayList<String> entities;
    @Saveable
    public boolean renderNone;

    public ModuleNoRender(nova.Nova Nova, Minecraft mc) {
        super(Nova, mc);

        this.entities = new ArrayList<>();
        this.renderNone = false;
        this.defaultArg = "add";
        this.description = "Select entites to remove from rendering";
    }

    @EventHandler
    public boolean onEntityRender(EntityRenderEvent e) {
        return !this.isEnabled || !entities.contains(Util.getEntityName(e.getEntity().getClass()));
    }

    @RegisterArgument(name = "valid", description = "Lists valid entites")
    public void valid() {
        // Maybe use Util.join here?
        String ret = "";
        for (String s : Util.getValidEntities().keySet()) {
            ret += s + ", ";
        }

        this.Nova.confirmMessage("You can select:");
        this.Nova.message(ret.substring(0, ret.length() - 2));
    }

    @RegisterArgument(name = "add", description = "Add entity to norender list")
    public void addrgb(String name) {
        if (Util.getValidEntities().containsKey(name.toLowerCase())) {
            this.entities.add(name.toLowerCase());
            this.Nova.confirmMessage("Added " + name + " to norender");
        } else {
            this.Nova.errorMessage("You cannot select that entity; check -norender valid");
        }
    }

    @RegisterArgument(name = "del", description = "Removes entity from norender list")
    public void del(String name) {
        if (this.entities.contains(name.toLowerCase())) {
            this.entities.remove(name.toLowerCase());
            this.Nova.confirmMessage("Deleted " + name);
        } else {
            this.Nova.errorMessage("That entity has not been added");
        }
    }

    @RegisterArgument(name = "list", description = "Lists entities not rendered")
    public void list() {
        String list = Util.join(entities, ", ");
        this.Nova.message(list);
        this.Nova.message("End of norender list.");
    }

    @RegisterArgument(name = "clear", description = "Clears norender list")
    public void clear() {
        this.entities = new ArrayList<>();
        this.Nova.message("Cleared norender list.");
    }

    @RegisterArgument(name = "all", description = "Toggles skips rendering of all entities")
    public void limit() {
        this.renderNone = !this.renderNone;
    }


    @Override
    public String getMetadata() {

        return renderNone ? "(All)" : entities.isEmpty() ? "(Ready)" : "(Active)";
    }
}
