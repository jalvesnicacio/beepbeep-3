/*
    BeepBeep, an event stream processor
    Copyright (C) 2008-2016 Sylvain Hallé

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package functions;

import ca.uqac.lif.cep.functions.ArgumentPlaceholder;
import ca.uqac.lif.cep.functions.Function;

/**
 * Basic usage of the {@link ca.uqac.lif.cep.functions.ArgumentPlaceholder}
 * function.
 * @author Sylvain Hallé
 */
public class PlaceholderUsage
{
	public static void main(String[] args)
	{
		// SNIP
		Function foo = new ArgumentPlaceholder(1);
		// A constant does not need any argument; we may pass
		// an empty array, or simply null
		Object inputs[] = new Object[]{42, "foo"};
		Object values[] = foo.evaluate(inputs);
		String s_value = (String) values[0]; // = "foo"
		// SNIP
		System.out.printf("The value of foo is %s\n", s_value);
	}
}