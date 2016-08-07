package nova.core;

import java.awt.Color;

public class Marker {
	public Color color;
	public int setting;
	
	public Marker(Color color, int setting){
		this.color = color;
		this.setting = setting;
	}
	
	public Marker(int r, int g, int b, int setting){
		this.color = new Color(r,g,b);
		this.setting = setting;
	}
}
