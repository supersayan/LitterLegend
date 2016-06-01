package maze;

import java.util.ArrayList;

public class Node{
ArrayList<Node> children;
public int value;

  	public Node(){  
		children = new ArrayList<Node>();
		value = 0;
	}
	
	public Node(int value) {
		children = new ArrayList<Node>();
		this.value = value;
	}
	
	public void add() {
		value++;
	}

}