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
package ca.uqac.lif.cep.ltl;

import ca.uqac.lif.cep.eml.tuples.EmlBoolean;

public class Until extends BinaryProcessor 
{
	protected boolean m_left;
	
	protected boolean m_right;
	
	public Until()
	{
		super();
		m_left = true;
		m_right = false;
	}
	
	@Override
	public void reset()
	{
		super.reset();
		m_left = true;
		m_right = false;
	}

	@Override
	protected EmlBoolean compute(EmlBoolean left, EmlBoolean right)
	{
		if (m_right)
		{
			return new EmlBoolean(true);
		}
		if (!m_left)
		{
			return new EmlBoolean(false);
		}
		m_right |= right.boolValue();
		m_left &= left.boolValue();
		if (m_right)
		{
			return new EmlBoolean(true);
		}
		if (!m_left)
		{
			return new EmlBoolean(false);
		}
		return null;
	}

}