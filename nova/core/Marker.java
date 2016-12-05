package nova.core;

import java.awt.*;

public class Marker {
	public Color color;
	public int setting;

    public Marker(Marker marker, int setting) {
        this.color = marker.color;
        this.setting = setting;
    }

	public Marker(Color color, int setting){
		this.color = color;
		this.setting = setting;
	}

	public Marker(int r, int g, int b, int setting){
        this.color = new Color(r, g, b);
        this.setting = setting;
    }
}
