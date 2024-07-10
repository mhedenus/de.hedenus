package de.hedenus.astro.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.function.Supplier;

import de.hedenus.astro.AstroException;

public class FileCache
{
	private static final FileCache instance = new FileCache();

	public static FileCache instance()
	{
		return instance;
	}

	private final File dir = new File(".cache");

	public FileCache()
	{
		if (!dir.exists())
		{
			if (!dir.mkdirs())
			{
				throw new AstroException("Cannot create dir: " + dir.getAbsolutePath());
			}
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T get(final String fileName, final Supplier<T> supplier)
	{
		T result;
		File serFile = serFile(fileName);
		if (serFile.exists())
		{
			try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(serFile)))
			{
				result = (T) objectInputStream.readObject();
			}
			catch (IOException | ClassNotFoundException ex)
			{
				throw new AstroException(ex);
			}
			Log.info("Found in cache: " + fileName);
		}
		else
		{
			result = supplier.get();
			put(serFile, result);

		}
		return result;
	}

	private File serFile(final String fileName)
	{
		return new File(dir, fileName + ".ser");
	}

	public void put(final String fileName, final Object object)
	{
		put(serFile(fileName), object);
	}

	public void put(final File serFile, final Object object)
	{
		try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(serFile)))
		{
			objectOutputStream.writeObject(object);
		}
		catch (IOException ex)
		{
			throw new AstroException(ex);
		}
	}

}
