package nova.event.events;

import net.minecraft.client.gui.GuiScreen;
import nova.event.Event;

/**
 * @author Kurt Dee
 * @since 7/17/2016
 */
public class GuiOpenEvent implements Event {
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
