package nova.module.modules;

import nova.Nova;
import nova.event.EventHandler;
import nova.event.RegisterArgument;
import nova.event.events.EntityRenderEvent;
import nova.module.ModuleBase;
import nova.saver.Saveable;
import nova.util.Util;

import java.util.ArrayList;

/**
 * Created by Skeleton Man on 1/12/2017.
 */
public class ModuleNoRender extends ModuleBase {

    @Saveable
    public ArrayList<String> entities;
    @Saveable
    public boolean renderNone;

    public ModuleNoRender() {
        super();

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
        Nova.confirmMessage("You can select:");
        Nova.message(Util.join(Util.getValidEntities().keySet(), ", "));
    }

    @RegisterArgument(name = "add", description = "Add entity to norender list")
    public void addrgb(String name) {
        if (Util.getValidEntities().containsKey(name.toLowerCase())) {
            this.entities.add(name.toLowerCase());
            Nova.confirmMessage("Added " + name + " to norender");
        } else {
            Nova.errorMessage("You cannot select that entity; check -norender valid");
        }
    }

    @RegisterArgument(name = "del", description = "Removes entity from norender list")
    public void del(String name) {
        if (this.entities.contains(name.toLowerCase())) {
            this.entities.remove(name.toLowerCase());
            Nova.confirmMessage("Deleted " + name);
        } else {
            Nova.errorMessage("That entity has not been added");
        }
    }

    @RegisterArgument(name = "list", description = "Lists entities not rendered")
    public void list() {
        String list = Util.join(entities, ", ");
        Nova.message(list);
        Nova.message("End of norender list.");
    }

    @RegisterArgument(name = "clear", description = "Clears norender list")
    public void clear() {
        this.entities = new ArrayList<>();
        Nova.message("Cleared norender list.");
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
