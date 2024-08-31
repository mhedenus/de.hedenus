package de.hedenus.astro.map;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class Labels implements Serializable, Iterable<Label>
{
	private static final long serialVersionUID = -7076878111448624945L;

	private final List<Label> labels = new ArrayList<>();

	@Override
	public Iterator<Label> iterator()
	{
		return labels.iterator();
	}

	public void add(final Label starLabel)
	{
		this.labels.add(starLabel);
	}

	public static class LayoutSolution implements Serializable
	{
		private static final long serialVersionUID = -3936352496314995116L;
		public int overlaps;
		public List<Rectangle> layout;
	}

	public LayoutSolution layout(final int starLabelLayoutPasses)
	{
		LayoutSolution solution = new LayoutSolution();

		final int len = labels.size();

		for (int i = 0; i < len; i++)
		{
			if (labels.get(i).position() >= 0)
			{
				labels.get(i).position((int) (8 * Math.random()));
				//labels.get(i).position(0);
			}
		}

		int overlaps = 0;
		for (int pass = 1; pass <= starLabelLayoutPasses; pass++)
		{
			overlaps = 0;
			for (int i = 0; i < len; i++)
			{
				Label label1 = labels.get(i);

				for (int j = i + 1; j < len; j++)
				{
					Label label2 = labels.get(j);

					if (label2.position() >= 0 && label1.layout().intersects(label2.layout()))
					{
						overlaps++;
						label2.rotate();

						for (int k = 0; k < 7; k++)
						{
							if (label1.layout().intersects(label2.layout()))
							{
								label2.rotate();
							}
							else
							{
								break;
							}
						}

					}
				}
			}
		}

		solution.overlaps = overlaps;
		solution.layout = this.labels.stream().map(l -> new Rectangle(l.layout())).collect(Collectors.toList());

		return solution;
	}

	public void draw(final Graphics2D g2d, final LayoutSolution layoutSolution)
	{
		for (int i = 0; i < labels.size(); i++)
		{
			labels.get(i).draw(g2d, layoutSolution.layout.get(i));
		}

	}
}
