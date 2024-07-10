package de.hedenus.astro;

public class AstroException extends RuntimeException
{
	private static final long serialVersionUID = 799500146909147352L;

	public AstroException(final String message, final Throwable cause)
	{
		super(message, cause);
	}

	public AstroException(final String message)
	{
		super(message);

	}

	public AstroException(final Throwable cause)
	{
		super(cause);
	}
}
