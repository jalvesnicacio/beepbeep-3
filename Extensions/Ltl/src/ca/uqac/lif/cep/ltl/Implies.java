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
package ca.uqac.lif.cep.ltl;

import java.util.Stack;

import ca.uqac.lif.cep.Connector;
import ca.uqac.lif.cep.Connector.ConnectorException;
import ca.uqac.lif.cep.functions.FunctionProcessor;
import ca.uqac.lif.cep.Processor;

public class Implies extends FunctionProcessor 
{
	public Implies()
	{
		super(Troolean.IMPLIES_FUNCTION);
	}
	
	public static void build(Stack<Object> stack) throws ConnectorException 
	{
		stack.pop(); // (
		Processor right = (Processor) stack.pop();
		stack.pop(); // )
		stack.pop(); // op
		stack.pop(); // (
		Processor left = (Processor) stack.pop();
		stack.pop(); // )
		Implies op = new Implies();
		Connector.connect(left, op, 0, 0);
		Connector.connect(right, op, 0, 1);
		stack.push(op);
	}
	
	@Override
	public Implies clone()
	{
		return new Implies();
	}
	
	@Override
	public String toString()
	{
		return "IMPLIES";
	}

}
