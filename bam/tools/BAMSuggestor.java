package bam.tools;

import java.util.ArrayList;
import java.util.List;

import bam.core.*;

public class BAMSuggestor
{

	private List<BAMGenericPayment> suggestions = new ArrayList<>();
	private BAMTransaction transaction;
	private BAMUser user;

	public BAMSuggestor(BAMTransaction transaction, BAMUser user)
	{
		this.transaction = transaction;
		this.user = user;
	}

	public BAMSuggestor findSuggestion()
	{
		for (BAMAccount a : user.getAccounts())
			for (BAMSubAccount sa : a.getSubAccounts())
			{
				for (BAMPayment p : sa.getPayments())
					if (p.suggestForTransaction(transaction))
						suggestions.add(p);
				for (BAMMultiPayment mp : sa.getMultiPayments())
					if (mp.suggestForTransaction(transaction))
						suggestions.add(mp);
			}
		return this;
	}

	public int getSuggestionCount()
	{
		return suggestions.size();
	}

	public List<BAMGenericPayment> getSuggestions()
	{
		return suggestions;
	}

	public BAMGenericPayment getSuggestion(int index)
	{
		return suggestions.get(index);
	}
}
