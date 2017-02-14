package nova.modules;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.AbstractHorse;
import nova.core.EventHandler;
import nova.core.RegisterArgument;
import nova.core.Saveable;
import nova.events.LivingUpdateEvent;

/**
 * Created by Skeleton Man on 7/21/2016.
 */
public class ModuleHorseRide extends ModuleBase{
    // TODO: expand this module to all entities
    @Saveable
    public boolean horseJump;
    @Saveable
    public boolean shouldSpeed;
    /*
    @Saveable
    public boolean jesus;
    */
    @Saveable
    public float speed;
    @Saveable
    public float jump;


    public ModuleHorseRide() {
        super();

        this.aliases.add("horse");
        this.description = ("Can ride all horses");

        this.defaultArg = "setspeed";

        this.speed = 0.3375f;
        this.jump = 1.0f;

        this.horseJump = true;
        this.shouldSpeed = true;
        //this.jesus = true;
    }

    @RegisterArgument(name = "jump", description = "Should modify jump on horses")
    public void toggleJump(){
        this.horseJump = !this.horseJump;
    }

    @RegisterArgument(name = "setjump", description = "Sets the horse's jump")
    public void setJump(float jump) {
        this.jump = jump;
    }

    @RegisterArgument(name = "speed", description = "Should modify speed on horses")
    public void toggleShouldSpeed() {
        this.shouldSpeed = !this.shouldSpeed;
    }

    @RegisterArgument(name = "setspeed", description = "Sets the horse's speed")
    public void setSpeed(float speed) {
        this.speed = speed;
    }

    /*
    @RegisterArgument(name = "jesus", description = "Makes horses walk on liquids")
    public void toggleJesus(){
        this.jesus = !jesus;
    }
    */

    @EventHandler
    public void onLivingUpdate(LivingUpdateEvent e) // this event may be placed wrong
    {
        if (this.isEnabled && mc.player.isRidingHorse()) {
            mc.player.horseJumpPower = 1.0F;
            if (horseJump) {
                ((AbstractHorse) mc.player.getRidingEntity()).getEntityAttribute(AbstractHorse.JUMP_STRENGTH).setBaseValue(jump);
            }
            if (shouldSpeed) {
                ((AbstractHorse) mc.player.getRidingEntity()).getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(speed);
            }

            if (mc.player.getRidingEntity().isInWater()) {
                // mc.player.getRidingEntity().posY = Math.floor(mc.player.getRidingEntity().posY)+1;
            }
        }
    }

    @Override
    public String getMetadata(){
        return (horseJump ? "(Jump: " + jump + ") " : "") + (shouldSpeed ? "(Speed: " + speed + ") " : "") /*+ (jesus ? "(Jesus)" : "")*/;
    }
}
