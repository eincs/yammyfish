
public class Pos {

	private double X_value;
	private double Y_value;
	
	public Pos(double x, double y)
	{
		X_value = x;
		Y_value = y;
	}
	public void setValue(double x, double y)
	{
		X_value = x;
		Y_value = y;
	}
	public double getX()
	{
		return X_value;
	}
	public double getY()
	{
		return Y_value;
	}
	public Pos getInstance()
	{
		Pos temp = new Pos(this.getX(), this.getY());
		return temp;
	}
	public double getDistance(Pos pos)
	{
		double res;
		
		res = Math.sqrt(Math.pow(this.X_value - pos.getX(), 2) + Math.pow(this.Y_value - pos.getY(), 2)); 
			
		return res;
	}
}

