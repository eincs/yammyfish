import java.awt.*;

import javax.swing.*;

public abstract class Fish implements Drawable {
	
	// 물고기의 종류와 관련된 상수 변수
	public static final int VF_PICACHU	 = 0;
	public static final int VF_PIRANHA	 = 1;
	public static final int VF_FOOTBALL	 = 2;
	public static final int VF_CHICKEN	 = 3;
	public static final int VF_NORMAL	 = 4;
	
	
	protected final double maxVelocity;		// 물고기의 최대속력
	protected final double Accelation;		// 물고기의 가속력
	protected final double waterResitance;	// 물고기의 저항(키를 누르지 않으면 속도가 점점 줄어든다)

	protected int status;					// 물고기의 현재 상태
	
	protected Pos location;					// 물고기의 현재 위치
	protected Pos velocity;					// 물고기의 현재 속도
	protected Pos accelation;				// 물고기의 현재 가속도
	
	protected int moveState;
	
	protected Pos colCenter;				// 충돌 계산을 위한 충돌원의 중심
	protected double radius;				// 충돌 계산을 위한 충돌원의 반지름
	
	protected int level;					// 물고기의 레벨
	protected int exp;						// 물고기의 경험치
	
	protected FishSprite imageFish;				// 물고기의 스프라이트를 저장
	
	
	private String message;
	
	public Fish(int startX, int startY,
				double maxVelocity, double Accelation, double waterResitance)
	{
		message = "";
		
		this.maxVelocity = maxVelocity;
		this.Accelation = Accelation;
		this.waterResitance = waterResitance;
		this.imageFish = new FishSprite();
		
		location = new Pos(startX, startY);
		velocity = new Pos(0, 0);
		accelation = new Pos(0, 0);
		
		colCenter = new Pos(startX, startY);
		
		
		moveState = -1;
		
		level = 1;
		exp = 0;
		
		radius= (level*5+5)/1.5;
	}
	public Pos getLocation()
	{
		return location;
	}
	public Pos getcolCenter()
	{
		return colCenter;
	}
	public double getRadius()
	{
		return this.radius;
	}
	public int getLevel()
	{
		return this.level;
	}
	public void setLevel(int level)
	{
		if(!(1<=level && level<=10))
			level = 10;
		this.level = level;
		radius = (level*5+5)/1.5;
		imageFish.setCurLevel(level);
	}
	public void setMoveState(int moveState)
	{
		this.moveState = moveState;
	}
	public boolean isOutOfbound()
	{
		if(!(-200<location.getX()&&location.getX()<1000)
		|| !(-200<location.getY()&&location.getY()<800))
			return true;
		
		return false;
	}
	public boolean isCollition(Fish fish)
	{
		double distance;
		double sumRadius;
		distance = colCenter.getDistance(fish.getcolCenter());
		sumRadius = this.getRadius() + fish.getRadius();
		
		if(distance>sumRadius)
			return false;
		else
			return true;
	}
	public boolean isEatable(Fish fish)
	{
		if(fish.isPlayer())
		{
			if(this.isPlayer())
			{
				if(this.getLevel()-fish.getLevel()>1)
					return true;
				else
					return false;
			}
			else {
				if(this.getLevel()>fish.getLevel())
					return true;
				else
					return false;
			}
		}
		else
		{
			if(this.isPlayer())
			{
				if(this.getLevel()>fish.getLevel())
					return true;
				else
					return false;
			}
			else {
				
				return false;
			}
		}
	}
	private int calcIncExp(Fish eatenFish)
	{
		int subLevel = this.getLevel() - eatenFish.getLevel();
		if(subLevel>10)
			return 1;
		else
			return (10-subLevel+1);
	}
	public void eat(Fish fish) // Fish를 먹었을때의 호출되는 메소드, 화면에 그려주는건 따로 한다.
	{
		if(this.level<9)
		{
			exp += this.calcIncExp(fish);
			if(exp>=100)
			{
				while(exp>=100) {
					exp= exp-100;
					imageFish.setCurLevel(++level);
				}
			}
		}
		radius = (level*5+5)/1.5;
	}
	public void move() // Fish의 가속도와 속도와 위치를 결정하는 메소드
	{
		double fvelocityX;
		double fvelocityY;
		
		double flocateX;
		double flocateY;
		
		// 여기서 부터 가속도의 조정이 일어남
		// moveState에 따라 물고기의 가속도를 조정한다.
		switch(moveState)
		{
		case -1:
			accelation.setValue(0, 0);
			break;
		case 0: // moveState = Up
			accelation.setValue(0, -Accelation);
			break;
		case 1: // moveState = UpRight
			accelation.setValue(Accelation, -Accelation);
			imageFish.setDirection(0);
			break;
		case 2: // moveState = Right
			accelation.setValue(Accelation, 0);
			imageFish.setDirection(0);
			break;
		case 3: // moveState = DownRight
			accelation.setValue(Accelation, Accelation);
			imageFish.setDirection(0);
			break;
		case 4: // moveState = Down
			accelation.setValue(0, Accelation);
			break;
		case 5: // moveState = DownLeft
			accelation.setValue(-Accelation, Accelation);
			imageFish.setDirection(1);
			break;
		case 6: // moveState = Left
			accelation.setValue(-Accelation, 0);
			imageFish.setDirection(1);
			break;
		case 7: // moveState = UpLeft
			accelation.setValue(-Accelation, -Accelation);
			imageFish.setDirection(1);
			break;
	
		}
		
		// 여기서 부터 가속도와 물 저항에 따른 속도값의 변경이 일어남
		// maxVelocity의 값에 따라 가질 수 있는 velocity를 제한한다.
		fvelocityX = velocity.getX()+accelation.getX();
		fvelocityY = velocity.getY()+accelation.getY();
		// 가속도에 따른 속도 변경
	
		
		if(fvelocityX>waterResitance)
		{
			fvelocityX -= waterResitance;
		}
		else if(fvelocityX<-waterResitance)
		{
			fvelocityX += waterResitance;
		}
		else
		{
			fvelocityX = 0;
		}
		if(fvelocityY>waterResitance)
		{
			fvelocityY -= waterResitance;
		}
		else if(fvelocityY<-waterResitance)
		{
			fvelocityY += waterResitance;
		}
		else
		{
			fvelocityY = 0;
		}
		// 물 저항에 따른 속도변경
	
		
		if(fvelocityX>maxVelocity)
		{
			fvelocityX = maxVelocity; 
		}
		else if(fvelocityX<-maxVelocity)
		{
			fvelocityX = -maxVelocity;
			
		}
		if(fvelocityY>maxVelocity)
		{
			fvelocityY = maxVelocity;
		}		
		else if(fvelocityY<-maxVelocity)
		{
			fvelocityY = -maxVelocity;
		}
			
		// 최대 속도에 따른 속도 변경
		
		
		velocity.setValue(fvelocityX, fvelocityY);
		
		// 여기서 부터 새로 설정된  속도값에 따라 위치의 변경이 일어남
		flocateX = location.getX() + velocity.getX();
		flocateY = location.getY() + velocity.getY();
		
		location.setValue(flocateX, flocateY);
		colCenter.setValue(flocateX, flocateY);
		
	}
	public abstract void useAbility();
	public abstract boolean isPlayer();
	public void draw(Graphics g)
	{
		ImageIcon curSprite = this.toImageIcon();
		int imageWidth = curSprite.getIconWidth();
		int imageHeight = curSprite.getIconHeight();
		g.drawImage(curSprite.getImage(), (int)location.getX()-imageWidth/2, (int)location.getY()-imageHeight/2, null);
		g.setColor(Color.white);
		g.setFont(new Font("SansSerif", Font.BOLD, (int)(radius)));
		g.drawString(message, (int)location.getX()-25,(int)location.getY()-(int)radius-10);
	}
	public ImageIcon toImageIcon()
	{
		return imageFish.getSprite();
	}
	public int getType()
	{
		return Fish.VF_NORMAL;
	}
	public void respown(Pos startPoint)
	{
		location.setValue(startPoint.getX(), startPoint.getY());
		velocity.setValue(0, 0);
		accelation.setValue(0, 0);
		setMoveState(-1);
	}
	public void setMessage(String str)
	{
		this.message = str;
	}
}