package bam.gui.tools;

import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import bam.gui.settings.BAMGUISettings;

@SuppressWarnings({ "deprecation", "serial" })
public class BAMDateSelect extends JPanel {

	public static final int GOTO_FIRST = 1;
	public static final int GOTO_LAST = 31;
	public static final int GOTO_PRESELECTION = 16;
	private int dateJump;
	
	private static BAMGUISettings guiSettings = BAMGUISettings.getInstance();
	
	private JComboBox<Integer> dateBox;
	private JComboBox<String> monthBox;
	private JComboBox<Integer> yearBox;
	
	private final String[] monthArray = new String[12];
	
	private Date today = new Date();

	private ActionListener refreshDateBox = e -> {
		int year = today.getYear() - yearBox.getSelectedIndex();
		int month = monthBox.getSelectedIndex();
		int date = dateBox.getSelectedIndex() + 1;
		dateBox.removeAllItems();
		for( int i = 1; i <= getNrOfDays(month, year); i++ )
			dateBox.addItem(i);
		switch(dateJump)
		{
		case GOTO_FIRST:
			dateBox.setSelectedIndex( 0 );
			return;
		case GOTO_LAST:
			dateBox.setSelectedIndex( getNrOfDays(month,year) - 1 );
			return;
		case GOTO_PRESELECTION:
			dateBox.setSelectedIndex( date - 1);
			return;			
		}
	};
	
	public BAMDateSelect() {
		this( new Date() );
	}

	public BAMDateSelect( Date preSelection ) {
		this( preSelection, GOTO_PRESELECTION );
	}
	
	public BAMDateSelect( int dateJump ) {
		this( new Date(), dateJump );
	}
	
	
	public BAMDateSelect( Date preSelection, int dateJump ) {
		
		initMonthArray();
		this.dateJump = dateJump;

		yearBox = new JComboBox<Integer>( intSequence( today.getYear()+1900, today.getYear()+1890 ) );
		monthBox = new JComboBox<String>( monthArray );
		dateBox = new JComboBox<Integer>( intSequence( 1, getNrOfDays(preSelection.getMonth(), preSelection.getYear()) ) );

		setDate( preSelection );
		
		yearBox.addActionListener(refreshDateBox);
		monthBox.addActionListener(refreshDateBox);
		
		setLayout( new BoxLayout(this, BoxLayout.LINE_AXIS));
		add(dateBox);
		add(monthBox);
		add(yearBox);
		add(Box.createHorizontalGlue());
	}	

	public Date getDate() {
		return getDate(0);
	}
		
	public Date getDate( int offset )
	{
		Date returnDate = new Date();
		int year = today.getYear() - yearBox.getSelectedIndex();
		int month = monthBox.getSelectedIndex();
		int date = dateBox.getSelectedIndex() + 1;
		returnDate.setYear( year );
		returnDate.setMonth( month );
		returnDate.setDate( date + offset);
		return returnDate;
	}
	
	public void setDate( Date date )
	{
		yearBox.setSelectedIndex( today.getYear() - date.getYear() );
		monthBox.setSelectedIndex( date.getMonth() );
		dateBox.setSelectedIndex( date.getDate() - 1 );
	}

	public void addActionListener(ActionListener l) {
		dateBox.addActionListener(l);
	}
	
	public void removeActionListener(ActionListener l) {
		dateBox.removeActionListener(l);
	}
	
	private void initMonthArray()
	{
		monthArray[0] = guiSettings.getPhrase("JAN");
		monthArray[1] = guiSettings.getPhrase("FEB");
		monthArray[2] = guiSettings.getPhrase("MAR");
		monthArray[3] = guiSettings.getPhrase("APR");
		monthArray[4] = guiSettings.getPhrase("MAY");
		monthArray[5] = guiSettings.getPhrase("JUN");
		monthArray[6] = guiSettings.getPhrase("JUL");
		monthArray[7] = guiSettings.getPhrase("AUG");
		monthArray[8] = guiSettings.getPhrase("SEP");
		monthArray[9] = guiSettings.getPhrase("OCT");
		monthArray[10] = guiSettings.getPhrase("NOV");
		monthArray[11] = guiSettings.getPhrase("DEC");
	}

	private static Integer[] intSequence( int firstValue, int lastValue)
	{
		if( firstValue < lastValue )
		{
			int length = lastValue - firstValue + 1;
			Integer[] result = new Integer[length];
			for( int i = 0; i < result.length; i++ )
				result[i] = firstValue + i;
			return result;
		}
		else
		{
			int length = firstValue - lastValue + 1;
			Integer[] result = new Integer[length];
			for( int i = 0; i < result.length; i++ )
				result[i] = firstValue - i;
			return result;
		}
	}
	
	private static int getNrOfDays( int month, int year )
	{
		switch(month)
		{
		case 1:
			if( year % 4 == 0) // Works until 28.02.2100 :)
				return 29;
			return 28;
		case 3: case 5: case 8: case 10:
			return 30;
		default:
			return 31;	
		}
	}
	
}