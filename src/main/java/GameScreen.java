import java.awt.*;
import javax.swing.*;
import java.awt.image.*;

public class GameScreen extends JPanel{

	private static final long serialVersionUID = 1L;
	
	private BufferedImage ScrBuffer;
	
	private Background background;
	private FishManager fishManager;
	private GameEventManager eventManager;
	
	private int gameMode;

	public GameScreen(Dimension screenSize)
	{
		this.setPreferredSize(screenSize);
		ScrBuffer = new BufferedImage((int)screenSize.getWidth(), (int)screenSize.getHeight(), BufferedImage.TYPE_INT_RGB);
		gameMode = FishEater.GAME_BEGINNING;
		background = new Background();
	}
	public void setEventManager(GameEventManager eventManager)
	{
		this.eventManager = eventManager;
	}
	public void reset()
	{
		gameMode = FishEater.GAME_BEGINNING;
		background.setMode(Background.PRN_LOGO);
		this.repaint();
		
	}
	public void setManager(FishManager manager)
	{
		fishManager=manager;
	}
	public void gameStart(int gameMode)
	{
		this.gameMode = gameMode;
		background.setMode(Background.PRN_BACKGROUND);
		this.repaint();
	}
	public void gameEnd()
	{
		gameMode=FishEater.GAME_END;
	}
	public void paintComponent(Graphics page)	//버퍼에 그린후 화면에 전체 출력
	{
		
		//page.setColor(Color.blue);
		//super.paintComponent(page);
		if(gameMode == FishEater.GAME_RUN_1P || 
				gameMode== FishEater.GAME_RUN_2P || gameMode ==FishEater.GAME_END) {
			Graphics g = ScrBuffer.getGraphics();
			g.setColor(Color.blue);
			g.fillRect(0, 0, 800, 600);
			background.draw(g);
			fishManager.allDraw(g);
			g.dispose();
			
			
			page.drawImage(ScrBuffer, 0, 0, Color.white, this);
		}
		else
			background.draw(page);

			
		
	}
}
