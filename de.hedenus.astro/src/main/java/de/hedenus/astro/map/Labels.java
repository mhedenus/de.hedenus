package de.hedenus.astro.map;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Labels implements Serializable, Iterable<Label>
{
	private static final long serialVersionUID = -7076878111448624945L;

	private final Settings settings;
	private final List<Label> labels = new ArrayList<>();

	public Labels(final Settings settings)
	{
		this.settings = settings;
	}

	@Override
	public Iterator<Label> iterator()
	{
		return labels.iterator();
	}

	public void add(final Label starLabel)
	{
		this.labels.add(starLabel);
	}

	public void layout()
	{
		final int len = labels.size();
		for (int pass = 1; pass <= settings.starLabelLayoutPasses; pass++)
		{
			int overlaps = 0;

			for (int i = 0; i < len; i++)
			{
				Label label1 = labels.get(i);

				for (int j = i + 1; j < len; j++)
				{
					Label label2 = labels.get(j);

					if (label1.layout().intersects(label2.layout()))
					{
						overlaps++;

						label2.flip();
						for (int k = 0; k < 3; k++)
						{
							if (label1.layout().intersects(label2.layout()))
							{
								label2.flip();
							}
							else
							{
								break;
							}
						}

					}

				}
			}

			System.out.println("Pass " + pass + ": " + overlaps);

		}
	}
}
