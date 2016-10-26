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
package ca.uqac.lif.cep.functions;

import java.util.Stack;

import ca.uqac.lif.cep.Connector.ConnectorException;

/**
 * Instance of the AND function. This class exists only so that we can
 * refer to it in an expression of the form:
 * <pre>
 * COMBINE (something) WITH CONJUNCTION
 * </pre>
 * 
 * @author Sylvain Hallé
 */
class AndInstance extends And
{
	public static final transient AndInstance instance = new AndInstance();
	
	AndInstance()
	{
		super();
	}
	
	public static void build(Stack<Object> stack) throws ConnectorException
	{
		stack.pop(); // the name
		stack.push(instance);
	}
}
