package lander;

import java.awt.Polygon;
import java.awt.geom.Point2D;

public class TerrainAction implements Action{

	
	private Point2D circle;
	private int y;
	private Polygon p;
	private int index;
	
	public TerrainAction(Point2D circle, Polygon p, int y, int i) {
		this.circle = circle;
		this.y = y; 
		this.p = p;
		index = i;
		
	}
	
	
	
	@Override
	public void execute() {
		p.ypoints[index] = y;
		circle.setLocation(circle.getX(), y);
	
	}


	@Override
	public Action update() {
		return new TerrainAction(circle, p, (int) circle.getY(), index);
		
	}



	
	
}
