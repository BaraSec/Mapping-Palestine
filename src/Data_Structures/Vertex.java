package Data_Structures;

import java.util.LinkedList;


public class Vertex implements Comparable<Vertex>
{
	// Data fields
	private String name;
	private double xCo, yCo;
	private double distance;
	private Vertex path;
	private LinkedList<Vertex> leavingMe = new LinkedList<>();
	private String type;

	// Constructors
	public Vertex(String name, double xCo, double yCo, String type)
	{
		this.name = name;
		this.xCo = xCo;
		this.yCo = yCo;
		this.type = type;
	}

	// Add an adjacent to a vertex
	public void addAdjacent(Vertex leaving)
	{
		leavingMe.addFirst(leaving);
	}

	// Getters and Setters
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public double getxCo()
	{
		return xCo;
	}

	public void setxCo(double xCo)
	{
		this.xCo = xCo;
	}

	public double getyCo()
	{
		return yCo;
	}

	public void setyCo(double yCo)
	{
		this.yCo = yCo;
	}

	public void setDistance(double distance)
	{
		this.distance = distance;
	}

	public double getDistance()
	{
		return distance;
	}

	public Vertex getPath()
	{
		return path;
	}

	public void setPath(Vertex path)
	{
		this.path = path;
	}

	public void setLeavingMe(LinkedList<Vertex> leavingMe)
	{
		this.leavingMe = leavingMe;
	}

	public LinkedList<Vertex> getLeavingMe()
	{
		return leavingMe;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	// Comparator
	@Override
	public int compareTo(Vertex other)
	{
		return Double.compare(distance, other.getDistance());
	}
}
