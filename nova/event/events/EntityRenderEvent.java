package nova.event.events;

import net.minecraft.entity.Entity;
import nova.event.Event;

/**
 * @author Kurt Dee
 * @since 1/12/2017
 */
public class EntityRenderEvent implements Event {
    private Entity entity;

    public EntityRenderEvent(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }
}
