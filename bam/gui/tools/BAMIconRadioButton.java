package bam.gui.tools;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

@SuppressWarnings("serial")
public class BAMIconRadioButton extends JPanel {

	JRadioButton button;
	JButton iconbutton;
	
	public BAMIconRadioButton( BufferedImage icon ) {
		super( new BorderLayout(0,0) );
		button = new JRadioButton();
		iconbutton = new JButton( new ImageIcon( icon) );
		
		iconbutton.addActionListener( e -> {
			button.setSelected(true);
		});
		iconbutton.setSize( new Dimension( icon.getHeight(), icon.getWidth()  ) );

		add(button, BorderLayout.WEST);
		add(iconbutton, BorderLayout.CENTER);
	}

	public BAMIconRadioButton( BufferedImage icon, boolean isSelected) {
		this( icon );
		button.setSelected(isSelected);
	}

	public BAMIconRadioButton( BufferedImage icon, BufferedImage selectedIcon ) {
		this( icon, icon.equals(selectedIcon) );
	}
	
	public JRadioButton getRadioButton()
	{
		return button;
	}
	
	public void addActionListener( ActionListener l)
	{
		button.addActionListener(l);
		iconbutton.addActionListener(l);
	}
	
	public void removeActionListener( ActionListener l)
	{
		button.removeActionListener(l);
		iconbutton.removeActionListener(l);
	}	
}
