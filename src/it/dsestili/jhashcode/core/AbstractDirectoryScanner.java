/*
JHashCode2 a simple hash code generator
Copyright (C) 2017 Davide Sestili

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

package it.dsestili.jhashcode.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.Vector;

public abstract class AbstractDirectoryScanner implements DirectoryScanner
{
	protected List<IScanProgressListener> listeners = new ArrayList<IScanProgressListener>();
	protected List<File> files = new Vector<File>();
	protected File directory;
	protected boolean recursively;
	protected volatile boolean interrupt = false;
	protected Timer timer;
	protected int charIndex = -1;
	protected volatile long totalSize = 0;
	protected volatile int directoriesFound = 0;
	protected volatile int symbolicLinksFound = 0;

	public List<IScanProgressListener> getListeners()
	{
		return listeners;
	}
	
	public void addIScanProgressListener(IScanProgressListener listener)
	{
		synchronized(listeners)
		{
			listeners.add(listener);
		}
	}

	public int getFilesFound()
	{
		return files.size();
	}

	public int getDirectoriesFound()
	{
		return directoriesFound;
	}

	public long getTotalSize()
	{
		return totalSize;
	}
	
	protected synchronized int getCharIndex()
	{
		charIndex = ++charIndex % 8;
		return charIndex;
	}

	public void interrupt()
	{
		interrupt = true;
	}
}
