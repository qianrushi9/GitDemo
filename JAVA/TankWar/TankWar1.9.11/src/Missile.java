import java.awt.*;
import java.util.List;

public class Missile {
	public static final int XSPEED = 20;
	public static final int YSPEED = 20;
	public static final int WIDTH = 10;
	public static final int HEIGHT = 10;
	public static int ID = 0;
	int tankId;
	int id;
	
	int x,y;
	Direction dir;
	private boolean live =true;
	private TankClient tc;
	boolean good;
	
	public boolean isLive() {
		return live;
	}


	public Missile(int tankId, int x, int y,boolean good, Direction dir, TankClient tc) {		
		this.tankId = tankId;
		this.id = ID++;
		this.x = x;
		this.y = y;
		this.good = good;
		this.dir = dir;
		this.tc  =tc;
	}
	
	public void draw(Graphics g) {
		if(!isLive()) {
			tc.missiles.remove(this);
			return;
		}
		Color c = g.getColor();
		if(!good) g.setColor(Color.BLACK);
		else g.setColor(Color.RED);
		g.fillOval(x, y, WIDTH,HEIGHT);
		g.setColor(c);
		
		move();
	}

	private void move() {
		switch(dir) {
		case L:
			x -= XSPEED;
			break;
		case LU:
			x -= XSPEED;
			y -= YSPEED;
			break;
		case U:
			y -= XSPEED;
			break;
		case RU:
			x += XSPEED;
			y -= YSPEED;
			break;
		case R:
			x += XSPEED;
			break;
		case RD:
			x += XSPEED;
			y += YSPEED;
			break;
		case D:			
			y += YSPEED;
			break;
		case LD:
			x -= XSPEED;
			y += YSPEED;
			break;
		}
		
		if(x<0 || y<0 || x>TankClient.GAME_WIDTH || y>TankClient.GAME_HEIGHT) {
			setLive(false);					
		}
	}
	
	public Rectangle getRect() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}
	
	public boolean hitTank(Tank t) {
		if(this.isLive() && this.getRect().intersects(t.getRect()) && t.isLive() && this.good != t.isGood()) {
			t.setLive(false);
			Explode e = new Explode(x, y, tc);
			tc.explodes.add(e);
			this.setLive(false);
			return true;
		}
		return false;
	}
	
	public boolean hitTanks(List<Tank> tanks) {
		for(int i=0; i<tanks.size(); i++) {
			if(hitTank(tanks.get(i))) return true;
		}
		
		return false;
	}


	public void setLive(boolean live) {
		this.live = live;
	}
	
}
