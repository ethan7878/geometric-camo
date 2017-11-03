package camo;

import java.applet.*;
import java.awt.*;

import java.util.*;

public class Main extends Applet {
	
	int w = 1600, h = 900;
	int dots = 200;
	int minDist = 50;
	double xStretch = 1, yStretch = 1;
	
	Random rng = new Random();
	ArrayList<Point> points;
	Point[][] screen;
	
	public void init() {
		setSize(this.w, this.h);
		setBackground(Color.WHITE);
		this.screen = new Point[this.w][this.h];
	}
	
	public double dist(int x1, int y1, int x2, int y2) {
		
		double dx = Math.abs(x1 - x2);
		double dy = Math.abs(y1 - y2);
		double d  = Math.sqrt(dx * dx + dy * dy);
		
		return d;
		
	}
	
	public double dist(int x1, int y1, int x2, int y2, double xs, double ys) {
		
		double dx = Math.abs(x1 - x2) / xs;
		double dy = Math.abs(y1 - y2) / ys;
		double d  = Math.sqrt(dx * dx + dy * dy);
		
		return d;
		
	}
	
	public ArrayList<Point> createDots(int num) {
		ArrayList<Point> dots = new ArrayList<Point>();
		for (int i=0; i<num; i++) {
			dots.add(new Point(rng.nextInt(this.w), rng.nextInt(this.h)));
		}
		return dots;
	}
	
	public ArrayList<Point> refinePoints(ArrayList<Point> points, int minDist) {
		
		for (int i = points.size() - 1; i > -1; i--) {
			Point p = points.get(i);
			
			for (Point n : points) {
				
				if (!p.equals(n)) {
					double d = this.dist(p.x, p.y, n.x, n.y);
					if (d < minDist) {
						points.remove(i);
						break;
					}
				}
			}
		}
		
		return points;
		
	}
	
	public Point[][] calculate(ArrayList<Point> points) {
		
		Point[][] screen = new Point[this.w][this.h];
		
		for (int x = 0; x < this.w; x++) {
			for (int y = 0; y < this.h; y++) {
				
				screen[x][y] = new Point(x, y);
				
				double d1 = 1000000000, d2 = 1000000000;
				int i1 = 0, i2 = 0;
				
				for (int i = 0; i < points.size(); i++) {
					
					Point p = points.get(i);
					
					double d = this.dist(x, y, p.x, p.y, this.xStretch, this.yStretch);
					
					if (d < d2) {
						d2 = d;
						i2 = i;
					}
					
					if (d2 < d1) {
						double tempD = d1;
						d1 = d2;
						d2 = tempD;
						
						int tempI = i1;
						i1 = i2;
						i2 = tempI;
					}
					
				}
				
				screen[x][y].closest = points.get(i1);
				
				if (Math.abs(d1 - d2) < 20) {
					points.get(i1).neighbours.add(points.get(i2));
					points.get(i2).neighbours.add(points.get(i1));
				}
				
			}
		}
		
		return screen;
		
	}
	
	public void pickColours(ArrayList<Point> points) {
		
		String[] blue  = {"#76a8f7", "#417ee0", "#aeccfc", "#bac9e2", "#3f78b7", "#ffffff"};
		String[] red   = {"#ff6600", "#ffa366", "#993d00", "#e63900", "#ff794d", "#000000"};
		String[] green = {"#00cc00", "#66ff66", "#009900", "#2d862d", "#59b300", "#ffffff"};
		String[] pink  = {"#ff4dff", "#cc00cc", "#ff66ff", "#cc00ff", "#e580ff", "#ffffff"};
		
		Color[] palette = new Color[6];
		
		for (int i=0; i<6; i++) {
			palette[i] = Color.decode(blue[i]);			// Change to whatever list you want to use
		}
		
		for (Point p : points) {
			
			for (Color c : palette) {
				
				boolean isTaken = false;
				
				for (Point n : p.neighbours) {
					if (n.c != null && n.c.equals(c)) {
						isTaken = true;
						break;
					}
				}
				
				if (!isTaken) {
					p.c = c;
					break;
				}
			}
			
			if (p.c == null) {
				p.c = palette[palette.length - 1];
			}
		}
	}
	
	public void paint(Graphics g) {
		
		this.points = this.createDots(this.dots);
		
		this.points = this.refinePoints(this.points, this.minDist);
		
		this.screen = this.calculate(this.points);
		
		this.pickColours(this.points);
		
		g.setColor(Color.BLACK);
		
		for (Point p : this.points) {
			g.fillOval(p.x - 2, p.y - 2, 4, 4);
		}
		
		this.draw(g, screen);
	}
	
	public void draw(Graphics g, Point[][] screen) {
		
		for (int x = 0; x < this.w; x++) {
			for (int y = 0; y < this.h; y++) {
				
				Point p = screen[x][y];
				g.setColor(p.closest.c);
				g.drawLine(x, y, x, y);
				
			}
		}
	}
	
}
