package me.jupdyke01.mazegenerator;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Cell {

	Main main;
	int x;
	int y;
	int w;
	boolean visited;
	public HashMap<String, Boolean> walls = new HashMap<>();
	private Color c = new Color(51, 51, 204);
	//private Color c = Color.black;
	
	public Cell(int x, int y, Main main) {
		w = main.w;
		HashMap<String, Boolean> walls = new HashMap<>();
		walls.put("N", true);
		walls.put("E", true);
		walls.put("S", true);
		walls.put("W", true);

		this.main = main;
		this.x = x;
		this.y = y;
		this.visited = false;
		this.walls = walls;
	}
	
	public int getX() {
		return this.x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return this.y;
	}

	public void setY(int y) {
		this.y = y;
	}	

	public HashMap<String, Boolean> getWalls() {
		return this.walls;
	}

	public void setColor(Color c) {
		this.c = c;
	}

	public Cell checkNeighbors() {
		ArrayList<Cell> neighbors = new ArrayList<>();
		if (index(x, y-1) != -1) {
			Cell top = Main.cells.get(index(x, y-1));
			if (top != null && !top.visited)
				neighbors.add(top);
		}
		if (index(x+1, y) != -1) {
			Cell right = Main.cells.get(index(x+1, y));
			if (right != null && !right.visited)
				neighbors.add(right);
		}
		if (index(x, y+1) != -1) {
			Cell bottom = Main.cells.get(index(x, y+1));
			if (bottom != null && !bottom.visited)
				neighbors.add(bottom);
		}
		if (index(x-1, y) != -1) {
			Cell left = Main.cells.get(index(x-1, y));
			if (left != null && !left.visited)
				neighbors.add(left);
		}
		if (neighbors.size() > 0) {
			Random r = new Random();
			return neighbors.get(r.nextInt(neighbors.size()));
		}
		return null;
	}

	public int index(int i, int j) {
		if (i < 0 || j < 0 || i > Main.cols - 1 || j > Main.cols - 1)
			return -1;
		return i + j * Main.cols;
	}

	public void renderWall(Graphics g) {
		int locX = x*main.w;
		int locY = y*main.w;
		Graphics g2 = main.g2;
		g.setColor(new Color(173, 173, 173));
		g2.setColor(new Color(173, 173, 173));
		if (walls.get("N")) {
			g.drawLine(locX , locY, locX + w , locY);
			g2.drawLine(locX , locY, locX + w , locY);
		}
		if (walls.get("E")) {
			g.drawLine(locX + w, locY , locX + w, locY + w );
			g2.drawLine(locX + w, locY , locX + w, locY + w );
		}
		if (walls.get("S")) {
			g.drawLine(locX + w , locY + w, locX , locY + w);
			g2.drawLine(locX + w , locY + w, locX , locY + w);
		}
		if (walls.get("W")) {
			g.drawLine(locX, locY + w , locX, locY );
			g2.drawLine(locX, locY + w , locX, locY );
		}
	}

	public void removeTopWall() {
		walls.put("N", false);
	}
	
	public void removeBotWall() {
		walls.put("S", false);
	}
	
	public void render(Graphics g) {
		Graphics g2 = main.image.getGraphics();
		g.setColor(c);
		g2.setColor(c);
		g.fillRect(x * main.w, y * main.w, main.w, main.w);
		g2.fillRect(x * main.w, y * main.w, main.w, main.w);



		if (visited) {
			//g.setColor(Color.CYAN);
			//g.fillRect(locX, locY, Main.W, Main.W);
		}
	}

}
