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
import java.util.Set;
import java.util.Stack;
import java.util.Vector;

import ca.uqac.lif.cep.Connector;
import ca.uqac.lif.cep.Processor;
import ca.uqac.lif.cep.eml.tuples.NamedTuple;
import ca.uqac.lif.cep.sets.EmlBag;

/**
 * Generates a Gnuplot file from a 2D {@link ca.uqac.lif.cep.sets.EmlBag}.
 */

public class GnuplotScatterplot extends GnuplotProcessor
{
	/**
	 * The name of the column containing the <i>x</i> values of the
	 * plot
	 */
	protected String m_xHeader;
	
	/**
	 * The string containing the last plot generated
	 */
	protected String m_lastPlot;
	
	public GnuplotScatterplot()
	{
		super();
		m_lastPlot = null;
		m_title = "Sample heatmap generated by BeepBeep 3";
	}

	@Override
	protected Queue<Vector<Object>> compute(Vector<Object> inputs) 
	{
		Object first_input = inputs.firstElement();
		if (first_input instanceof EmlBag)
		{
			generatePlot((EmlBag) first_input);
		}
		if (m_lastPlot != null)
		{
			Vector<Object> out = new Vector<Object>();
			out.add(m_lastPlot);
			return wrapVector(out);
		}
		return null;
	}
	
	protected void generatePlot(EmlBag bag)
	{
		StringBuilder plot_contents = new StringBuilder();
		Set<Object> bag_elements = bag.keySet();
		for (Object bag_element : bag_elements)
		{
			if (bag_element instanceof NamedTuple)
			{
				NamedTuple tuple = (NamedTuple) bag_element;
				Object x_value = tuple.get(m_xHeader);
				for (String key : tuple.keySet())
				{
					if (key.compareTo(m_xHeader) == 0)
					{
						continue;
					}
				}
			}
		}
		m_lastPlot = plot_contents.toString();
	}
	
	protected StringBuilder getBoilerplateHeader()
	{
		StringBuilder out = new StringBuilder();
		out.append("set title \"").append(m_title).append("\"\n");
		out.append("set datafile separator \",\"\n");
		out.append("plot ");
		return out;
	}


	@Override
	public void build(Stack<Object> stack) 
	{
		stack.pop(); // )
		Processor p = (Processor) stack.pop();
		stack.pop(); // (
		stack.pop(); // OF
		stack.pop(); // SCATTERPLOT
		stack.pop(); // GNUPLOT
		stack.pop(); // THE
		Connector.connect(p, this);
		stack.push(this);
	}

}