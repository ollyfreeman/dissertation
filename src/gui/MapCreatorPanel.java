package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import engine.Engine;

/*
 * Panel for creating Custom Maps
 */
public class MapCreatorPanel extends JPanel implements ActionListener{
	
	private static final long serialVersionUID = 1L;
	
	private final Engine engine;
	private final GUICoordinator coordinator;
	private final JButton createMapButton;
	private JComboBox<String> resolutionComboBox;
	private final JRadioButton brushButton;
	private JComboBox<String> brushComboBox;
	private final JRadioButton eraserButton;
	private JComboBox<String> eraserComboBox;
	private final JButton saveMapButton;

	public MapCreatorPanel(final Engine engine, final GUICoordinator coordinator) {
		
		this.engine = engine;
		this.coordinator = coordinator;
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.fill = GridBagConstraints.VERTICAL;
	    c.gridx = 0;
	    c.gridy = 0;
	    
	    JLabel titleLabel = new JLabel("Map Creator");
		this.add(titleLabel,c);
		
		c.gridy = 1;
		createMapButton = new JButton("New Creation");
		createMapButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				createMapButton.setEnabled(false);
				resolutionComboBox.setEnabled(false);
				saveMapButton.setEnabled(true);
				String[] resolutionArray = data.MapResolutions.getMapResolutionArray();
				String resolutionString = resolutionArray[resolutionComboBox.getSelectedIndex()];
				String[] widthAndHeightInCells = resolutionString.split("x");int counter = 0;
				
				for(int i = 64; i >= 800/Integer.parseInt(widthAndHeightInCells[1]); i /=2) {
					counter++;
				}
				String[] generalBrushResolutionArray = data.MapResolutions.getBrushResolutionArray();
				String[] currentBrushSizeArray = new String[counter];
				for(int i = 0; i<counter; i++) {
					currentBrushSizeArray[i] = generalBrushResolutionArray[i];
				}
				brushComboBox.setModel(new DefaultComboBoxModel<String>(currentBrushSizeArray));
				eraserComboBox.setModel(new DefaultComboBoxModel<String>(currentBrushSizeArray));
				coordinator.setBrushSize(Integer.parseInt(data.MapResolutions.getBrushResolutionArray()[0]));
				brushComboBox.setEnabled(true);
				eraserComboBox.setEnabled(true);
				
				coordinator.startEditMode(Integer.parseInt(widthAndHeightInCells[0]),Integer.parseInt(widthAndHeightInCells[1]));
			}
		});
		this.add(createMapButton,c);
		
		c.gridwidth = 2;
		c.gridx = 1;
		resolutionComboBox = new JComboBox<String>(data.MapResolutions.getMapResolutionArray());
		this.add(resolutionComboBox,c);
		
		c.gridy = 2;
		c.gridx = 0;
		brushButton = new JRadioButton("Brush");
		//brushButton.setMnemonic(KeyEvent.VK_B);
		brushButton.setActionCommand("Brush");
		this.add(brushButton,c);
		
		c.gridx = 1;
		brushComboBox = new JComboBox<String>(data.MapResolutions.getBrushResolutionArray());
		brushComboBox.setEnabled(false);
		brushComboBox.setActionCommand("Brush");
		brushComboBox.addActionListener(this);
		coordinator.setBrushSize(Integer.parseInt(data.MapResolutions.getBrushResolutionArray()[0]));
		this.add(brushComboBox,c);
		
		c.gridy=3;
		c.gridx=0;
		eraserButton = new JRadioButton("Eraser");
		//eraserButton.setMnemonic(KeyEvent.VK_E);
		eraserButton.setActionCommand("Eraser");
		this.add(eraserButton,c);
		
		c.gridx=1;
		eraserComboBox = new JComboBox<String>(data.MapResolutions.getBrushResolutionArray());
		eraserComboBox.setEnabled(false);
		eraserComboBox.setActionCommand("Eraser");
		eraserComboBox.addActionListener(this);
		this.add(eraserComboBox,c);
		
		ButtonGroup group = new ButtonGroup();
		group.add(brushButton);
		group.add(eraserButton);
		brushButton.addActionListener(this);
		eraserButton.addActionListener(this);
		brushButton.setSelected(true);
		
		c.gridy=4;
		saveMapButton = new JButton("Creation finished");
		saveMapButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				createMapButton.setEnabled(true);
				resolutionComboBox.setEnabled(true);
				saveMapButton.setEnabled(false);
				brushButton.setSelected(true);
				brushComboBox.setEnabled(false);
				eraserComboBox.setEnabled(false);
				int[][] array = coordinator.stopEditMode();
				engine.plotMapFrom2DArray(array);
			}
		});
		this.saveMapButton.setEnabled(false);
		this.add(saveMapButton,c);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Brush")) {
			if(brushButton.isSelected()) {
				coordinator.getDrawingPanel().toBrush(true);
				String[] brushArray = data.MapResolutions.getBrushResolutionArray();	
				coordinator.setBrushSize(Integer.parseInt(brushArray[brushComboBox.getSelectedIndex()]));
			}
		} else {
			if(eraserButton.isSelected()) {
				coordinator.getDrawingPanel().toBrush(false);
				String[] brushArray = data.MapResolutions.getBrushResolutionArray();	
				coordinator.setBrushSize(Integer.parseInt(brushArray[eraserComboBox.getSelectedIndex()]));
			}
		}
	}

}
