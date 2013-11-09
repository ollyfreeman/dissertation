package preparation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class DrawingPanel extends JPanel{

	Map map;
	int width;
	int height;
	int multiplier = 4;
	List<Cell> path;

	public DrawingPanel(Map map, int width, int height, List<Cell> path) {
		this.map = map;
		this.width = width;
		this.height = height;
		this.path = path;
		setBorder(BorderFactory.createLineBorder(Color.black));
	}

	public Dimension getPreferredSize() {
		return new Dimension(width*multiplier,height*multiplier);
	}

	public void paintComponent(Graphics g) {      

		//draw map
		for(int y=0;y < map.getHeight(); y++) {
			for(int x=0; x < map.getWidth(); x++) {
				if(map.getCell(x,y).isBlocked()) {
					g.setColor(Color.BLACK);
					g.fillRect(x*multiplier, y*multiplier, multiplier, multiplier);
				}
			}
		}
		//overlay path
		int i = 0;
		while(i+1 < path.size()) {
			Cell from = path.get(i);
			Cell to = path.get(i+1);
			g.setColor(Color.RED);
			g.drawLine((from.getX()*multiplier)+(multiplier/2), (from.getY()*multiplier)+(multiplier/2), (to.getX()*multiplier)+(multiplier/2), (to.getY()*multiplier)+(multiplier/2));
			i++;
		}
	} 
}
