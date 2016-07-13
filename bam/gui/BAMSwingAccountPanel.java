package bam.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.*;
import java.math.BigDecimal;
import java.util.*;

import javax.swing.*;

import bam.controller.BAMController;
import bam.core.*;
import bam.gui.settings.BAMGUISettings;
import bam.gui.tools.*;


@SuppressWarnings({"serial","deprecation"})
public class BAMSwingAccountPanel extends JPanel implements BAMModifiedListener {

	private static BAMGUISettings guiSettings = BAMGUISettings.getInstance();
	private BAMController controller;
	
	private BAMAccount account;
	private BAMUser user;

	private JTabbedPane north = new JTabbedPane();
	private BAMSwingTwoTable infoTab = new BAMSwingTwoTable( 3 );
	private BAMSwingTwoTable selectionTab = new BAMSwingTwoTable( 3 );
	private BAMSwingTwoTable searchTab = new BAMSwingTwoTable( 3 );
	private BAMSwingTwoTable statsTab = new BAMSwingTwoTable( 3 );
	
	private static String searchName = "";
	private static String searchPurpose = "";
	private static Date fromDate = new Date();
	private static Date toDate = fromDate;
	
	private JCheckBox bDrawStored;
	private BAMDateSelect dsFromDate; 
	private BAMDateSelect dsToDate; 
	private JTextField tfSearchName;
	private JTextField tfSearchPurpose;

	private JPanel center = new JPanel(); 
	private Comparator<BAMTransaction> comparator = (t1,t2) -> t2.getDate().compareTo(t1.getDate());
	
	private List<BAMTransaction> tList;

	private class preSet {		
		private String name;
		private Date fromDate;
		private Date toDate;
		
		public preSet( String name, long daysAgo ) {
			this( name, new Date( new Date().getTime() - (daysAgo * 86400000) ) );
		}
		public preSet( String name, Date fromDate) {
			this( name, fromDate, new Date() );
		}		
		public preSet( String name, Date fromDate, Date toDate)
		{
			if( guiSettings.hasPhrase(name) )
				name = guiSettings.getPhrase(name);
			this.name = name;
			this.fromDate = fromDate;
			this.toDate = toDate;
		}
		@Override
		public String toString() { return name; }
		public Date getFromDate() {	return fromDate; }
		public Date getToDate() { return toDate; }		
	}

	private preSet[] preSets;
	

	private Action view = new AbstractAction(){
		@Override
		public void actionPerformed(ActionEvent e) {
			controller.openPopup( (BAMGenericPayment)e.getSource() );
		}			
	};

	private KeyListener redraw = new KeyAdapter(){
		@Override
		public void keyReleased(KeyEvent e) {
			redraw();
		}
	};	
	
	public BAMSwingAccountPanel( BAMAccount account, BAMUser user, BAMController controller ){
		this.account = account;
		this.user = user;		
		this.controller = controller;
		tList = account.getTransactions();
		drawNorth();
		drawCenter();
		setLayout( guiSettings.getBorderLayout() );
		add( north, BorderLayout.NORTH);
		add( center, BorderLayout.CENTER );
		setVisible(true);
	}
	

	private void drawInfoTab()
	{
		infoTab.setVisible( false );
		infoTab.removeAll();;
		infoTab.setLeftAppend("     ");
		infoTab.setMiddleAppend(" : ");
		infoTab.addRow( "ACCOUNT" ,account.toString());
		infoTab.addValues(account, BAMAccount.IBAN, BAMAccount.BALANCE );
		infoTab.setVisible( true );
	}
	
	private void drawSelectionTab()
	{
		bDrawStored = new JCheckBox( guiSettings.getPhrase("SHOW_STORED") );
		bDrawStored.setSelected( true );
		bDrawStored.addItemListener( e -> {
			redraw();
		});
		
		dsToDate = new BAMDateSelect( toDate, BAMDateSelect.GOTO_LAST );
		dsToDate.addActionListener( e -> {
			redraw();
		});
		
		dsFromDate = new BAMDateSelect( fromDate, BAMDateSelect.GOTO_FIRST );
		dsFromDate.addActionListener( e -> {
			redraw();
		});		

		JPanel datePanel = new JPanel();
		datePanel.setLayout( new BoxLayout(datePanel,BoxLayout.X_AXIS) );
		datePanel.add(dsFromDate);
		datePanel.add( new JLabel( guiSettings.getPhrase("SHOW_TO") + "   ", SwingConstants.RIGHT ) );
		datePanel.add(dsToDate);
		datePanel.add(Box.createHorizontalGlue() );

		initPreSets();
		JComboBox<preSet> preSetsBox = new JComboBox<>( preSets );
		preSetsBox.addActionListener( e -> {
			int index = preSetsBox.getSelectedIndex();
			dsFromDate.setDate( preSets[index].getFromDate() );
			dsToDate.setDate( preSets[index].getToDate() );
		});
		preSetsBox.setSelectedIndex(3);
		
		selectionTab.removeAll();
		selectionTab.setLeftAppend("     ");
		selectionTab.addRow("" , bDrawStored);
		selectionTab.setMiddleAppend( " : ");
		selectionTab.addRow("SHOW_TIMESPAN", preSetsBox);
		selectionTab.setMiddleAppend("   ");
		selectionTab.addRow( "SHOW_FROM", datePanel);
	}
	
	private void drawSearchTab()
	{
		tfSearchName = new JTextField( searchName );
		tfSearchName.addKeyListener( redraw ); 
		tfSearchPurpose = new JTextField( searchPurpose );
		tfSearchPurpose.addKeyListener( redraw );
		
		searchTab.removeAll();
		searchTab.setLeftAppend("     ");
		searchTab.setMiddleAppend("   ");
		searchTab.addRow("SEARCH", "");
		searchTab.setMiddleAppend(" : ");
		searchTab.addRow( "NAME", tfSearchName );
		searchTab.addRow( "PURPOSE", tfSearchPurpose );
	}
	
	private void drawStatsTab()
	{
		BigDecimal posSum = BigDecimal.ZERO;
		BigDecimal negSum = BigDecimal.ZERO;
		for( BAMTransaction t : tList)
			if( t.getAmount().signum() > 0 )
				posSum = posSum.add( t.getAmount() );
			else
				negSum = negSum.add( t.getAmount() );
		
		statsTab.removeAll();
		statsTab.setLeftAppend("     ");
		statsTab.setMiddleAppend(" : ");
		statsTab.addRow("INCOMING", posSum );
		statsTab.addRow("OUTGOING", negSum );
		statsTab.addRow("DIFFERENCE", posSum.add(negSum) );
	}
	
	private void drawNorth()
	{
		north.removeAll();
		drawInfoTab();
		drawSearchTab();
		drawSelectionTab();
		drawStatsTab();
		north.addTab(guiSettings.getPhrase("ACCOUNT_INFO"), infoTab);
		north.addTab(guiSettings.getPhrase("SELECTION"), selectionTab);
		north.addTab(guiSettings.getPhrase("SEARCH"), searchTab);
		north.addTab(guiSettings.getPhrase("STATISTICS"), statsTab);
	}

	private void drawCenter()
	{
		tList = new ArrayList<BAMTransaction>();
		for( BAMTransaction t : account.getTransactions() )
			if( bDrawStored.isSelected() || ! user.isTransactionStored( t ) )
				if( t.getDate().before( toDate ) && t.getDate().after( fromDate ) )
					if( t.getName().toLowerCase().contains( searchName.toLowerCase() ) )
						if( t.getPurpose().toLowerCase().contains( searchPurpose.toLowerCase() ) )
							tList.add(t);
		tList.sort( comparator );
		
		BAMListableTable<BAMTransaction> table = new BAMListableTable<>( tList, controller, BAMTransaction.NAME, BAMTransaction.DATE, 
				BAMTransaction.AMOUNT, BAMTransaction.PURPOSE);
		table.setColumnWidths(150,70,70,350);
		table.setDoubleClickAction(view);
		table.draw();
		
		center.setVisible(false);
		center.removeAll();
		center.setLayout( new GridLayout(1,1) );
		center.add(new JScrollPane( table ) );
		center.setVisible(true);
	}

	private void redraw()
	{
		fromDate = dsFromDate.getDate(-1);
		toDate = dsToDate.getDate();
		searchName = tfSearchName.getText();
		searchPurpose = tfSearchPurpose.getText();
		drawCenter();
		drawStatsTab();
	}
	
	private void initPreSets()
	{
		preSets = new preSet[6];
		Date date = new Date();
		preSets[0] = new preSet("THIS_MONTH", date.getDate());
		preSets[1] = new preSet("LAST_30_DAYS", 30);
		preSets[2] = new preSet("LAST_90_DAYS", 90);
		date.setDate(1);
		date.setMonth(0);
		date.setDate(-5);
		preSets[3] = new preSet("THIS_YEAR",date);
		Date date2 = new Date( date.getTime() - 366 * 86400000 );
		preSets[4] = new preSet("LAST_YEAR",date2,date);
		preSets[5] = new preSet("ALL", 3650 );
	}
	
	@Override
	public void modified(BAMModifiedEvent e) {
		if( e.getModfiedInstance() == account)			
			redraw();
	}
	
	@Override
	public void setVisible( boolean aFlag )
	{
		super.setVisible( aFlag );
		if( aFlag )
			account.addModifiedListener( this );
		else
			account.removeModifiedListener( this );
	}

}