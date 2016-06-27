package bam.core;

import java.util.*;

public abstract class BAMModifyableListable extends BAMAbstractListable {

	
	
	private List<BAMModifiedListener> modListeners = new ArrayList<>();

	public void addModifiedListener( BAMModifiedListener ml )
	{
		modListeners.add(ml);
	}
	
	public void removeModifiedListener( BAMModifiedListener ml )
	{
		int i = modListeners.indexOf( ml );
		if( i > -1 )
			modListeners.remove(i);
	}

	protected void fireModifiedEvents()
	{
		BAMModifiedEvent e = new BAMModifiedEvent( this );
		for( BAMModifiedListener ml : modListeners )
			ml.modified(e);	
	}	
	
}