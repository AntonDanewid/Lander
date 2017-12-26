package lander;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.Stack;

import javax.swing.undo.*;
import javax.vecmath.*;

public class GameModel extends Observable {

	private Rectangle2D.Double landingPad;
	private Polygon terrain;
	private boolean currentpad = false;
	private boolean currentC = false;
	private boolean running;
	private int lol = 0;
	private Point2d resetPos;
	private boolean won = false;
	private String messageString = "";
	private Stack<Action> undo;
	private Stack<Action> redo;


	private int currentCircle;

	// Create hashmap for the circles and polygon points

	private ArrayList<Point2D> circlePoints;

	public GameModel(int fps, int width, int height, int peaks) {

		undo = new Stack<Action>();
		redo = new Stack<Action>();

		ship = new Ship(60, width / 2, 50);
		resetPos = new Point2d(width / 2, 50);
		landingPad = new Rectangle2D.Double(330, 100, 40, 10);

		worldBounds = new Rectangle2D.Double(0, 0, width, height);
		running = true;
		circlePoints = new ArrayList<Point2D>();
		// anonymous class to monitor ship updates
		ship.addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				gameLogic();
				setChangedAndNotify();
			}
		});

		Random rand = new Random();
		terrain = new Polygon();
		terrain.addPoint(0, height);
		for (int i = 0; i < 21; i++) {
			int randH = height / 2 + rand.nextInt(height / 2);
			terrain.addPoint(i * width / 20, randH);
			circlePoints.add(new Point2D.Double(i * width / 20, randH));

		}

		terrain.addPoint(width, height);

	}

	// World
	// - - - - - - - - - - -
	public final Rectangle2D getWorldBounds() {
		return worldBounds;
	}

	Rectangle2D.Double worldBounds;

	// Ship
	// - - - - - - - - - - -

	public Ship ship;

	// Observerable
	// - - - - - - - - - - -

	private void gameLogic() {
		if (running) {
			if (terrain.intersects(ship.getShape())) {

			ship.setPaused(true);
				messageString = "CRASHED";
				running = false;
				won = false;
			} else if (!worldBounds.contains(ship.getShape())) {
				won = false;
				running = false;
				ship.setPaused(true);
				messageString = "CRASHED";

			} else if (landingPad.intersects(ship.getShape()) && ship.getSafeLandingSpeed() > ship.getSpeed()) {
				won = true;
				running = false;
				ship.setPaused(true);
				messageString = "LANDED";

			} else if (landingPad.intersects(ship.getShape()) && ship.getSafeLandingSpeed() <= ship.getSpeed()) {
				won = false;
				running = false;
				ship.setPaused(true);
				messageString = "CRASHED";
			}
		}
	}

	void setChangedAndNotify() {

		setChanged();
		notifyObservers();
	}

	public void movePad(int x, int y) {
		if (worldBounds.contains(new Point(x, y))) {
			landingPad.setRect(x, y, landingPad.getWidth(), landingPad.getHeight());
			setChanged();
			notifyObservers();
		}
	}
	
	public void snapPad(int x, int y) {
		undo.add(new LanderAction(landingPad, landingPad.getX(), landingPad.getY()));
		landingPad.setRect(x, y, landingPad.getWidth(), landingPad.getHeight());
		setChanged();
		notifyObservers();
	}

	

	public Rectangle2D getLandingPad() {
		return landingPad;
	}

	public void click(int x, int y) {
		if (landingPad.contains(new Point(x, y))) {
			currentpad = true;
			undo.push(new LanderAction(landingPad, landingPad.getX(), landingPad.getY()));
			return;
		}
		for (int i = 0; i < circlePoints.size(); i++) {
			Point2D p = circlePoints.get(i);
			if (p.distance(x, y) < 7.5) {
				currentCircle = i;
				currentC = true;
				undo.add(new TerrainAction(circlePoints.get(i), terrain, y, i + 1));
			}
		}
	}

	public void drag(int x, int y) {
		if (currentC) {
			moveCircle(x, y);
		} else if (currentpad) {
			movePad(x, y);
		}
		setChanged();
		notifyObservers();
	}

	private void moveCircle(int x, int y) {
		if (worldBounds.contains(new Point(x, y))) {
			Point2D p = circlePoints.get(currentCircle);

			p.setLocation(p.getX(), y);
			terrain.ypoints[currentCircle + 1] = y;
		}

	}

	public void mouseReleased() {
		currentCircle = 0;
		currentC = false;
		currentpad = false;

	}

	public Polygon getTerrain() {
		return terrain;
	}

	public ArrayList<Point2D> getcircleList() {
		return circlePoints;
	}

	public void spacePressed() {
		if (!running) {
			ship.reset(resetPos);
			running = true;
		} else {
			ship.setPaused(!ship.isPaused());
			if (ship.isPaused()) {
				messageString = "(PAUSED)";
			} else {
				messageString = "";
			}

		}
	}

	public boolean haveWon() {
		return won;
	}

	public String getMessage() {
		return messageString;
	}

	public void undoAction() {
		if (undo.size() > 0) {
			Action a = undo.pop();
			Action r = a.update();
			a.execute();
			redo.push(r);
			setChanged();
			notifyObservers();
		}
	}
	
	public void redoAction() {
		if(redo.size() > 0) {
			Action a = redo.pop();
			Action r = a.update();
			a.execute();
			undo.add(r);
			setChanged();
			notifyObservers();
		}
	}
	
	public boolean canUndo() {
		return undo.size() > 0;
	}
	
	public boolean canRedo() {
		return redo.size() > 0;
	}

}
