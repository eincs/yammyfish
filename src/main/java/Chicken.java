import javax.swing.*;

public class Chicken extends Fish {

	public Chicken(Pos startPoint)
	{
		super((int)startPoint.getX(), (int)startPoint.getY(), 5, 0.5, 0.05);
		this.setLevel(5);
		
		this.imageFish = new FishSprite();
			
		imageFish.addImageIcon(new ImageIcon("img\\fish\\Chicken\\Chicken_3_right.png"));
		imageFish.addImageIcon(new ImageIcon("img\\fish\\Chicken\\Chicken_3_left.png"));
		imageFish.addImageIcon(new ImageIcon("img\\fish\\Chicken\\Chicken_3_right.png"));
		imageFish.addImageIcon(new ImageIcon("img\\fish\\Chicken\\Chicken_3_left.png"));
		
		imageFish.addImageIcon(new ImageIcon("img\\fish\\Chicken\\Chicken_3_right.png"));
		imageFish.addImageIcon(new ImageIcon("img\\fish\\Chicken\\Chicken_3_left.png"));
		
		imageFish.addImageIcon(new ImageIcon("img\\fish\\Chicken\\Chicken_4_right.png"));
		imageFish.addImageIcon(new ImageIcon("img\\fish\\Chicken\\Chicken_4_left.png"));
		
		imageFish.addImageIcon(new ImageIcon("img\\fish\\Chicken\\Chicken_5_right.png"));
		imageFish.addImageIcon(new ImageIcon("img\\fish\\Chicken\\Chicken_5_left.png"));
		
		imageFish.addImageIcon(new ImageIcon("img\\fish\\Chicken\\Chicken_6_right.png"));
		imageFish.addImageIcon(new ImageIcon("img\\fish\\Chicken\\Chicken_6_left.png"));
		
		imageFish.addImageIcon(new ImageIcon("img\\fish\\Chicken\\Chicken_7_right.png"));
		imageFish.addImageIcon(new ImageIcon("img\\fish\\Chicken\\Chicken_7_left.png"));
		
		imageFish.addImageIcon(new ImageIcon("img\\fish\\Chicken\\Chicken_8_right.png"));
		imageFish.addImageIcon(new ImageIcon("img\\fish\\Chicken\\Chicken_8_left.png"));
		
		imageFish.addImageIcon(new ImageIcon("img\\fish\\Chicken\\Chicken_9_right.png"));
		imageFish.addImageIcon(new ImageIcon("img\\fish\\Chicken\\Chicken_9_left.png"));
		
	}
	public void useAbility()
	{
		
	}
	public boolean isPlayer()
	{
		return true;
	}
	public boolean isOutOfbound()
	{
		if(!(0<location.getX()&&location.getX()<800)
		|| !(0<location.getY()&&location.getY()<600))
			return true;
				
		return false;
	}
	public void move()
	{
		super.move();
		
		double flocateX;
		double flocateY;
		
		flocateX = location.getX();
		flocateY = location.getY();
		
		if(flocateX<0)
			flocateX=800;
		else if(flocateX>800)
			flocateX=0;
		
		if(flocateY<0)
			flocateY=0;
		else if(flocateY>600)
			flocateY=600;
		
		location.setValue(flocateX, flocateY);
		colCenter.setValue(flocateX, flocateY);
	}
	public int getType()
	{
		return Fish.VF_CHICKEN;
	}
}
