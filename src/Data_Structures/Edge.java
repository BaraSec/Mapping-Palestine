package Data_Structures;

public class Edge
{
	// Data fields
	private Vertex from;
	private Vertex to;
	private double cost;

	// Constructors
	public Edge(Vertex from, Vertex to, double cost)
	{
		this.from = from;
		this.to = to;
		this.cost = cost;

		from.addAdjacent(to);
	}

	// Getters
	public Vertex getFrom()
	{
		return from;
	}

	public void setFrom(Vertex from)
	{
		this.from = from;
	}

	public Vertex getTo()
	{
		return to;
	}

	public void setTo(Vertex to)
	{
		this.to = to;
	}

	public double getCost()
	{
		return cost;
	}

	public void setCost(double cost)
	{
		this.cost = cost;
	}
}
