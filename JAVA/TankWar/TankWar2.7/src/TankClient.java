import java.awt.*;
import java.util.ArrayList;
import java.awt.event.*;
import java.util.List;

public class TankClient extends Frame {
	
	public static final int GAME_WIDTH = 800;
	public static final int GAME_HEIGHT = 600; 
	
	Wall w1 = new Wall(100,200,20,150,this), w2 = new Wall(300, 100, 300, 20, this);
		
	Tank myTank  = new Tank(50,50,true,Direction.STOP,this);
	List<Tank> tanks = new ArrayList<Tank>();
	
	List<Missile> missiles = new ArrayList<Missile>();
	List<Explode> explodes = new ArrayList<Explode>();
	
	Image offScreenImage = null;	
	Blood b = new Blood(this);
	
	@Override
	public void paint(Graphics g) {
		
		if(tanks.size() == 0) {
			for(int i=0; i<8; i++) {
				tanks.add(new Tank(50 + 50 *(i+1), 50, false, Direction.D, this));
			}
		}
		
		g.drawString("missiles count:" + missiles.size(), 30, 50);
		g.drawString("explodes count:" + explodes.size(),30, 70);
		g.drawString("tanks    count:" + tanks.size(),30, 90);
		g.drawString("life     count:" + myTank.getLife(), 30, 110);
		w1.draw(g);
		w2.draw(g);
		myTank.draw(g);	
		b.draw(g);
		myTank.eatBlood(b);
		for(int i=0;i<missiles.size();i++) {
			Missile m = missiles.get(i);
			m.hitTank(myTank);
			m.hitTanks(tanks);
			m.draw(g);
			m.hitWall(w1);
			m.hitWall(w2);
		}	
		
		for(int i=0;i<explodes.size();i++) {
			Explode e = explodes.get(i);
			e.draw(g);			
		}
		
		for(int i=0;i<tanks.size();i++) {
			Tank t = tanks.get(i);
			t.collidesWithTanks(tanks);
			t.collidesWithTank(myTank);
			t.collidesWithWall(w1);
			t.collidesWithWall(w2);
			t.draw(g);
		}
		
		
		
	}
	
	@Override
	public void update(Graphics g) {
		if(offScreenImage == null) {
			offScreenImage = this.createImage(GAME_WIDTH,GAME_HEIGHT);			
		}
		Graphics gOffScreen = offScreenImage.getGraphics();
		Color c = g.getColor();
		gOffScreen.setColor(Color.GREEN);
		gOffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
		gOffScreen.setColor(c);
		paint(gOffScreen);
		g.drawImage(offScreenImage, 0, 0, null);
		
	}

	public void launchFrame() {		
		for(int i=0; i<10; i++) {
			tanks.add(new Tank(50 + 50 *(i+1), 50, false, Direction.D, this));
		}
		
		this.setLocation(400,100);
		this.setSize(GAME_WIDTH,GAME_HEIGHT);
		this.setTitle("TankWar");
		this.addWindowListener(new WindowAdapter() {
		
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
			
		});
		this.setResizable(false);
		this.setBackground(Color.GREEN);
		this.setVisible(true);
		
		new Thread(new PaintThread()).start();
		this.addKeyListener(new KeyMonitor());
	}
	
	public static void main(String[] args) {
		TankClient tc = new TankClient();
		tc.launchFrame();

	}
	
	private class PaintThread implements  Runnable {

		@Override
		public void run() {
			while(true) {
				repaint();
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}				
						
		}
		
	}
	
	private class KeyMonitor extends KeyAdapter {

		@Override
		public void keyReleased(KeyEvent e) {
			myTank.keyReleased(e);
			b.keyRealesed(e);
		}

		@Override
		public void keyPressed(KeyEvent e) {
			myTank.keyPressed(e);
		}
		
	}

}
