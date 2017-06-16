package it.dsestili.jhashcode.core;

/*
JHashCode a simple hash code generator
Copyright (C) 2013-2016 Davide Sestili

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

import java.util.EventObject;

public class ProgressEvent extends EventObject
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected static char[] charArray = {'|', '/', '-', '\\', '|', '/', '-', '\\'};
	
	protected final int pCent;
	protected final long current;
	protected final long tot;
	protected long time;
	protected int filesFound;
	protected int directoriesFound;
	protected long totalSize;
	protected int charIndex;

	public ProgressEvent(Object source, int pCent, long current, long tot) 
	{
		super(source);

		this.time = System.currentTimeMillis();
		this.pCent = pCent;
		this.current = current;
		this.tot = tot;
	}

	public long getTime() 
	{
		return time;
	}

	public int getPCent() 
	{
		return pCent;
	}

	public long getCurrent() 
	{
		return current;
	}

	public long getTot() 
	{
		return tot;
	}

	public int getFilesFound()
	{
		return filesFound;
	}

	public int getDirectoriesFound()
	{
		return directoriesFound;
	}

	public long getTotalSize()
	{
		return totalSize;
	}
	
	public int getCharIndex()
	{
		return charIndex;
	}

	@Override
	public String toString() 
	{
		Object[] messageArguments = {
			    new Integer(pCent),
			    Utils.getFriendlySize(current),
			    Utils.getFriendlySize(tot)
			};

		return Utils.getInternationalizedString(messageArguments, "workerThread.progressEvent");
	}
	
}
