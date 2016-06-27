package bam.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import bam.core.BAMAccount;
import bam.core.BAMMultiPayment;
import bam.core.BAMSubAccount;
import bam.core.BAMTransaction;
import bam.core.BAMUser;

public class BAMSuggestMultiPayment {
	
	private List<BAMMultiPayment> suggestions = new ArrayList<>();
	private BAMTransaction transaction;
	private BAMUser user;
	
	public BAMSuggestMultiPayment(BAMTransaction transaction, BAMUser user)
	{
		this.transaction = transaction;
		this.user = user;
	}


	
	public BAMSuggestMultiPayment findSuggestion()
	{
		for( BAMAccount a : user.getAccounts())
			for( BAMSubAccount sa : a.getSubAccounts() )
				for( BAMMultiPayment mp : sa.getMultiPayments() )
					if( mp.suggestForTransaction(transaction) )
						suggestions.add(mp);
		return this;
	}
	
	public int getSuggestionCount()
	{
		return suggestions.size();
	}
	
	public List<BAMMultiPayment> getSuggestions()
	{
		return Collections.unmodifiableList( suggestions );
	}
	
	public BAMMultiPayment getSuggestion( int index)
	{
		return suggestions.get(index);
	}
}
