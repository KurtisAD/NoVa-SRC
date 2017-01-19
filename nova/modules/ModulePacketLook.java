package nova.modules;

import net.minecraft.client.Minecraft;
import nova.events.EventHandler;
import nova.events.PlayerTickEvent;

/**
 * Created by Skeleton Man on 7/21/2016.
 */

@Deprecated
public class ModulePacketLook extends ModuleBase {
    Long expireTime;

    float yaw, pitch;


    public ModulePacketLook(nova.Nova Nova, Minecraft mc) {
        super(Nova, mc);

        this.isToggleable = false;
        this.description = ("Not togglable, sends packet looking stuff without changing your vision");

        this.expireTime = 0L;
    }

    @EventHandler
    public void onPlayerUpdate(PlayerTickEvent e){
        if (isEnabled){
            if (this.expireTime > System.currentTimeMillis()){
                this.isEnabled = false;
            }
        }
    }

    public void setExpireTime(Long minimumTime){
        this.isEnabled = true;

        Long min = System.currentTimeMillis() + minimumTime;

        this.expireTime = min > expireTime ? min : expireTime;
    }

    public void setLook(){

    }

    @Override
    public String getMetadata(){
        return "(" + Long.toString(System.currentTimeMillis() - expireTime) + ")";
    }
}
