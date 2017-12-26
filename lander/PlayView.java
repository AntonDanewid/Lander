package lander;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Observable;
import java.util.Observer;

// the actual game view
public class PlayView extends JPanel implements Observer {

	private GameModel model;
	
    public PlayView(GameModel model) {

        // needs to be focusable for keylistener
        setFocusable(true);
        this.model = model;
        // want the background to be black
        setBackground(Color.BLACK);
        model.addObserver(this);
        
        
        KeyListener listener = new KeyListener() {

			@Override
		    public void keyPressed(KeyEvent e) {
				if(e.getKeyChar() == 'w') {
					model.ship.thrustUp();
				}
				if(e.getKeyChar() == ' ') {
					model.spacePressed();
				}
				if(e.getKeyChar() == 's') {
					model.ship.thrustDown();
				}
				
				if(e.getKeyChar() == 'a') {
					model.ship.thrustLeft();
				}
				if(e.getKeyChar() == 'd') {
					model.ship.thrustRight();
				}
				
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
        };
        this.addKeyListener(listener);

    }

    @Override
    public void update(Observable o, Object arg) {
    	repaint();
    }
    
    
    public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.LIGHT_GRAY);
		AffineTransform M = g2.getTransform();
		
		g2.scale(3, 3);
		g2.translate(-model.ship.position.x +100, -model.ship.position.y +50);
		g2.fill(model.getWorldBounds());
		g2.setColor(Color.DARK_GRAY);
		g2.fill(model.getTerrain());
		Rectangle2D ship = model.ship.getShape();
		g2.setColor(Color.BLUE);
		g2.fill(ship);
		g2.setColor(Color.RED);
		g2.fill(model.getLandingPad());
		g2.setTransform(M);
		
		
		
		
	}
    
}
