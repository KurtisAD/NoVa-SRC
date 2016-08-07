package nova.altmanager;

import java.io.*;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

import org.lwjgl.input.Keyboard;

public class GuiDirectLogin extends GuiScreen
{
	public GuiScreen parent;
	public GuiTextField usernameBox;
	public GuiPasswordField passwordBox;
	
	public GuiDirectLogin(GuiScreen paramScreen)
	{
		this.parent = paramScreen;
	}
	
	public void initGui()
	{
		Keyboard.enableRepeatEvents(true);
		buttonList.add(new GuiButton(1, width / 2 - 100, height / 4 + 96 + 12, "Login"));
		buttonList.add(new GuiButton(2, width / 2 - 100, height / 4 + 96 + 36, "Cancel"));
		usernameBox = new GuiTextField(0, this.fontRendererObj, width / 2 - 100, 76 - 25, 200, 20);
		passwordBox = new GuiPasswordField(this.fontRendererObj, width / 2 - 100, 116 - 25, 200, 20);
		usernameBox.setMaxStringLength(200);
		passwordBox.func_146203_f(200);
	}
	
	public void onGuiClosed()
	{
		Keyboard.enableRepeatEvents(false);
	}
	
	public void updateScreen()
	{
		usernameBox.updateCursorCounter();
		passwordBox.updateCursorCounter();
	}
	
	public void mouseClicked(int x, int y, int b)
	{
		usernameBox.mouseClicked(x, y, b);
		passwordBox.mouseClicked(x, y, b);
		try {
			super.mouseClicked(x, y, b);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void actionPerformed(GuiButton button)
	{
        if (button.id == 1) {
        	YggdrasilAuthenticator auth = new YggdrasilAuthenticator(usernameBox.getText(), passwordBox.getText());
            if (auth.login()) {
                Minecraft.getMinecraft().session = auth.getSession();
            }

        } else if (button.id == 2) {
            Minecraft.getMinecraft().displayGuiScreen(parent);
        }
       
	}
	
	protected void keyTyped(char c, int i)
	{
		usernameBox.textboxKeyTyped(c, i);
		passwordBox.textboxKeyTyped(c, i);
		if(c == '\t')
		{
			if(usernameBox.isFocused())
			{
				usernameBox.setFocused(false);
				passwordBox.setFocused(true);
			}else
			if(passwordBox.isFocused())
			{
				usernameBox.setFocused(false);
				passwordBox.setFocused(false);
			}
		}
		if(c == '\r')
		{
			actionPerformed(buttonList.get(0));
		}
	}
	
	public void drawScreen(int x, int y, float f)
	{
		drawDefaultBackground();
		drawString(this.fontRendererObj, "Username", width / 2 - 100, 63 - 25, 0xA0A0A0);
		drawString(this.fontRendererObj, "\2474*", width / 2 - 106, 63 - 25, 0xA0A0A0);
		drawString(this.fontRendererObj, "Password", width / 2 - 100, 104 - 25, 0xA0A0A0);

		this.fontRendererObj.drawStringWithShadow(
				"Playing as " + Minecraft.getMinecraft().session.getUsername(),
				3, 3, 0xFFFFFF);

		try{
		usernameBox.drawTextBox();
		passwordBox.drawTextBox();
		}catch(Exception err)
		{
			err.printStackTrace();
		}
		super.drawScreen(x, y, f);
	}
}
