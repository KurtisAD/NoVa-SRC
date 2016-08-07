package nova.events;

import net.minecraft.client.gui.GuiScreen;

/**
 * Created by Skeleton Man on 7/17/2016.
 */
public class GuiOpenEvent {
    private GuiScreen gui;
    public GuiOpenEvent(GuiScreen gui)
    {
        this.setGui(gui);
    }

    public GuiScreen getGui()
    {
        return gui;
    }

    public void setGui(GuiScreen gui)
    {
        this.gui = gui;
    }

}
