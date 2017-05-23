package nova.module.modules;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import nova.event.EventHandler;
import nova.event.RegisterArgument;
import nova.event.events.LivingUpdateEvent;
import nova.event.events.PacketReceivedEvent;
import nova.module.ModuleBase;
import nova.saver.Saveable;

/**
 * Created by Skeleton Man on 7/21/2016.
 */
public class ModuleEntityRide extends ModuleBase {
    // TODO: expand this module to all entities

    @Saveable
    public float speed;
    @Saveable
    public float jump;


    public ModuleEntityRide() {
        super();

        this.aliases.add("horse");
        this.description = ("Can ride all horses");

        this.defaultArg = "setspeed";

        this.speed = 0.3375f;
        this.jump = 1.0f;
    }

    @RegisterArgument(name = "setjump", description = "Sets the horse's jump")
    public void setJump(float jump) {
        this.jump = jump;
    }

    @RegisterArgument(name = "setspeed", description = "Sets the horse's speed")
    public void setSpeed(float speed) {
        this.speed = speed;
    }


    @EventHandler
    public void onLivingUpdate(LivingUpdateEvent e) // this event may be placed wrong
    {
        if (this.isEnabled) {
            if (mc.player.isRiding() && mc.player.getRidingEntity() instanceof EntityAnimal) {
                ((EntityAnimal) mc.player.getRidingEntity()).getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(speed);
            }
            if (mc.player.isRidingHorse()) {
                mc.player.horseJumpPower = 1.0F;
                ((AbstractHorse) mc.player.getRidingEntity()).getEntityAttribute(AbstractHorse.JUMP_STRENGTH).setBaseValue(jump);
            }
        }

    }

    @EventHandler
    public boolean onPacketRecieve(PacketReceivedEvent e) {
        if (this.isEnabled && mc.player != null && mc.player.isRiding()) {
            if (e.getPacket() instanceof SPacketEntityVelocity && ((SPacketEntityVelocity) e.getPacket()).getEntityID() == mc.player.getRidingEntity().getEntityId()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String getMetadata(){
        return "(Jump: " + jump + " Speed: " + speed + ")";
    }
}
