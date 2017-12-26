package lander;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Observable;
import java.util.Observer;

// the edit toolbar
public class ToolBarView extends JPanel implements Observer {

    JButton undo = new JButton("Undo");
    JButton redo = new JButton("Redo");
    private GameModel model;

    public ToolBarView(GameModel model) {

        setLayout(new FlowLayout(FlowLayout.LEFT));

        
        this.model = model;
        // prevent buttons from stealing focus
        undo.setFocusable(false);
        redo.setFocusable(false);

        add(undo);
        add(redo);
		undo.setEnabled(false);
		redo.setEnabled(false);

        model.addObserver(this);
        
        MouseAdapter undoListener = new MouseAdapter() {

			public void mouseClicked (MouseEvent e) {
			
				model.undoAction();
			}

        	
        	
        };
        undo.addMouseListener(undoListener);
        
        
        
        MouseAdapter redoListener = new MouseAdapter() {

			public void mouseClicked (MouseEvent e) {
			
				model.redoAction();
			}

        	
        	
        };
        redo.addMouseListener(redoListener);
    }
    
    

    @Override
    public void update(Observable o, Object arg) {
    	if(!model.canRedo()) {
    		redo.setEnabled(false);
    	} else {
    		redo.setEnabled(true);
    	}
    	if(!model.canUndo()) {
    		undo.setEnabled(false);
    	} else {
    		undo.setEnabled(true);
    	}
    	repaint();
    }
}
