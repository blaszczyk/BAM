package bam.gui.settings;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.swing.JOptionPane;

import bam.tools.BAMException;

public class BAMLanguage {

	private Map<String,String> phrases = new HashMap<>();
	
	
	
	public BAMLanguage( Locale l ) throws BAMException
	{
		String language = l.getLanguage();
		if( language == Locale.GERMAN.getLanguage() )
			loadLanguage("deutsch");
		if( language == Locale.ENGLISH.getLanguage() )
			loadLanguage("english");
		JOptionPane.setDefaultLocale(l);
	}
	
	


	private void loadLanguage( String language ) throws BAMException
	{
		FileReader file = null;
		String line;
		try{
			file = new FileReader( "data/" + language + ".lang" );
		}
		catch(IOException e){
			throw new BAMException( "Error opening " + language + ".lang", e );
		}
		BufferedReader in = new BufferedReader( file );
		try{
			while( ( line = in.readLine() ) != null )
			{
				if( "%&!*/".contains( line.substring(0, 1) ) )
					continue;
				if( line.indexOf(' ') < 1 || line.indexOf('"') < 0 || line.lastIndexOf('"') < 0)
					continue;
				phrases.put( line.substring(0,line.indexOf(' ')) , line.substring( line.indexOf('"')+1, line.lastIndexOf('"') ) );
			}
		}
		catch( IOException e){
			throw new BAMException( "Error reading " + language + ".lang", e );
		}
		try {
			file.close();
		} catch (IOException e) {
			throw new BAMException( "Error closing " + language + ".lang", e );
		}
	}
	
	public String getPhrase( String key )
	{
		if( phrases.containsKey(key) )
			return phrases.get(key);
		System.out.println(key);
		return "Key " + key + " not Found";
	}

	public boolean hasPhrase(String key) 
	{
		return phrases.containsKey(key);
	}
	
}
