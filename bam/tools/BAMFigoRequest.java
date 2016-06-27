package bam.tools;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

import bam.core.BAMAccount;
import bam.core.BAMTransaction;
import bam.core.BAMUser;
import me.figo.*;
import me.figo.internal.*;
import me.figo.models.*;

public class BAMFigoRequest {

	//Figo Connection Data provided for this service
	private static final String CLIENT_ID = "CBDFoiLw4yAzCEfL7IjswjzGadvxRQC0-3R2kQvGIPP0";
	private static final String SECRET = "SrFsH94TJ8lqjL2866EXb8WT5uTRWq8u2tFWneaecj5M";
	
	private BAMUser user;
	private FigoSession session;
	private FigoConnection connection;


	public BAMFigoRequest( BAMUser user )
	{
		this.user = user;
	}


	public void startSession( ) throws BAMException
	{
		connection = new FigoConnection( CLIENT_ID , SECRET, "http://localhost:3000/callback");
		TokenResponse tokres = null;
		try{
			
			tokres = connection.credentialLogin(user.getFigoAccount(), user.getFigoPW());
		}
		catch( Exception e )
		{
			throw new BAMException( "Figo Login Error.", e );
			
		} 
		session = new FigoSession(tokres.access_token);
	}

	public Account getAccount( String accountName ) throws BAMException
	{
		try{
			for( Account a : session.getAccounts() )
				if(a.getName().equals(accountName) )
					return a;
		}
		catch(IOException e){
			throw new BAMException( "Figo Connection Error.", e );
		}
		catch(FigoException e){
			throw new BAMException( "Account \"" + accountName + "\" not found.", e );
		}
		return null;
	}

	public List<BAMTransaction> getNewTransactions( BAMAccount account ) throws BAMException
	{
		List<BAMTransaction> tList = new ArrayList<BAMTransaction>();
		try{
			for( Account a : session.getAccounts() )
				if(a.getName().equals(account.toString()) )
					for( Transaction t : session.getTransactions(a) )
						if( t.isBooked() && !account.containsTransaction(t.getTransactionId()) )
								tList.add( new BAMTransaction(t) );
		}
		catch(Exception e){
			throw new BAMException( "Figo Connection Error.", e );
		}
		return tList;
		
	}
	
	public BigDecimal getBalance( BAMAccount account ) throws BAMException
	{
		try{
			for( Account a : session.getAccounts() )
				if(a.getName().equals(account.toString()) )
					return a.getBalance().getBalance();
		}
		catch(Exception e){
			throw new BAMException( "Figo Connection Error.", e );
		}
		throw new BAMException("Account " + account + " not found." );
	}

	public void synchronizeAccounts() throws BAMException
	{
		try {
			TaskTokenResponse response = session.queryApi("/rest/sync", new SyncTokenRequest("asdfghjkl", "http://localhost:3000/callback"), "POST", TaskTokenResponse.class);
			TaskStatusResponse status = session.submitResponseToTask(response, user.getBankPin() , TaskResponseType.PIN );
			while(!status.isEnded())
			{
				status = session.submitResponseToTask(response, user.getBankPin() , TaskResponseType.PIN );
				synchronized(this)
				{
					this.wait(300);
				}
			}
		} catch (FigoException | IOException e ) {
			throw new BAMException("Synchronizing Account with Bank Server Error! ", e);
		} catch (InterruptedException e) {
		}
	}
}
