package me.jupdyke01.mazegenerator;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;

public class Main extends Canvas implements Runnable {

	public static final long serialVersionUID = 2114965587617771299L;
	public int w;
	public int width, height;
	public static int cols, rows;
	public static ArrayList<Cell> cells = new ArrayList<>();
	public static JButton speed;
	public static JButton export;
	public boolean running = false;
	public double framerate = 5.0;
	public int frames;
	public double delta;
	public Stack<Cell> stack = new Stack<>();
	public BufferedImage image;
	public Graphics g2;
	private Cell current;
	private Random r;
	private Cell entrance;
	private Cell exit;
	Thread thread;

	public Main(int dim, int w) {
		start();
		// Assigning variables
		this.w = w;
		this.width = dim;
		this.height = dim;
		r = new Random();

		// Creating buffered image
		image = new BufferedImage(width + 1, height + 1, BufferedImage.TYPE_INT_RGB);
		image.createGraphics();
		g2 = image.getGraphics();

		// Creating jframe
		JFrame frame = new JFrame("Maze Generator");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Creating JButton for Speed
		speed = new JButton("Speed - 5");
		speed.setBounds(width + 2, 10, 200, 80);
		ButtonListener listener = new ButtonListener(this);
		speed.addActionListener(listener);

		// Creating JButton for exporting
		export = new JButton("Export");
		export.setBounds(width + 2, 90, 200, 80);
		export.addActionListener(listener);
		
		// Assigning frame variables
		frame.setSize(new Dimension(width + 207, height + 30));
		frame.setResizable(false);
		frame.getContentPane().add(speed);
		frame.getContentPane().add(export);
		frame.add(this);
		frame.setVisible(true);
		// Begin maze generation
		generate();

	}
	
	public void start() {
		running = true;
		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run() {
		// Game loop
		running = true;
		this.requestFocus();
		long lastTime = System.nanoTime();
		delta = 0;
		long timer = System.currentTimeMillis();
		frames = 0;
		while(running) {
			long now = System.nanoTime();
			delta += (now-lastTime) / (1000000000 / framerate);
			lastTime = now;
			while(delta >= 1) {
				tick();
				frames++;
				delta--;
			}
			if(System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				frames = 0;
			}
		}
	}


	public void tick() {
		// Create buffer strategy and graphics.
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();

		// Set current(start cell) to visited
		current.visited = true;
		// Get an unvisited neighbor of current cell
		Cell next = current.checkNeighbors();
		// If the cell has an unvisited neighbor
		if (next != null) {
			// Set to visited
			next.visited = true;
			// As long as it has neighbors push it to the stack for backtracking
			if (current.checkNeighbors() != null) {
				// Pushing it to stack
				stack.push(current);
			}
			// Remove walls between current cell and next cell
			removeWalls(current, next);
			// Set the cell color to black
			current.setColor(Color.black);
			// Set the current cell to the neighbor/next cell
			current = next;
			// Set the color to green
			current.setColor(new Color(0, 255, 0));
		} else if (stack.size() > 0) {
			//Set color to black aain
			current.setColor(Color.black);
			// Pop the top cell with unvisited neighbors off the stack
			current = stack.pop();
		} else {
			// Set color to black
			current.setColor(Color.black);
		}
		// Set color to black
		g.setColor(Color.black);
		// Fill background
		g.fillRect(0, 0, width + 500, height + 500);

		// Go through every cell
		for(Cell cell : cells) {
			// If program is not running
			if (!running)
				// Leave for loop
				break;
			//Else; Render the cell
			cell.render(g);
		}
		// Go through every cell
		for(Cell cell : cells) {
			// If program is not running
			if (!running)
				// Leave for loop
				break;
			//Else; Render the walls
			cell.renderWall(g);
		}

		// Draw entrance and exit nodes
		g.setColor(Color.GREEN);
		g.drawLine(entrance.getX() * w + 1, entrance.getY(), (entrance.getX() * w + w) - 1, entrance.getY());
		g.setColor(Color.RED);
		g.drawLine(exit.getX() * w + 1, exit.getY() * w + w, (exit.getX() * w + w) - 1, exit.getY() * w + w);

		// Draw entrance and exit nodes for buffered image
		g2.setColor(Color.GREEN);
		g2.drawLine(entrance.getX() * w + 1, entrance.getY(), (entrance.getX() * w + w) - 1, entrance.getY());
		g2.setColor(Color.RED);
		g2.drawLine(exit.getX() * w + 1, exit.getY() * w + w, (exit.getX() * w + w) - 1, exit.getY() * w + w);

		// Dispose graphics and show buffer strategy
		g.dispose();
		bs.show();

	}


	// Generate all cells
	public void generate() {
		// Amount of columns
		cols = width/w;
		// Amount of rows
		rows = height/w;

		// Clear all arraylists and stacks
		clear();
		// Loop through all possible cell spots
		for (int j = 0; j < cols; j++) {
			for (int i = 0; i < rows; i++) {
				// Create a cell object
				Cell cell = new Cell(i, j, this);
				// Add object to array
				cells.add(cell);
			}
		}
		// Getting random cell at top and bottom for entrance/exit
		entrance = getRandTopCell();
		exit = getRandBotCell();

		// Remove the wall for entrance/exit
		entrance.removeTopWall();
		exit.removeBotWall();

		// Picking starting cell somewhere towards the middle
		current = cells.get((cells.size() / 2) + r.nextInt(cols));
		//if (!running)
			// If the program is not running, make it run.
			//run();
	}


	public void clear() {
		cells.clear();
		stack.clear();
	}

	public void export() {
		File folder = new File("Mazes");
		if (!folder.exists()) {
			folder.mkdir();
		}
		try {
			Double CURRENT_FRAME_RATE = framerate;
			framerate = 0;
			delta = 0;
			ImageIO.write(this.image, "png", new File(folder, folder.listFiles().length + ".png"));
			framerate = CURRENT_FRAME_RATE;
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*public void export() {
		File folder = new File("Mazes");
		if (!folder.exists())
			folder.mkdir();


		try {
			ImageIO.write(image, "png", new File(folder, folder.listFiles().length + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/

	public void removeWalls(Cell a, Cell b) {
		int x = a.x - b.x;
		if (x == 1) {
			a.walls.put("W", false);
			b.walls.put("E", false);
		} else if (x == -1) {
			a.walls.put("E", false);
			b.walls.put("W", false);
		}
		int y = a.y - b.y;
		if (y == 1) {
			a.walls.put("N", false);
			b.walls.put("S", false);
		} else if (y == -1) {
			a.walls.put("S", false);
			b.walls.put("N", false);
		}
	}


	public Cell getRandBotCell() {
		return cells.get(cells.size() - 1 - r.nextInt(cols));
	}


	public Cell getRandTopCell() {
		return cells.get(r.nextInt(cols));
	}

}
