package lander;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Observable;
import java.util.Observer;

// the editable view of the terrain and landing pad
public class EditView extends JPanel implements Observer {

	private GameModel model;
	private boolean drag = false;
	
	public EditView(GameModel model) {

		
		this.model = model;
		// want the background to be black
		setBackground(Color.BLACK);
		model.addObserver(this);
		MouseAdapter listener = new MouseAdapter() {

			@Override
			public void mouseClicked (MouseEvent e) {
				System.out.println(e.getX());
				if(e.getClickCount() == 2) {
					model.snapPad(e.getX(), e.getY());
				
				}
				
			}
			
		@Override
		public void mousePressed(MouseEvent e) {
			model.click(e.getX(), e.getY());

		}
			
		@Override
		public void mouseDragged(MouseEvent e) {
				model.drag(e.getX(), e.getY());
			}

		
		@Override
		public void mouseReleased(MouseEvent e) {
			model.mouseReleased();
		}


			

		};
		this.addMouseListener(listener);
		this.addMouseMotionListener(listener);

	}

	@Override
	public void update(Observable o, Object arg) {
		repaint();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		Rectangle2D world = model.getWorldBounds();
		g2.setColor(Color.LIGHT_GRAY);
		g2.fill(world);
		g2.setColor(Color.DARK_GRAY);
		g2.fillPolygon(model.getTerrain());
		g2.setColor(Color.RED);
		Rectangle2D pad = model.getLandingPad();
		g2.fillRect((int) pad.getX(),(int) pad.getY(),(int) pad.getWidth(),(int) pad.getHeight());
		
		
		g2.setColor(Color.GRAY);
		for(Point2D p: model.getcircleList()) {
			
			g2.drawArc((int) (p.getX()-7.5),  (int) (p.getY()-7.5), 15, 15, 0, 360);
		}
		g2.setColor(Color.BLUE);
		g2.fill(model.ship.getShape());
		
		
		
	}
	

}
