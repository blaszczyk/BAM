package bam.tools;

import java.math.BigDecimal;
import java.text.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;


public class BAMUtils {

/*
//***********************************************************	
	public static String strToLength( String in, int length )
	{
		if(in == null)
			return strToLength("null",length);
		if(length < 0) return "";
		if(in.length()>length) return in.substring(0,length);
		StringBuilder sb = new StringBuilder().append(in);
		for(int i = in.length(); i<length; i++)
			sb.append(' ');
		return sb.toString();
	}


//***********************************************************	
	public static String StrToLengthRight( String in, int length )
	{
		if(in == null)
			return StrToLengthRight("null",length);
		if(length < 0) return "";
		if(in.length()>length) return in.substring(0,length);
		StringBuilder sb = new StringBuilder();
		for(int i = in.length(); i<length; i++)
			sb.append(' ');
		return sb.append(in).toString();
	}
	

//***********************************************************
	public static String StrToLengthRight( BigDecimal in, int length )
	{
		return StrToLengthRight( in.toString(), length );
	}
	
//***********************************************************
	public static String IntToLengthRight( int in, int length)
	{
		return StrToLengthRight( "" + in , length);
	}
//*/
//***********************************************************
	public static Date parseDate( String in ) throws BAMException
	{		
		try{
			DateFormat dateform = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy");
			return dateform.parse(in);
		}
		catch(ParseException pe){
			throw new BAMException("Error parsing Date from " + in, pe);
		}		
	}

//***********************************************************	
	public static boolean isBigDec( String in)
	{		
		try{
			DecimalFormat decform = (DecimalFormat) NumberFormat.getInstance(Locale.ENGLISH);
			decform.setParseBigDecimal( true );
			decform.parse(in);
			return true;
		}
		catch(ParseException pe){
			return false;
		}		
	}
	
	public static BigDecimal toBigDec( String in)
	{		
		try{
			DecimalFormat decform = (DecimalFormat) NumberFormat.getInstance(Locale.ENGLISH);
			decform.setParseBigDecimal( true );
			return (BigDecimal) decform.parse(in);
		}
		catch(ParseException pe){
			return BigDecimal.ZERO;
		}		
	}

//***********************************************************	
	public static String DateDDMMYYYY( Date in )
	{
		DateFormat dateform = new SimpleDateFormat("dd.MM.yyyy");
		return dateform.format( in );
	}


	public static boolean isDateFormatDDMMYYYY( String in )
	{
		DateFormat dateform = new SimpleDateFormat("dd.MM.yyyy");
		try {
			dateform.parse( in );
		} catch (ParseException e) {
			return false;
		}
		return true;
	}
	
	public static Date toDateFormatDDMMYYYY( String in )
	{
		DateFormat dateform = new SimpleDateFormat("dd.MM.yyyy");
		try {
			return dateform.parse( in );
		} catch (ParseException e) {
			return null;
		}
	}

//***********************************************************
/*	public static boolean isInListRange( int index, List<?> list )
	{
		if(index < 0 || index >= list.size() )
			return false;
		return true;
	}

//***********************************************************	
	public static boolean isInListRange( String index, List<?> list )
	{
		int i;
		try{
			i = Integer.parseInt( index );
		}
		catch(Exception e){
			return false;
		}		
		if(i < 0 || i >= list.size() )
			return false;
		return true;
	}
	//*/

//***********************************************************	
	public static Date daysAgo( long days )
	{
		LocalDate ld = LocalDate.now().minusDays( days );
		return Date.from(ld.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}
	/*
	public static void setColumnWidths( JTable table, int ...widths)
	{
		for( int i = 0; i < widths.length; i++)
			table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);	
	}
	
	/*
	@SuppressWarnings("serial")
	public static void makeTableNumbersRed( JTable table, int col )
	{
		table.getColumnModel().getColumn(col).setCellRenderer( new DefaultTableCellRenderer(){
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				if( ! ( table.getValueAt(row, column) instanceof BigDecimal) )
					return c;
				if( ( (BigDecimal) table.getValueAt(row, column) ).signum() < 0 )
					c.setForeground( Color.RED );
				else
					c.setForeground( Color.BLACK );
				return c;
			}
		});
	}
	//*/
}	
