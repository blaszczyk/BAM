package bam.tools;

public class BAMException extends Exception {
	
	private static final long serialVersionUID = -5709937488611212822L;
	
	private String errorMessage;
	private Exception cause;
	

	public BAMException(String errorMessage, Exception cause)
	{
		super(cause);
		this.errorMessage = errorMessage;
		this.cause = cause;
	}

	public BAMException(String errorMessage)
	{
		this(errorMessage,null);
	}
	
	@Override
	public void printStackTrace()
	{
		if(cause != null)
			cause.printStackTrace();
		else
			super.printStackTrace();
	}

	public String getErrorMessage() {
		return errorMessage;
	}
}