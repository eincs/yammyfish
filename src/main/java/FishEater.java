import java.awt.*;
import javax.swing.*;
import java.applet.AudioClip;
import java.net.URL;
import java.awt.event.*;
import java.util.Random;
import java.io.*;

public class FishEater extends JFrame
{
	
	private static final long serialVersionUID = 1L;
	
	// ������ ���ۿ� �ʿ��� �����
	private final int GAME_SPEED = 30;
	private final int DELAY = 1000/GAME_SPEED;
	private final int SCREEN_WIDTH = 800, SCREEN_HEIGHT = 600;
	
	// GameState�� �� ��� ��
	public static final int GAME_BEGINNING = 0;
	public static final int GAME_RUN_1P	 = 1;			// ���Ŀ�  ȥ�ڼ� �ϱ� ��嵵 ��������
	public static final int GAME_RUN_2P	 = 2;
	public static final int GAME_END		 = 3;
	
	private Timer gameRunTimer;						// ���� RUN�� �ʿ��� Ÿ�̸�
	private Random rand = new Random();

	private GameScreen gameScreen;					// ���� ȭ���� ����� ��ũ��
	private FishManager fishes;								// ����⸦ ó���� �Ŵ��� Ŭ����
	private GameEventManager eventManager;
	
	private int gameState;							// ���� ���ӻ��¸� ��Ÿ���� ����
	
	//private AudioClip music;
	//private File file;
	
	public FishEater()
	{
		super("Yammy Fish");
		
		/*
		URL url = null;
		file = new File("music\\back.wav");
		try {
			url = new URL("file", "localhost", file.getPath());
		}
		catch(Exception e) {}
		music = JApplet.newAudioClip(url);
		
		music.loop();
		*/
		// frame�� Ư���� ����
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		
		// frame�� GameScreen�� �޴���  �߰�
		fishes = new FishManager(); 
		gameScreen = new GameScreen(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		gameScreen.setManager(fishes);
		this.getContentPane().add(gameScreen);
		this.setJMenuBar(new GameMenuBar());
		this.pack();
		
				eventManager = new GameEventManager(this, fishes, gameScreen);
		
		fishes.setEventManager(eventManager);
		gameScreen.setEventManager(eventManager);
		
		gameRunTimer = new Timer(DELAY, new GameRunTimer());
		gameLoad();

		// frame�� ��ġ�� ȭ���� �� ����� ����
		Dimension ScrSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = this.getSize();
		int frameX = (int)(ScrSize.getWidth()-frameSize.getWidth())/2;
		int frameY = (int)(ScrSize.getHeight()-frameSize.getHeight())/2;
		this.setLocation(frameX, frameY);
		
		this.setVisible(true);
	}
	public void gameLoad()
	{
		gameRunTimer.restart();
		gameState = GAME_BEGINNING;	
	}
	public void gameRun(int gameMode)
	{
		gameState = gameMode;
		this.addKeyListener(new FishKeyListener(fishes.getPlayer1Fish(), KeyEvent.VK_UP, KeyEvent.VK_RIGHT, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_ENTER));
		if(gameState==GAME_RUN_2P)
			this.addKeyListener(new FishKeyListener(fishes.getPlayer2Fish(), KeyEvent.VK_W, KeyEvent.VK_D, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_Z));
		
		gameRunTimer.start();

	}
	public void gameEnd()
	{
		gameState = GAME_END;
	}
	private class GameRunTimer implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if(gameState==GAME_RUN_1P || gameState==GAME_RUN_2P || gameState == GAME_END) {
				fishes.allMove();
				gameScreen.repaint();
			}			
		}
	}
	public class FishKeyListener extends KeyAdapter{
	// �÷��̾� ����⸦ Ű����� �����Ҽ� �ְ� ���ִ� KeyListener
		
		private int[] keySettings = new int[5];		// ��Ʈ�� Ű�� ������ �迭
		private int keyState = 0x0000;				// ������ Ű�� ���¸� ����
		private Fish controlFish;					// �� �����ʷ� ��Ʈ���� ����⸦ ����
		
		/*
		 *  KeyState�� 16���� ���·� �����Ѵ�
		 *  ���ڸ��� 16������, MSB���� LSB���� ���ʴ��
		 *  F�� ����� ���: Up, Right, Down, Left Ű�� ������ �����̰�,
		 *  0�� ����� ���: Up, Right, Down, Left Ű�� �������� ����  �����̴�.
		 *  ��Ʈ�����ڸ� �̿��Ͽ� �� ����� KeyState�� �����Ѵ�
		 */
		
		public FishKeyListener(Fish controlUnit, int KeyUp, int KeyRight, int KeyDown, int KeyLeft, int KeyAbility)
		// ��Ʈ�� �� ������ �� Ű ���� �޾� �����Ѵ�
		{
			controlFish = controlUnit;
			
			keySettings[0] = KeyUp;
			keySettings[1] = KeyRight;
			keySettings[2] = KeyDown;
			keySettings[3] = KeyLeft;
			keySettings[4] = KeyAbility;
		}
		public void keyPressed(KeyEvent e)
		{
			int pressedKey = e.getKeyCode();
			
			if(pressedKey == keySettings[0]) // Up
			{
				keyState = keyState | 0xF000;
			}
			else if(pressedKey == keySettings[1]) // Right
			{
				keyState = keyState | 0x0F00;
			}
			else if(pressedKey == keySettings[2]) // Down
			{
				keyState = keyState | 0x00F0;
			}
			else if(pressedKey == keySettings[3]) // Left
			{
				keyState = keyState | 0x000F;
			}
			else if(pressedKey == keySettings[4]) // Ability
			{
				
			}
			setMoveState();
			
		}
		public void keyReleased(KeyEvent e)
		{
			int pressedKey = e.getKeyCode();
			
			if(pressedKey == keySettings[0]) // Up
			{
				keyState = keyState & 0x0FFF;
			}
			else if(pressedKey == keySettings[1]) // Right
			{
				keyState = keyState & 0xF0FF;
			}
			else if(pressedKey == keySettings[2]) // Down
			{
				keyState = keyState & 0xFF0F;
			}
			else if(pressedKey == keySettings[3]) // Left
			{
				keyState = keyState & 0xFFF0;
			}
			else if(pressedKey == keySettings[4]) // Ability
			{
				
			}
			setMoveState();
		}
		private void setMoveState()
		// ���� Ű ���¿� ���� controlFish�� MoveState�� �����Ѵ�
		{
			// moveState info
			// Stop -1, Up 0, UpRight 1, Right 2,
			// DownRight 3, Down 4, DownLeft 5, Left 6, UpLeft 7
			switch(keyState)
			{
			case 0x0000:
				controlFish.setMoveState(-1);	// Stop
					break;
			case 0xF000:
				controlFish.setMoveState(0);	// Up
				break;
			case 0x0F00:
				controlFish.setMoveState(2);	// Right
				break;
			case 0x00F0:
				controlFish.setMoveState(4);	// Down
				break;
			case 0x000F:
				controlFish.setMoveState(6);	// Left
				break;
			case 0xFF00:
				controlFish.setMoveState(1);	// UpRight
				break;
			case 0xF00F:
				controlFish.setMoveState(7);	// UpLeft
				break;
			case 0xF0F0:
				controlFish.setMoveState(-1);	// Stop
				break;
			case 0x0FF0:
				controlFish.setMoveState(3);	// downRight
				break;
			case 0x00FF:
				controlFish.setMoveState(5);	// downLeft
				break;
			case 0x0F0F:
				controlFish.setMoveState(-1);	// Stop
				break;
			case 0xFFF0:
				controlFish.setMoveState(-1);	// Stop
				break;
			case 0xFF0F:
				controlFish.setMoveState(-1);	// Stop
				break;
			case 0xF0FF:
				controlFish.setMoveState(-1);	// Stop
				break;
			case 0x0FFF:
				controlFish.setMoveState(-1);	// Stop
				break;
			case 0xFFFF:
				controlFish.setMoveState(-1);	// Stop
				break;
			}
		}
	}
	public class GameMenuBar extends JMenuBar {
		
		private static final long serialVersionUID = 1L;
		public GameMenuBar()
		{
			this.add(new Game());
			this.add(new Help());
		}
		private class Game extends JMenu
		{
			private static final long serialVersionUID = 1L;
			public Game()
			{
				super("Game");
				this.setMnemonic('G');
					
				JMenuItem[] gameMenu = new JMenuItem[5];
				gameMenu[0] = new JMenuItem("Play Alone");
				gameMenu[1] = new JMenuItem("Play Together");
				
				gameMenu[2] = new JMenuItem("Satistic");
				gameMenu[3] = new JMenuItem("Settings");
				
				gameMenu[4] = new JMenuItem("Exit");
				
				gameMenu[0].setEnabled(false);
				gameMenu[2].setEnabled(false);
				gameMenu[3].setEnabled(false);
				gameMenu[4].setMnemonic('x');
				
				gameMenu[0].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
				gameMenu[1].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0));
				gameMenu[2].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0));
				gameMenu[3].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));

				int menuNumber = gameMenu.length;
				for(int i=0; i<menuNumber; i++)
				{
					gameMenu[i].addActionListener(new MenuListener());
				}
				this.add(gameMenu[0]);
				this.add(gameMenu[1]);
				this.addSeparator();
				this.add(gameMenu[2]);
				this.add(gameMenu[3]);
				this.addSeparator();
				this.add(gameMenu[4]);
				
			}
		}
		private class Help extends JMenu
		{
			private static final long serialVersionUID = 1L;
			public Help(){
				
				super("Help");
				this.setMnemonic('G');
					
				JMenuItem[] gameMenu = new JMenuItem[2];
				gameMenu[0] = new JMenuItem("Help");
				this.addSeparator();
				gameMenu[1] = new JMenuItem("About Game");
				
				gameMenu[0].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
				gameMenu[0].setEnabled(false);
				gameMenu[1].setEnabled(false);
				this.add(gameMenu[0]);
				this.add(gameMenu[1]);
			}
		}
		private class MenuListener implements ActionListener
		{
			public void actionPerformed(ActionEvent e)
			{
				if(e.getActionCommand() == "Exit")
					System.exit(1);
				else if(e.getActionCommand() == "Play Alone")
					eventManager.throwEvent(new GameStartEvent(GameEvent.GAME_START_1P, Math.abs(rand.nextInt()%4), 0));
				else if(e.getActionCommand() == "Play Together")
					if(gameState == FishEater.GAME_RUN_1P || gameState == FishEater.GAME_RUN_2P || gameState == FishEater.GAME_END)
						eventManager.throwEvent(new GameSetEvent());
					else
						eventManager.throwEvent(new GameStartEvent(GameEvent.GAME_START_2P, Math.abs(rand.nextInt()%4), Math.abs(rand.nextInt()%4)));
			}
		}
	}
}
