package nova.module.modules;

import net.minecraft.entity.player.EntityPlayer;
import nova.Nova;
import nova.event.EventHandler;
import nova.event.RegisterArgument;
import nova.event.events.PlayerEnterVisualRangeEvent;
import nova.event.events.PlayerLeaveVisualRangeEvent;
import nova.event.events.PlayerLogOffEvent;
import nova.event.events.PlayerLogOnEvent;
import nova.module.ModuleBase;
import nova.saver.Saveable;

/**
 * Created by Skeleton Man on 1/8/2017.
 */
public class ModuleNotifications extends ModuleBase {

    // TODO: properly rename logoffloc
    // BUG: leave visual range events fire twice, maybe optifine bug?

    @Saveable
    public boolean visualRange;
    @Saveable
    public boolean onLog;
    @Saveable
    public boolean logOffLoc;

    public ModuleNotifications() {
        super();

        this.aliases.add("note");
        this.aliases.add("notify");

        this.visualRange = true;
        this.onLog = true;
        this.logOffLoc = true;


        this.description = "Notifies you of server events";
    }

    @RegisterArgument(name = "see", description = "Toggles if you get a notification when a player enters or leaves your visual range")
    public void see() {
        this.visualRange = !this.visualRange;
    }

    @RegisterArgument(name = "log", description = "Toggles if you see a notification when a player logs in or out")
    public void log() {
        this.onLog = !this.onLog;
    }

    @RegisterArgument(name = "loc", description = "Say where a player logged off, currently broken, will tell you where last rendered location is")
    public void loc() {
        this.logOffLoc = !logOffLoc;
    }

    @EventHandler
    public void onPlayerLogIn(PlayerLogOnEvent e) {
        if (this.isEnabled && this.onLog) {
            if (!e.getUsername().equals(mc.player.getDisplayName().getUnformattedText()))
                Nova.message("\247e" + e.getUsername() + " joined the game");
        }
    }

    @EventHandler
    public void onPlayerLogOff(PlayerLogOffEvent e) {
        if (this.isEnabled) {
            if (!e.getUsername().equals(mc.player.getDisplayName().getUnformattedText())) {
                if (this.onLog)
                    Nova.message("\247e" + e.getUsername() + " left the game");


                if (this.logOffLoc) {
                    EntityPlayer p = mc.world.getPlayerEntityByName(e.getUsername());
                    if (p != null)
                        Nova.message(p.getDisplayName().getUnformattedText() + " logged off at (" + p.posX + ", " + p.posY + ", " + p.posZ + ")");
                }
            }
        }
    }

    @EventHandler
    public void onPlayerEnterVisualRange(PlayerEnterVisualRangeEvent e) {
        if (this.isEnabled && this.visualRange) {
            if (!e.getPlayer().getDisplayName().getUnformattedText().equals(mc.player.getDisplayName().getUnformattedText()))
                Nova.notificationMessage("\2474\247l!!!\247r " + e.getPlayer().getDisplayName().getUnformattedText() + " entered visual range");
        }
    }

    @EventHandler
    public void onPlayerLeaveVisualRange(PlayerLeaveVisualRangeEvent e) {
        if (this.isEnabled && !e.getPlayer().getDisplayName().getUnformattedText().equals(mc.player.getDisplayName().getUnformattedText())) {
            {
                EntityPlayer p = e.getPlayer();

                if (this.visualRange)
                    Nova.notificationMessage("\247b\247l...\247r " + p.getDisplayName().getUnformattedText() + " left visual range");
                if (this.logOffLoc) {
                    if (e.getPlayer().getHealth() == 0D) {
                        Nova.message("\2476" + p.getName() + " has died at (" + Math.round(p.posX) + ", " + Math.round(p.posY) + ", " + Math.round(p.posZ) + ")");
                    } else {
                        String format = (mc.player.connection.getPlayerInfo(p.getName()) == null) ? " logged off at (" : " last seen at (";

                        String message = " \u00A72!!! " + p.getDisplayName().getUnformattedText() + format + Math.round(p.posX) + ", " + Math.round(p.posY) + ", " + Math.round(p.posZ) + ")";
                        message += ((ModuleESP) Nova.getModule(ModuleESP.class)).healthEsp ? " health:\u00A74" + String.format("%.2f", p.getHealth()) + "\u00A7r" : "";
                        message += " \u00A72!!! ";
                        Nova.message(message);
                    }
                }
            }
        }
    }

    @Override
    public String getMetadata() {
        if (!this.visualRange && !this.onLog && !this.logOffLoc)
            return "";


        String ret = "(";
        ret += this.visualRange ? "See, " : "";
        ret += this.onLog ? "Log, " : "";
        ret += this.logOffLoc ? "Loc, " : "";

        if (ret.endsWith(", "))
            ret = ret.substring(0, ret.length() - 2);

        return ret + ")";
    }

}
