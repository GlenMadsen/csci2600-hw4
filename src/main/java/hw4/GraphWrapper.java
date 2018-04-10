package hw4;

import java.util.Iterator;

public class GraphWrapper
{
    private Graph<String, String> graph;
    
    public GraphWrapper()
    {
    	this.graph = new Graph<String, String>();
    }
    
    public void addNode(String nodeData)
    {
    	this.graph.addNode(nodeData);
    }

    public void addEdge(String parentNode, String childNode, String edgeLabel)
    {
    	this.graph.addEdge(parentNode, childNode, edgeLabel);
    }
	public Iterator<String> listNodes()
	{
		return this.graph.listNodes();
	}
	public Iterator<String> listChildren(String parentNode)
	{
		return this.graph.listChildren(parentNode);
	}

}

