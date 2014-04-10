import java.awt.*;
import java.util.*;
import javax.swing.*;

public class Sprite implements Drawable {

	protected Vector<ImageIcon> sprite;
		
	public Sprite()
	{
		sprite = new Vector<ImageIcon>();
	}
	
	public ImageIcon getSprite(int idx)
	{
		// idx = 0 : Right, idx = 1 : Left
		return sprite.elementAt(idx);
	}
	public ImageIcon getSprite()
	{
		return sprite.elementAt(0);
	}
	public ImageIcon toImageIcon()
	{
		return sprite.elementAt(0);
	}
	public void draw(Graphics g)
	{
		
	}
}
