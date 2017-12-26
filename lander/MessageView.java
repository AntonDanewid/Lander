package lander;
import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class MessageView extends JPanel implements Observer {

    // status messages for game
    JLabel fuel = new JLabel("fuel");
    JLabel speed = new JLabel("speed");
    JLabel message = new JLabel("message");
    private GameModel model;

    public MessageView(GameModel model) {

        // want the background to be black
        setBackground(Color.BLACK);

        setLayout(new FlowLayout(FlowLayout.LEFT));

        add(fuel);
        add(speed);
        add(message);
        this.model = model;
        model.addObserver(this);

        for (Component c: this.getComponents()) {
            c.setForeground(Color.WHITE);
            c.setPreferredSize(new Dimension(100, 20));
        }
    }


    public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		if(model.ship.getFuel() < 10 ) {
			fuel.setForeground(Color.RED);
		} else {
			fuel.setForeground(Color.WHITE);
		}
		fuel.setText("fuel: "+ Double.toString(model.ship.getFuel()));
		if(model.ship.isPaused()) {
			message.setText("PAUSED");
		}
		message.setText(model.getMessage());
		if(model.ship.getSafeLandingSpeed() >= model.ship.getSpeed()) {
			speed.setForeground(Color.GREEN);
		} else {
			speed.setForeground(Color.WHITE);
		}
		speed.setText("speed: " + Double.toString(model.ship.getSpeed()));

		
		
    }
		
		
    @Override
    public void update(Observable o, Object arg) {
    	repaint();
    }
}