
public class GameStartEvent extends GameEvent {

	private int gameMode;
	private int playerFish1;
	private int playerFish2;
	
	public GameStartEvent(int gameMode, int fish1, int fish2)
	{
		super("GameStart");
		this.gameMode = gameMode;
		if(!(gameMode == GameEvent.GAME_START_1P 
			|| gameMode == GameEvent.GAME_START_2P))
		{
			gameMode = GameEvent.GAME_END_PLAYER2;
		}
		playerFish1 = fish1;
		playerFish2 = fish2;
				
	}
	public int getPlayer1Fish()
	{
		return playerFish1;
	}
	public int getPlayerFish2()
	{
		return playerFish2;
	}
	public int getEventType()
	{
		return gameMode;
	}

}
