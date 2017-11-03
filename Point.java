package camo;

import java.awt.*;
import java.util.*;

public class Point {

	int x, y;
	Color c;
	ArrayList<Point> neighbours;
	
	Point closest;
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
		
		this.neighbours = new ArrayList<Point>();
	}

}
