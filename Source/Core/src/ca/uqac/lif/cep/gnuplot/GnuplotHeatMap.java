/*
    BeepBeep, an event stream processor
    Copyright (C) 2008-2015 Sylvain Hallé

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ca.uqac.lif.cep.gnuplot;

import java.util.Queue;
import java.util.Stack;
import java.util.Vector;

import ca.uqac.lif.cep.Connector;
import ca.uqac.lif.cep.Processor;
import ca.uqac.lif.cep.eml.tuples.EmlNumber;
import ca.uqac.lif.cep.eml.tuples.NamedTuple;
import ca.uqac.lif.cep.sets.EmlBag;

/**
 * Generates a Gnuplot file from a 2D {@link ca.uqac.lif.cep.sets.EmlBag}.
 * The keys of the heatmap
 * are {@link NamedTuple}s of integers representing (x,y) coordinates; the
 * values of the heatmap are integers. When its method {@link #compute(Vector)}
 * is called, the processor returns a String containing a Gnuplot file
 * producing the graphical heatmap from the <em>last</em> map received as an
 * input.
 * <p>
 * Dimensions of the map must first be declared
 * through {@link #setDimensions(int, int, int, int)}.
 * It shall be noted that the map passed
 * to the processor does not need to define a value for every (x,y) coordinate
 * in the declared space; missing points are given the value zero.
 * <p>
 * Optionally, a title for the output
 * graph can be defined using {@link #setTitle(String)}.
 * @author sylvain
 *
 */
public class GnuplotHeatMap extends GnuplotProcessor 
{
	/**
	 * The preconfigured minimum value of the generated heat map
	 * along the x axis
	 */
	protected int m_minX;

	/**
	 * The preconfigured maximum value of the generated heat map
	 * along the x axis
	 */
	protected int m_maxX;

	/**
	 * The preconfigured minimum value of the generated heat map
	 * along the y axis
	 */
	protected int m_minY;

	/**
	 * The preconfigured maximum value of the generated heat map
	 * along the y axis
	 */
	protected int m_maxY;

	/**
	 * Scale factor for number of occurrences. All values in the heat map will be
	 * divided by this factor
	 */
	protected int m_scale = 1;

	/**
	 * The last heatmap that was sent to the processor. The output
	 * will only be generated from that map upon a call to 
	 * {@link #compute(Vector)}.
	 */
	protected EmlBag m_lastMap;

	/**
	 * The array that will contain the values of the heat map
	 */
	protected int[][] m_values;

	public GnuplotHeatMap()
	{
		super();
		m_lastMap = null;
		setDimensions(0, 0, 0, 0);
		m_title = "Sample heatmap generated by BeepBeep 3";
	}

	/**
	 * Sets the coordinate space for the heat map. The map will be drawn
	 * in the rectangle (x1, y1) - (x2, y2).
	 * @param x1 x coordinate of the rectangle
	 * @param y1 y coordinate of the rectangle
	 * @param x2 x coordinate of the rectangle
	 * @param y2 y coordinate of the rectangle
	 */
	public void setDimensions(int x1, int y1, int x2, int y2)
	{
		m_minX = x1;
		m_minY = y1;
		m_maxX = x2;
		m_maxY = y2;
		m_values = new int[x2 - x1][y2 - y1];
	}

	protected void processInput(EmlBag map)
	{
		m_lastMap = map;
	}

	/**
	 * Sets the scale factor for the number of occurrences
	 * @param scale The scale factor
	 */
	public void setScale(int scale)
	{
		m_scale = scale;
	}

	/**
	 * Fills the table of values with zeros
	 */
	protected void fillWithZeros()
	{
		for (int i = m_minX; i < m_maxX; i++)
		{
			for (int j = m_minY; j < m_maxY; j++)
			{
				m_values[i - m_minX][j - m_minY] = 0;
			}
		}
	}

	protected StringBuilder getBoilerplateHeader()
	{
		StringBuilder out = new StringBuilder();
		out.append("set title \"").append(m_title).append("\"\n");
		out.append("set xrange [").append(m_minX - 0.5).append(":").append(m_maxX + 0.5).append("]\n");
		out.append("set yrange [").append(m_minY - 0.5).append(":").append(m_maxY + 0.5).append("]\n");
		out.append("set view map\n");
		out.append("splot '-' matrix with image\n");
		return out;
	}

	protected StringBuilder getBoilerplateFooter()
	{
		StringBuilder out = new StringBuilder();
		out.append("e\ne\n");
		return out;
	}

	@Override
	protected Queue<Object[]> compute(Object[] inputs)
	{
		if (m_lastMap == null)
		{
			return null;
		}
		// Fill matrix with zeros
		for (int i = m_minX; i < m_maxX; i++)
		{
			for (int j = m_minY; j < m_maxY; j++)
			{
				m_values[i - m_minX][j - m_minY] = 0;
			}
		}
		// Now replace matrix with actual values when defined
		for (Object o : m_lastMap.keySet())
		{
			NamedTuple tuple = (NamedTuple) o;
			int value = m_lastMap.get(tuple);
			int x = ((EmlNumber) tuple.get("x")).numberValue().intValue();
			int y = ((EmlNumber) tuple.get("y")).numberValue().intValue();
			m_values[x - m_minX][y - m_minY] = value / m_scale;
		}
		// Create Gnuplot output file from that data
		StringBuilder out = new StringBuilder();
		out.append(getBoilerplateHeader());
		for (int i = m_minX; i < m_maxX; i++)
		{
			for (int j = m_minY; j < m_maxY; j++)
			{
				out.append(m_values[i - m_minX][j - m_minY]).append(" ");
			}
			out.append("\n");
		}
		out.append(getBoilerplateFooter());
		Object[] out_vector = new String[1];
		out_vector[0] = out.toString();
		return wrapVector(out_vector);
	}

	@Override
	public void build(Stack<Object> stack) 
	{
		stack.pop(); // )
		Processor p = (Processor) stack.pop();
		stack.pop(); // (
		stack.pop(); // OF
		stack.pop(); // HEATMAP
		stack.pop(); // GNUPLOT
		stack.pop(); // THE
		Connector.connect(p, this);
		stack.push(this);
	}

}
