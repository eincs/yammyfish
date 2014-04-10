
public class GameSetEvent extends GameEvent{
	public GameSetEvent()
	{
		super("GameSetEvent");
	}
	public int getEventType()
	{
		return GameEvent.GAME_SET_EVENT;
	}
}
