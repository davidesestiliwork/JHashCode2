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
import java.util.Arrays;
import java.util.Timer;

import it.dsestili.jhashcode.ui.MainWindow;

public class DirectoryScannerRecursive extends AbstractDirectoryScanner
{
	private ProgressEvent lastEvent;

	public DirectoryScannerRecursive(File directory, boolean recursively) throws Throwable
	{
		if(directory.isFile())
		{
			throw new IllegalArgumentException();
		}

		this.directory = directory;
		this.recursively = recursively;
	}

	public ProgressEvent getLastEvent()
	{
		return lastEvent;
	}

	private void generateEvents(int pCent, long current, long tot)
	{
		synchronized(listeners)
		{
			lastEvent = new ScanProgressEvent(this, pCent, current, tot, files.size(), directoriesFound, totalSize, getCharIndex());
			
			for(IScanProgressListener listener : listeners)
			{
				listener.scanProgressEvent(lastEvent);
			}
		}
	}

	private void generateEvents()
	{
		synchronized(listeners)
		{
			lastEvent = new StartScanProgressEvent(this);
			
			for(IScanProgressListener listener : listeners)
			{
				listener.scanProgressEvent(lastEvent);
			}
		}
	}

	private void scan(File directory, boolean isParent) throws Throwable
	{
		if(isParent)
		{
			generateEvents();
		}
		
		File[] sub = directory.listFiles();
		if(sub != null)
		{
			Arrays.sort(sub);
			
			if(isParent)
			{
				generateEvents(0, 0, sub.length);
			}
			
			for(int i = 0; i < sub.length; i++)
			{
				File content = sub[i];
				
				if(interrupt)
				{
					timer.cancel();
					throw new InterruptedException();
				}

				if(content.isDirectory() && !recursively)
				{
					continue;
				}
				
				if(content.isDirectory())
				{
					directoriesFound++;
					scan(content, false);
				}
				else if(MainWindow.getExcludeSymbolicLinks() && Utils.isSimbolikLink(content))
				{
					symbolicLinksFound++;
				}
				else if(MainWindow.getExcludeHiddenFiles() && Utils.isHidden(content))
				{
					hiddenFilesFound++;
				}
				else
				{
					files.add(content);
					totalSize += content.length();
				}
				
				if(isParent)
				{
					final int pCent = ((i + 1) * 100) / sub.length;
					generateEvents(pCent, (i + 1), sub.length);
				}
			}
		}
	}

	public DirectoryInfo getFiles() throws Throwable
	{
		FilesFoundTimerTask task = new FilesFoundTimerTask(this);
		timer = new Timer();
		timer.schedule(task, WorkerThread.SECOND, WorkerThread.SECOND);
		
		scan(directory, true);
		
		timer.cancel();
		
		File[] f = files.toArray(new File[0]);
		DirectoryInfo di = new DirectoryInfo(f, totalSize, symbolicLinksFound, hiddenFilesFound);
		return di;
	}
}
