package de.hedenus.astro.common;

public class Log
{
	public static void info(final String message)
	{
		System.out.println(message);
	}

	public static void infoBegin(final String message)
	{
		System.out.print(message);
	}

	public static void status(final String message)
	{
		System.out.print("\r" + message);
	}
}
