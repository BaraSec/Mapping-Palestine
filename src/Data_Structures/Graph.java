package Data_Structures;

import java.util.*;


public class Graph
{
	// Data fields
	LinkedList<Edge> edges = new LinkedList<>();
	LinkedList<Vertex> vertices = new LinkedList<>();

	// add data to the graph
	public void addData(Vertex from, Vertex to, double cost)
	{
		Vertex fromLocal = findVertex(from);
		Vertex toLocal = findVertex(to);

		if(fromLocal == null)
		{
			fromLocal = from;
			vertices.addFirst(fromLocal);
		}
		if(toLocal == null)
		{
			toLocal = to;
			vertices.addFirst(toLocal);
		}

		edges.addFirst(new Edge(fromLocal, toLocal, cost));
	}

	// Getters and setters
	public LinkedList<Edge> getEdges()
	{
		return edges;
	}

	public void setEdges(LinkedList<Edge> edges)
	{
		this.edges = edges;
	}

	public LinkedList<Vertex> getVertices()
	{
		return vertices;
	}

	public void setVertices(LinkedList<Vertex> vertices)
	{
		this.vertices = vertices;
	}

	// Find a vertex
	private Vertex findVertex(Vertex v)
	{
		for(int i = 0; i < vertices.size(); i++)
			if(v.getName().toLowerCase().matches(vertices.get(i).getName().toLowerCase()) && v.getxCo() == vertices.get(i).getxCo() && v.getyCo() == vertices.get(i).getyCo() && v.getType().toLowerCase().matches(vertices.get(i).getType().toLowerCase()))
				return vertices.get(i);

		return null;
	}

	// Find a vertex by name
	public Vertex findVertexByName(String name)
	{
		for(int i = 0; i < vertices.size(); i++)
			if(name.toLowerCase().matches(vertices.get(i).getName().toLowerCase()))
				return vertices.get(i);

		return null;
	}

	// Find a vertex by coordinates
	public Vertex findVertexByCoos(double x, double y)
	{
		for(int i = 0; i < edges.size(); i++)
		{
			if(isInRange((int)(edges.get(i).getFrom().getxCo()), (int)x, 6) && isInRange((int)(edges.get(i).getFrom().getyCo()), (int)y, 6))
				return edges.get(i).getFrom();

			else if(isInRange((int)(edges.get(i).getTo().getxCo()), (int)x, 6) && isInRange((int)(edges.get(i).getTo().getyCo()), (int)y, 6))
				return edges.get(i).getTo();
		}

		return null;
	}

	// returns th number of one direction edges
	public int findNumberOfOneWays()
	{
		int sum = 0;

		for (Edge e : edges)
			if(findEdge(e.getTo(), e.getFrom()) == null)
				sum++;

		return sum;
	}

	// find an edge
	public Edge findEdge(Vertex u, Vertex v)
	{
		for(int i = 0; i < edges.size(); i++)
			if((u.getName().toLowerCase().matches(edges.get(i).getFrom().getName().toLowerCase()) && u.getxCo() == edges.get(i).getFrom().getxCo() && u.getyCo() == edges.get(i).getFrom().getyCo() && u.getType().toLowerCase().matches(edges.get(i).getFrom().getType().toLowerCase())) && (v.getName().toLowerCase().matches(edges.get(i).getTo().getName().toLowerCase()) && v.getxCo() == edges.get(i).getTo().getxCo() && v.getyCo() == edges.get(i).getTo().getyCo() && v.getType().toLowerCase().matches(edges.get(i).getTo().getType().toLowerCase())))
				return edges.get(i);

		return null;
	}

	// return if a vertex exists in the desired coordinates
	public boolean isFound(double x, double y)
	{
		for(int i = 0; i < edges.size(); i++)
			if((isInRange((int)(edges.get(i).getFrom().getxCo()), (int)x, 6) && isInRange((int)(edges.get(i).getFrom().getyCo()), (int)y, 6)) || (isInRange((int)(edges.get(i).getTo().getxCo()), (int)x, 6) && isInRange((int)(edges.get(i).getTo().getyCo()), (int)y, 6)))
				return true;

		return false;
	}

	// are the coordinates around a close vertex?
	public boolean isInRange(int num, int key, int plusMinus)
	{
		if(key <= num + plusMinus && key >= num - plusMinus)
			return true;

		return false;
	}

	// SP algorithm
	private void dijkstra(Vertex from)
	{
		// reset all vertices minDistance and previous
		resetDistances();

		Vertex fromLocal = findVertex(from);

		// set to 0 and add to heap
		fromLocal.setDistance(0);

		PriorityQueue<Vertex> pq = new PriorityQueue<>();
		pq.add(fromLocal);

		while (!pq.isEmpty() )
		{
			//pull off top of queue
			Vertex u = pq.poll();

			// loop through adjacent vertices
			for (Vertex v : u.getLeavingMe())
			{
				Edge e = findEdge(u, v);

				// add cost to current
				double newDistance = u.getDistance() + e.getCost();

				if (newDistance < v.getDistance())
				{
					// new cost is smaller, set it and add to queue
					pq.remove(v);

					v.setDistance(newDistance);

					// link vertex
					v.setPath(u);

					pq.add(v);
				}
			}
		}
	}

	// return description about the SP
	public LinkedList<String> getShortestPathDescription(Vertex target)
	{
		LinkedList<String> path = new LinkedList<>();

		// check for no path found
		if (target.getDistance() == Integer.MAX_VALUE)
		{
			path.add("No path found");

			return path;
		}

		// loop through the vertices from end target
		for (Vertex v = target; v.getPath() != null; v = v.getPath())
			path.add(v.getPath().getName() + " > " + v.getName() + " : " + findEdge(v.getPath(), v).getCost() + " km");

		// flip the list
		Collections.reverse(path);

		return path;
	}

	// get the SP's vertices
	public LinkedList<Vertex> getShortestPathVertices(Vertex target)
	{
		LinkedList<Vertex> path = new LinkedList<>();

		// check for no path found
		if (target.getDistance() == Integer.MAX_VALUE)
			return null;

		for (Vertex v = target; v.getPath() != null; v = v.getPath())
			path.add(v);

		Collections.reverse(path);

		return path;
	}

	public Vertex getShortestPathStart(Vertex target)
	{
		// check for no path found
		if (target.getDistance() == Integer.MAX_VALUE)
			return null;

		return target;
	}

	// reset table
	private void resetDistances()
	{
		for (Vertex v : vertices)
		{
			v.setDistance(Integer.MAX_VALUE);
			v.setPath(null);
		}
	}

	// get the shortest path from a vertex to a another
	public Vertex getShortestPath(Vertex from, Vertex to)
	{
		dijkstra(from);

		return getShortestPathStart(findVertex(to));
	}

	public Vertex AstarSearch(Vertex source, Vertex goal)
	{
//		source.setDistance(0);;
//		        PriorityQueue<Vertex> queue = new PriorityQueue<Vertex>(20,
//		            new Comparator<Vertex>(){
//
//		                //override compare method
//		                public int compare(Vertex i, Vertex j){
//		                    if(i.getDistance() > j.getDistance()){
//		                        return 1;
//		                    }
//
//		                    else if (i.getDistance() < j.getDistance()){
//		                        return -1;
//		                    }
//
//		                    else{
//		                        return 0;
//		                    }
//		                }
//		            }
//
//		        );
//
//		        queue.add(source);
//
//		        Set<Vertex> explored = new HashSet<Vertex>();
//		        boolean found = false;
//
//		        //while frontier is not empty
//		        do{
//
//					Vertex current = queue.poll();
//		            explored.add(current);
//
//
//		            if(current.getName().matches(goal.getName())){
//		                found = true;
//
//
//		            }
//
//
//
//
//		            for(Vertex child: current.getLeavingMe()){
//
//		            	Edge e = findEdge(child, current);
//
//		                double cost = e.getCost();
//		                child.setDistance(current.getDistance() + cost);
//
//		                if(!explored.contains(child) && !queue.contains(child)){
//
//		                    child.setPath(current);
//		                    queue.add(child);
//
//		                }
//		                else if((queue.contains(child))&&(child.getDistance()>current.getDistance())){
//		                    child.setPath(current);
//
//		                    // the next two calls decrease the key of the node in the queue
//		                    queue.remove(child);
//		                    queue.add(child);
//		                }
//
//
//		            }
//		        }while(!queue.isEmpty());
//
//		return findVertex(goal);

		return  null;
	}
}
