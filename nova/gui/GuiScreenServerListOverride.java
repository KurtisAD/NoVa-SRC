package nova.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenServerList;
import net.minecraft.client.multiplayer.ServerData;

import java.io.IOException;

/**
 * Created by Skeleton Man on 7/17/2016.
 */
public class GuiScreenServerListOverride extends GuiScreenServerList {
    // TODO: Properly implement without changing base class


    public GuiScreenServerListOverride(GuiScreen p_i1031_1_, ServerData p_i1031_2_){
        super(p_i1031_1_, p_i1031_2_);
    }

    protected void actionPerformed(GuiButton button)
            throws IOException
    {
        if (button.enabled) {
            if (button.id == 1)
            {
                this.lastScreen.confirmClicked(false, 0);
            }
            else if (button.id == 0)
            {
                this.serverData.serverIP = this.ipEdit.getText();


                GuiDisconnectedOverride.updateLastServerFromDirectConnect((GuiMultiplayerOverride)this.lastScreen, this.serverData);

                this.lastScreen.confirmClicked(true, 0);
            }
        }
    }
}
