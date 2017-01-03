package nova.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.network.LanServerInfo;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;

import java.io.IOException;

/**
 * Created by Skeleton Man on 7/17/2016.
 */
public class GuiDisconnectedOverride extends GuiDisconnected {
    // TODO: Properly implement without changing base class

    private int autoReconnectTimer;
    private static ServerListEntryNormal lastServer;


    public GuiDisconnectedOverride(GuiScreen screen, String reasonLocalizationKey, ITextComponent chatComp){
        super(screen, reasonLocalizationKey, chatComp);
    }

    public static void updateLastServerFromServerList(GuiListExtended.IGuiListEntry entry, GuiMultiplayer guiMultiplayer)
    {
        if ((entry instanceof ServerListEntryNormal))
        {
            ServerData serverData = ((ServerListEntryNormal)entry).getServerData();

            lastServer = new ServerListEntryNormal(guiMultiplayer, serverData);
        }
        else if ((entry instanceof ServerListEntryLanDetected))
        {
            LanServerInfo lanServer = ((ServerListEntryLanDetected) entry).getServerData();

            lastServer = new ServerListEntryNormal(guiMultiplayer, new ServerData("LAN-Server", lanServer.getServerIpPort(), true));
        }
    }

    public static void updateLastServerFromDirectConnect(GuiMultiplayer guiMultiplayer, ServerData serverData)
    {
        lastServer = new ServerListEntryNormal(guiMultiplayer, serverData);
    }

    private void reconnectToLastServer(GuiScreen parentScreen)
    {
        if (lastServer != null)
        {
            Minecraft mc = Minecraft.getMinecraft();
            mc.displayGuiScreen(new GuiConnecting(parentScreen, mc, lastServer.getServerData()));
        }
    }

    public void initGui()
    {
        this.buttonList.clear();
        this.multilineMessage = this.fontRendererObj.listFormattedStringToWidth(this.message.getFormattedText(), this.width - 50);
        this.textHeight = (this.multilineMessage.size() * this.fontRendererObj.FONT_HEIGHT);
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 2 + this.textHeight / 2 + this.fontRendererObj.FONT_HEIGHT, I18n.format("gui.toMenu", new Object[0])));


        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 2 + this.textHeight / 2 + this.fontRendererObj.FONT_HEIGHT + 24, "Reconnect"));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 2 + this.textHeight / 2 + this.fontRendererObj.FONT_HEIGHT + 48, "AutoReconnect"));
        if (GuiMultiplayerOverride.autoReconnect) {
            this.autoReconnectTimer = 100;
        }
    }

    public void updateScreen()
    {
        super.updateScreen();
        if (GuiMultiplayerOverride.autoReconnect)
        {
            if (this.buttonList.size() < 2) {
                return;
            }
            this.buttonList.get(2).displayString = ("AutoReconnect (" + (this.autoReconnectTimer / 20 + 1) + ")");
            if (this.autoReconnectTimer > 0) {
                this.autoReconnectTimer -= 1;
            } else {
                try
                {
                    actionPerformed(this.buttonList.get(1));
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        else
        {
            this.buttonList.get(2).displayString = "AutoReconnect";
        }
    }

    protected void actionPerformed(GuiButton button)
            throws IOException
    {
        switch (button.id)
        {
            case 0:
                this.mc.displayGuiScreen(new GuiMultiplayer(new GuiMainMenu()));
                break;
            case 1:
                reconnectToLastServer(this);
                break;
            case 2:
                GuiMultiplayerOverride.autoReconnect = !GuiMultiplayerOverride.autoReconnect;
                if (GuiMultiplayerOverride.autoReconnect) {
                    this.autoReconnectTimer = 100;
                }
                break;
        }
    }

}
