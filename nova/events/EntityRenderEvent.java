package nova.events;

import net.minecraft.entity.Entity;

/**
 * Created by Skeleton Man on 1/12/2017.
 */
public class EntityRenderEvent {
    private Entity entity;

    public EntityRenderEvent(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }
}
