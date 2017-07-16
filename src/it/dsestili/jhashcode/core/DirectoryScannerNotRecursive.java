package it.dsestili.jhashcode.core;

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

import java.io.File;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;

public class DirectoryScannerNotRecursive extends AbstractDirectoryScanner
{
	private Queue<File> directories = new LinkedList<File>();

	public DirectoryScannerNotRecursive(File directory, boolean recursively) throws Throwable
	{
		if(directory.isFile())
		{
			throw new IllegalArgumentException();
		}

		this.directory = directory;
		this.recursively = recursively;
	}
	
	private void generateEvents()
	{
		synchronized(listeners)
		{
			StartScanProgressEvent event = new StartScanProgressEvent(this);
			
			for(IScanProgressListener listener : listeners)
			{
				listener.scanProgressEvent(event);
			}
		}
	}

	private void notRecursiveScan(File directory) throws Throwable
	{
		generateEvents();

		getFilesAndFolders(directory);
		
		if(recursively)
		{
			File currentDirectory = null;

			while( (currentDirectory = directories.poll()) != null )
			{
				getFilesAndFolders(currentDirectory);
			}
		}
	}

	private void getFilesAndFolders(File directory) throws Throwable
	{
		File[] sub = directory.listFiles();
		
		if(sub != null)
		{
			for(int i = 0; i < sub.length; i++)
			{
				if(interrupt)
				{
					timer.cancel();
					throw new InterruptedException();
				}
				
				File content = sub[i];

				if(content.isFile())
				{
					files.add(content);
					totalSize += content.length();
				}
				else
				{
					directories.add(content);
					directoriesFound++;
				}
			}
		}
	}

	public DirectoryInfo getFiles() throws Throwable
	{
		FilesFoundTimerTaskNR task = new FilesFoundTimerTaskNR(this);
		timer = new Timer();
		timer.schedule(task, WorkerThread.SECOND, WorkerThread.SECOND);
		
		notRecursiveScan(directory);
		
		timer.cancel();
		
		File[] f = files.toArray(new File[0]);
		DirectoryInfo di = new DirectoryInfo(f, totalSize);
		return di;
	}
}
