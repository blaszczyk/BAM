package bam.tools;

import java.math.BigDecimal;
import java.text.*;
import java.util.*;

public class BAMFormats
{

	private static NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.GERMANY);
	private static DecimalFormat decimalFileFormat = (DecimalFormat) NumberFormat.getNumberInstance(Locale.ENGLISH);
	private static DateFormat dateDisplayFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
	private static DateFormat dateSaveFormat = new SimpleDateFormat("dd.MM.yyyy");

	private BAMFormats()
	{
	};

	public static Date parseDateSaveFormat(String in) throws BAMException
	{
		try
		{
			return dateSaveFormat.parse(in);
		}
		catch (ParseException pe)
		{
			throw new BAMException("Error parsing Date from " + in, pe);
		}
	}

	public static boolean isBigDec(String in)
	{
		try
		{
			decimalFileFormat.setParseBigDecimal(true);
			decimalFileFormat.parse(in);
			return true;
		}
		catch (ParseException pe)
		{
			return false;
		}
	}

	public static BigDecimal parseBigDec(String in)
	{
		try
		{
			decimalFileFormat.setParseBigDecimal(true);
			return (BigDecimal) decimalFileFormat.parse(in);
		}
		catch (ParseException pe)
		{
			return BigDecimal.ZERO;
		}
	}

	public static String currencyFormat(BigDecimal in)
	{
		return currencyFormat.format(in.doubleValue());
	}

	public static String dateFormat(Date in)
	{
		return dateDisplayFormat.format(in);
	}

	public static boolean isDateDisplayFormat(String in)
	{
		try
		{
			dateDisplayFormat.parse(in);
		}
		catch (ParseException e)
		{
			return false;
		}
		return true;
	}

	public static Date parseDateDisplayFormat(String in) throws BAMException
	{
		try
		{
			return dateDisplayFormat.parse(in);
		}
		catch (ParseException e)
		{
			throw new BAMException("Error Parsing Date from " + in);
		}
	}
}
