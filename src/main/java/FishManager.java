import java.util.Random;
import java.awt.event.*;
import javax.swing.Timer;
import java.util.Vector;
import java.awt.*;

/* 
 * ��ǻ�� ������ �÷��̾� ����⸦ �����ϴ� ����� �Ŵ��� Ŭ����
 * ������ ���õ� ��� ����� �� Ŭ������ ���� �Ͼ��
 * ������� �ν��Ͻ��� ����� �͵� �� Ŭ������ ���� �Ͼ��.
*/

public class FishManager {

	// ����� �������� ���õ� ��� ����
	private static final int MAX_FISH = 20;	// �ִ�� ����� �� �� �ִ� ��ǻ��  ������� ��.
	private static final int MIN_FISH = 10;
	private static final int RESPOWN_DELAY = 1000;
	private static final int RESPOWN_MAXVELOCITY= 2;

	// ����� ������ ��ġ�� ���õ� ��� ����
	private static final int SCREEN_WIDTH = 800, SCREEN_HEIGHT = 600;
	private static final int UP_RESPOWN = 0+75;
	private static final int DOWN_RESPOWN = SCREEN_HEIGHT-75;
	private static final int RIGHT_RESPOWN = SCREEN_WIDTH+100;
	private static final int LEFT_RESPOWN = 0-100;

	public static final Pos StartPoint1 = new Pos(600, 300);
	public static final Pos StartPoint2 = new Pos(200, 300);
	
	// ����� ���� ������ �����ϴ� ����
	private Vector<Fish> FishList; // ����� ��ü�� ������ ���� �ڷᱸ��
	private Player playerFish1;
	private Player playerFish2;
	
	// ��ǻ�� �������� �������� �����ϴ� ����
	private Random rand = new Random();
	private Timer respownTimer;
	
	private int curFishNumber;
	private int curLevel;
	// ���� �÷��̾��� ���� �����ϴ� ����
	// 1p, 2p�� ���� ��忡 ���� �۵��Ѵ�.
	
	// ���� �̺�Ʈ ó���� ���� �̺�Ʈ �Ŵ���
	private GameEventManager eventManager;
	private int gameMode;
	
	public FishManager()
	{
		// ��ǻ�� �������� ������ ���͸� �����Ѵ�
		FishList = new Vector<Fish>();
		gameMode = FishEater.GAME_BEGINNING;
	}
	public void setEventManager(GameEventManager eventManager)
	{
		this.eventManager = eventManager;
	}
	public void gameStart(int gameMode, int fishType1, int fishType2)
	{
		playerFish1 = new Player(Player.PLAYER1, eventManager);
		playerFish2 = new Player(Player.PLAYER2, eventManager);
		
		FishList.clear();
		
		if(!(gameMode==FishEater.GAME_RUN_1P ||
			gameMode == FishEater.GAME_RUN_2P))
			gameMode = FishEater.GAME_RUN_2P;
				
		playerFish1.setFish(createPlayerFish(fishType1, StartPoint1));
		this.gameMode = FishEater.GAME_RUN_1P;
		if(gameMode == FishEater.GAME_RUN_2P) {
			playerFish2.setFish(createPlayerFish(fishType2, StartPoint2));
			this.gameMode = FishEater.GAME_RUN_2P;
		}
		// �������� �������� ���õ� ������ �����ѵ� �������� �����Ѵ�
		curFishNumber = 0;
		curLevel = 3;
					
		respownTimer = new Timer(RESPOWN_DELAY, new RespownListener());
		
		respownTimer.start();
	}
	public void gameEnd(int whoWin)
	{
		if(whoWin == GameEvent.GAME_END_PLAYER1)
			playerFish1.setDone();
		else
			playerFish2.setDone();
	}
	public void gameEnd()
	{
		playerFish1.setDone();
		playerFish2.setDone();
	}
	public Fish getPlayer1Fish()
	{
		return playerFish1.getFish();
	}
	public Fish getPlayer2Fish()
	{
		return playerFish2.getFish();
	}
	private void respownFish()			// �ʿ� �°� ������ ����� ���� �����Ͽ� ������ �ϴ� �Լ�
	{
		while(true)
		{
			if(curFishNumber>= MAX_FISH) break;
			createComFish();
			if(curFishNumber<=MIN_FISH || rand.nextInt()%5==0) continue;
			break;
		}
	}
	public static Fish createPlayerFish(int fishType, Pos startPoint)
	{
		Fish fish;
		switch(fishType)
		{
		case Fish.VF_FOOTBALL:
			fish = new Football(startPoint);
			break;
		case Fish.VF_CHICKEN:
			fish = new Chicken(startPoint);
			break;
		case Fish.VF_PIRANHA:
			fish = new Piranha(startPoint);
			break;
		case Fish.VF_PICACHU:
			fish = new Pica(startPoint);
			break;
		default:
			fish = null;
		}
		return fish;
	}
	
	private void createComFish()			//��ġ ����, ���ӵ�, �ӵ�, ������ �������� ��ǻ�� ����� ����
	{
		// moveState info
		// Stop -1, Up 0, UpRight 1, Right 2,
		// DownRight 3, Down 4, DownLeft 5, Left 6, UpLeft 7
		int respownX = Math.abs(rand.nextInt())%2==0?LEFT_RESPOWN:RIGHT_RESPOWN;
		int respownY = Math.abs(rand.nextInt())%(DOWN_RESPOWN-2*UP_RESPOWN)+UP_RESPOWN;
		int respownLevel = Math.abs(rand.nextInt())%(curLevel+2)+1;
		int respownMaxVelocity= Math.abs(rand.nextInt())%RESPOWN_MAXVELOCITY+1;
		int respownAccelation=1;
		int respownWaterResitance=0;
		
		Fish fish = new NormalFish(respownX, respownY,
					respownMaxVelocity, respownAccelation, respownWaterResitance);
		fish.setLevel(respownLevel);
		if(respownX==RIGHT_RESPOWN)
		{
			fish.setMoveState(6);
		}
		else
		{
			fish.setMoveState(2);
		}
		
		FishList.add(fish);
		curFishNumber++;
	}
	public void allMove()
	// �������� �̵���Ű�� �浹Ȯ���ϰ� �̺�Ʈ�� ó���ϴ� �޼ҵ�
	{
		// ��ǻ�� ������ �÷��̾� ����⸦ ��� ���̵���Ų��
		for(int i=0; i<curFishNumber; i++)
		{
			FishList.elementAt(i).move();
		}
		this.playerFish1.move();
		if(this.gameMode==FishEater.GAME_RUN_2P)
			this.playerFish2.move();
		
		// ��ǻ�� ����Ⱑ ȭ�� ������ ������ ��� remove�Ѵ�.
		for(int i=0; i<curFishNumber; i++)
		{
			if(FishList.elementAt(i).isOutOfbound())
			{
				FishList.removeElementAt(i);
				curFishNumber--;
			}
		}

		// �÷��̾� ������  ��ǻ�� ����Ⱑ �浹�ߴ��� Ȯ���ϰ� �浹 �� ���
		// ���� �� �ִٸ�, �԰�, ���� �� ���ٸ� ���� �ʰ�, ������ �Ѵٸ� ������.
		allCollisionTest(playerFish1);
		if(gameMode==FishEater.GAME_RUN_2P)
			allCollisionTest(playerFish2);
		
		// �÷��̾� ����� ���� �浹�ߴ��� �˻��ѵ� ������ �̺�Ʈ�� �߻��Ѵ�
		// curLevel�� �����Ͽ� ������ �Ǵ� ��ǻ�� ������� ������ �����Ѵ�
		// ���� ���� ����� �÷��̾��� ���� ���� ��������
		if(gameMode==FishEater.GAME_RUN_2P) {
			if(playerFish1.isCollition(playerFish2.getFish()))
			{
				if(playerFish1.isEatable(playerFish2)){
					playerFish1.eat(playerFish2.getFish());
					playerFish2.eaten();
				}
				else if(playerFish2.isEatable(playerFish1)) {
					playerFish2.eat(playerFish1.getFish());
					playerFish1.eaten();
					
				}
			}
			curLevel = (playerFish1.getLevel()+playerFish2.getLevel())/2; 
		}
		else
			curLevel = playerFish1.getLevel();
	}
	private void allCollisionTest(Player player)
	// �÷��̾� ������  ��ǻ�� ����Ⱑ �浹�ߴ��� Ȯ���ϰ� �浹 �� ���
	// ���� �� �ִٸ�, �԰�, ���� �� ���ٸ� ���� �ʰ�, ������ �Ѵٸ� ������.
	{
		for(int i=0; i<curFishNumber; i++)
		{
			if(player.isCollition(FishList.elementAt(i)))
			{
				if(player.isEatable(FishList.elementAt(i)))
				{
					player.eat(FishList.elementAt(i));
					FishList.removeElementAt(i);
					curFishNumber--;
				}
				else if(FishList.elementAt(i).isEatable(player.getFish()))
				{
					player.eaten();
				}
			}
		}
	}
	public void allDraw(Graphics g)
	// �������� ��� GameScreen�� �׸��� �޼ҵ�
	{
		for(int i=0; i<curFishNumber; i++)
		{
			FishList.elementAt(i).draw(g);
		}
		this.playerFish1.draw(g);
		if(gameMode == FishEater.GAME_RUN_2P);
			this.playerFish2.draw(g);
	}
	private class RespownListener implements ActionListener
	// RESPOWN_DELAY ���� ��ǻ�� ����⸦ �������Ѵ�.
	{
		public void actionPerformed(ActionEvent e)
		{
			respownFish();
		}
	}
}
