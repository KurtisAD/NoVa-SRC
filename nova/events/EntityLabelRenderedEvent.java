package nova.events;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;

public class EntityLabelRenderedEvent {

	public EntityLivingBase entity;
	public double interpolationX, interpolationY, interpolationZ;
    public boolean canRenderName;
    public RenderManager rm;

    public EntityLabelRenderedEvent(EntityLivingBase e, double interpolationX, double interpolationY, double interpolationZ, boolean canRenderName, RenderManager rm) {
        this.entity = e;
		this.interpolationX = interpolationX;
		this.interpolationY = interpolationY;
		this.interpolationZ = interpolationZ;
        this.canRenderName = canRenderName;
        this.rm = rm;
    }
}
