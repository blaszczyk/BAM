package bam.core;

import bam.gui.settings.BAMGUISettings;

public class BAMModifiedEvent {

	public static final int UNKNOWN = 0;
	public static final int USER_MODIFIED = 1;
	public static final int ACCOUNT_MODIFIED = 2;
	public static final int SUBACCOUNT_MODIFIED = 4;
	public static final int GUISETTINGS_MODIFIED = 8;
	
	private final int modifiedType;
	
	private final BAMModifyableListable m;
	
	public BAMModifiedEvent ( BAMModifyableListable m )
	{
		this.m = m;
		if( m instanceof BAMUser )
			modifiedType = USER_MODIFIED;
		else if (m instanceof BAMAccount ) 
			modifiedType = ACCOUNT_MODIFIED;
		else if (m instanceof BAMSubAccount ) 
			modifiedType = SUBACCOUNT_MODIFIED;
		else if (m instanceof BAMGUISettings )
			modifiedType = GUISETTINGS_MODIFIED;
		else
			modifiedType = UNKNOWN;
	}
	
	public BAMModifyableListable getModfiedInstance()
	{
		return m;
	}
	
	public int getModifiedType()
	{
		return modifiedType;
	}

}
