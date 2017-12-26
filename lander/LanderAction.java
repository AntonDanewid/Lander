package lander;

import java.awt.geom.Rectangle2D;

public class LanderAction implements Action {

	
	private double x;
	private double y;
	private Rectangle2D.Double lander;
	
	public LanderAction(Rectangle2D.Double lander, double x, double y) {
		this.lander = lander;
		this.x  = x;
		this.y = y;
	}

	@Override
	public void execute() {
		lander.setRect(x, y, lander.width, lander.height);
		
	}

	@Override
	public Action update() {

		return new LanderAction(lander, lander.getX(), lander.getY());
		
	}
	
	

}
