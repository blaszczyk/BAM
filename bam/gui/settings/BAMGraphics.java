package bam.gui.settings;

import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class BAMGraphics
{
	public static final String USER = "user.png";
	public static final String ACCOUNT = "account.png";
	public static final String SUBACCOUNT = "subaccount.png";
	public static final String PAYMENT_LIST = "paymentlist.png";
	public static final String MULTIPAYMENT_LIST = "multipaymentlist.png";
	public static final String PAYMENT = "payment.png";
	public static final String MULTIPAYMENT = "multipayment.png";

	public static final String ICON1 = "icon1.bmp";
	public static final String ICON2 = "icon2.bmp";
	public static final String ICON3 = "icon3.bmp";
		
	private static Map<String,BufferedImage> images = new HashMap<>();
	
	private BAMGraphics()
	{
//		loadGraphics( USER, ACCOUNT, SUBACCOUNT, PAYMENT_LIST, MULTIPAYMENT_LIST, PAYMENT, MULTIPAYMENT);
	}
	
	private static  void loadGraphics( String... filenames )
	{
		for( String filename : filenames )
			loadGraphic( filename);
	}
	
	
	
	private static void loadGraphic( String filename )
	{
		String path = "data/";
		BufferedImage img;
		try
		{
			img = ImageIO.read( new File( path + filename ) );
		}
		catch (IOException e)
		{
			System.err.println("Image " + filename + " not found.");
			byte[] b = { (byte) 0}; 
			IndexColorModel icm = new IndexColorModel(8, 1, b, b, b);
			img = new BufferedImage(16, 16, BufferedImage.TYPE_BYTE_INDEXED,icm);
		}
		images.put( filename, img );
	}
	
	public static BufferedImage getImage( String filename )
	{
		if(! images.containsKey(filename))
			loadGraphics(filename);
		return images.get(filename);
	}
	
	public static ImageIcon getImageIcon( String filename )
	{
		return new ImageIcon( getImage( filename) );
	}
	
}
