package bam.gui.settings;

import java.util.Random;
import java.util.Scanner;

public class ZahlenRaten
{

	private static int readInt(Scanner in, String message)
	{
		while (true)
		{
			System.out.println(message);
			String line = in.nextLine();
			try
			{
				int result = Integer.parseInt(line);
				return result;
			}
			catch (NumberFormatException e)
			{
				System.out.println("Falsches Zahlenformat.");
			}
		}
	}

	public static void main(String[] args)
	{
		Random random = new Random();
		int number = random.nextInt(100) + 1;
		Scanner in = new Scanner(System.in);
		System.out.println("Raten Sie eine Zahl zwischen 1 und 100.");
		for (int nrGuesses = 0; nrGuesses < 7; nrGuesses++)
		{
			System.out.println("Sie haben noch " + (7 - nrGuesses) + " Versuch" + (nrGuesses == 6 ? "." : "e."));
			int guess = readInt(in, "Geben Sie eine ganze Zahl ein:");
			if (guess == number)
			{
				System.out.println("Juhuu! Richtig geraten!");
				in.close();
				return;
			}
			if (guess < number)
				System.out.println("Zu tief geraten.");
			else
				System.out.println("Zu hoch geraten.");
		}
		System.out.println("Leider Verloren. Die gesuchte Zahl war " + number + ".");
		in.close();

	}

}
