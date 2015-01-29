import java.applet.Applet;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Snake extends Applet implements KeyListener 
{
	int UP = 0, RIGHT = 1, DOWN = 2, LEFT = 3, NEXT_MOVE, nsnake = 0, nmove = 0, speed = 1, ncord = 0, score = 0;
	int PREV_MOVE = DOWN, CURRENT_MOVE = PREV_MOVE;
	int x, y, RAND_MAX = 380, RAND_MIN = 20;
	int XMAX = 400, YMAX = 300;
	int snake[][]     = new int[100][3];
	int move_list[]   = new int[1000];
	int cord_list[][] = new int[1000][2];
	
	boolean up, down, right, left, eaten = false;

	private Image dbImage;
	private Graphics dbg;

	public void init()
	{
		add_snake(100, 30, 0);
		add_snake(100, 15, 0);
		add_snake(100, 0, 0);
		x = get_rand(RAND_MAX, RAND_MIN);
		y = get_rand(YMAX - 50, RAND_MIN);
		move_list[nmove++] = DOWN;
		
		resize(XMAX, YMAX);
		addKeyListener(this);
		this.requestFocus();
	}
	
	public void paint(Graphics g)
	{
		try {
			Thread.sleep(3); // Slow down the speed of the snake.
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		move_snake(g);
		check_apple_collision(g);
		
		PREV_MOVE = CURRENT_MOVE;
		if (collision(snake[0][0], snake[0][1]))
			end_game(g);
		update_score(g);
		
		repaint();
	}
	
	public void add_snake(int x, int y, int dir)
	{
		snake[nsnake][0] = x;
		snake[nsnake][1] = y;
		snake[nsnake][2] = dir;
		nsnake++;
	}
	
	public boolean on_cord(int x, int y)
	{
		for (int i = 0; i < ncord; ++i)
		{
			if (cord_list[i][0] == x && cord_list[i][1] == y)
				return true;
		}
		return false;
	}
	
	public void move_snake(Graphics g)
	{
		for (int i = 0; i < nsnake; ++i) {
			if (i == 0) 
			{
				if (CURRENT_MOVE != PREV_MOVE) 
				{
					cord_list[ncord][0]   = snake[i][0];
					cord_list[ncord++][1] = snake[i][1];
					move_rect(i, g, true);
				}
				else
					move_rect(i, g, false);
			}
			else
			{
				if (on_cord(snake[i][0], snake[i][1])) 
					move_rect(i, g, true);
				else
					move_rect(i, g, false);
			}
		}
	}
	
	public void move_rect(int i, Graphics g, boolean inc)
	{
		if (move_list[snake[i][2]] == UP)
			snake[i][1] -= speed;
		else if (move_list[snake[i][2]] == RIGHT)
			snake[i][0] += speed;
		else if (move_list[snake[i][2]] == DOWN)
			snake[i][1] += speed;
		else if (move_list[snake[i][2]] == LEFT)
			snake[i][0] -= speed;
		if (snake[i][2] < nmove - 1 && inc)
			snake[i][2] += 1;
		g.fillRect(snake[i][0], snake[i][1], 15, 15);
	}
	
	public int get_rand(int max, int min)
	{
		return min + (int)(Math.random() * ((max - min) + 1));
	}
	
	public void check_apple_collision(Graphics g)
	{
		if (snake[0][0] > x-14 && snake[0][0] < x+15 && snake[0][1] > y-14 && snake[0][1] < y+15)
		{
			score++;
			x = get_rand(RAND_MAX, RAND_MIN);
			y = get_rand(YMAX - 50, RAND_MIN);
			int addx = 0, addy = 0; 
			if (move_list[snake[nsnake-1][2]] == UP) {
				addx = snake[nsnake-1][0];
				addy = snake[nsnake-1][1] + 15;
			}
			if (move_list[snake[nsnake-1][2]] == RIGHT) {
				addx = snake[nsnake-1][0] - 15;
				addy = snake[nsnake-1][1];
			}
			if (move_list[snake[nsnake-1][2]] == DOWN) {
				addx = snake[nsnake-1][0];
				addy = snake[nsnake-1][1] -15;
			}
			if (move_list[snake[nsnake-1][2]] == LEFT) {
				addx = snake[nsnake-1][0] + 15;
				addy = snake[nsnake-1][1];
			}
			add_snake(addx, addy, snake[nsnake-1][2]);
		}
		g.fillRect(x, y, 15, 15);
	}
	public boolean collision(int headx, int heady)
	{
		boolean collide = false;
		
		if (headx >= XMAX || headx <= 0 || heady >= YMAX || heady <= 0)
			collide = true;
		for (int i = 1; i < nsnake; ++i)
		{
			int x = snake[i][0], y = snake[i][1];
			if (headx > x && headx < x+8 && heady > y && heady < y+8)
				collide = true;
		}
		return collide;
	}
	
	public void end_game(Graphics g)
	{
		speed = 0;
		Font font = new Font("Arial", Font.PLAIN, 30);
	    g.setFont(font);
	    g.drawString("GAME OVER", 110, 200);
	    g.drawString("Score: " + score, 110, 240);
	    repaint();
	}
	
	public void update_score(Graphics g)
	{
		g.drawRect(299, 0, 100, 100);
		g.drawString("Score: " + score, 320 ,20);
	}
	
	@Override
	public void keyPressed(KeyEvent e) 
	{
		int key = e.getKeyCode(), dir = move_list[snake[0][2]];
		
		if (key == e.VK_UP && dir != DOWN && dir != UP) {
			NEXT_MOVE = UP;
			move_list[nmove++] = UP;
			CURRENT_MOVE = UP;
		}
		else if (key == e.VK_RIGHT && dir != LEFT && dir != RIGHT) {
			NEXT_MOVE = RIGHT;
			move_list[nmove++] = RIGHT;
			CURRENT_MOVE = RIGHT;
		}
		else if (key == e.VK_DOWN && dir != UP && dir != DOWN) { 
			NEXT_MOVE = DOWN;
			move_list[nmove++] = DOWN;
			CURRENT_MOVE = DOWN;
		}
		else if (key == e.VK_LEFT && dir != RIGHT && dir != LEFT) {
			NEXT_MOVE = LEFT;
			move_list[nmove++] = LEFT;
			CURRENT_MOVE = LEFT;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}
	
	public void update(Graphics g) // Smoother graphics.
	  {
	    if (dbImage == null) {
	      dbImage = createImage(this.getSize().width, this.getSize().height);
	      dbg = dbImage.getGraphics();
	    }
	    dbg.setColor(getBackground());
	    dbg.fillRect(0, 0, this.getSize().width, this.getSize().height);
	    
	    dbg.setColor(getForeground());
	    paint(dbg);
	    
	    g.drawImage(dbImage, 0, 0, this);
	  } 
}
