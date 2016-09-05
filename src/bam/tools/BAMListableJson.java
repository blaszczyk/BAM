package bam.tools;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import bam.core.BAMListable;

/*
 * Allowed Structure:
 * Listable = {values,arrays} | {values}
 * values = value | values,value
 * value = String | BigDecimal | Date
 * arrays = array | arrays,array
 * array = List<String> | List<Listable> 
 * 
 */


public class BAMListableJson {

	public static void writeListable( BAMListable listable, JsonWriter writer) throws IOException
	{
		Iterator<String> keyIterator = listable.getKeyIterator();
		writer.beginObject();
		while( keyIterator.hasNext() )
		{
			String key = keyIterator.next();
			if( listable.isList(key) )
				writeArray( key, (List<?>)listable.getValue(key), writer );
			else
			{
				String value;
				if( listable.getClass(key) == Date.class )
					value = BAMFormats.dateFormat( (Date)listable.getValue(key)  );
				else
					value = listable.getValue(key).toString();
				writer.name(key).value(value);
			}
		}
		writer.endObject();		
		writer.flush();
	}
	
	public static void writeArray( String key, List<?> array, JsonWriter writer ) throws IOException
	{
		writer.name(key).beginArray();
		if( !array.isEmpty() )
		{
			if( isListable( array.get(0).getClass() ) )
				for( Object o : array )
					writeListable( (BAMListable) o, writer );
			else
				for( Object o : array )
				{
					writer.beginObject();
					writer.name(key).value( o.toString() );
					writer.endObject();
				}
		}
		writer.endArray();
		writer.flush();
	}
	
	public static void readListable( BAMListable listable, JsonReader reader ) throws IOException
	{
		reader.beginObject();
		while( reader.hasNext() )
		{
			String key = reader.nextName();
			if( ! listable.containsKey(key) )
			{
				reader.nextString();
				continue;
			}
			if( listable.isList(key) )
			{
				listable.setValue(key, readArray( reader, listable.getClass(key)) );
			}
			else
			{
				String value = reader.nextString();
				if( listable.getClass(key) == Date.class )
					try {
						listable.setValue( key, BAMFormats.parseDateSaveFormat( value ) );
					} catch (BAMException e) {
						System.err.println(e.getErrorMessage());
						e.printStackTrace();
					}
				else if( listable.getClass(key) == BigDecimal.class )
					listable.setValue( key, BAMFormats.parseBigDec( value ) );
				else
					listable.setValue( key, value );
			}
		}
		reader.endObject();
	}
	
	public static <T> List<T> readArray( JsonReader reader, Class<T> type ) throws IOException
	{
		List<T> list = new ArrayList<T>();
		reader.beginArray();
		while( reader.hasNext() )
		{
			if( isListable( type ) )
			{
				try {
					BAMListable listable;
					listable = (BAMListable) type.getConstructor().newInstance();
					readListable( listable, reader );
					list.add( type.cast( listable ) );
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException | SecurityException e) {
					System.out.println( type );
					e.printStackTrace();
				}				
			}
			else
			{
				reader.beginObject();
				reader.nextName();
				list.add( type.cast( reader.nextString() ) );
				reader.endObject();
			}
		}
		reader.endArray();
		return list;
	}
	

	public static JsonWriter getJsonWriter( Writer writer ) {
		return new JsonWriter( writer );
	}
	
	public static JsonReader getJsonReader( Reader reader ) {
		return new JsonReader( reader );
	}
	
	public static void saveListable( BAMListable listable, String path ) throws BAMException
	{
		FileWriter file;
		try {
			file = new FileWriter( path );
			writeListable(listable, getJsonWriter(file));
			file.close();
		} catch (IOException e) {
			throw new BAMException("Error Saving " + listable, e );
		}
	}
	
	public static void loadListable( BAMListable listable, String path ) throws BAMException
	{
		FileReader file;
		try	{
			file = new FileReader( path );
			readListable(listable, getJsonReader(file));
			file.close();
		} catch (IOException e) {
			throw new BAMException("Error Loading " + listable, e );
		}
	}	
	
	private static boolean isListable( Class<?> type )
	{
		return BAMListable.class.isAssignableFrom(type);
	}
}