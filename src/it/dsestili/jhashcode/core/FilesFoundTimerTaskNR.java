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

import java.util.List;
import java.util.TimerTask;

public class FilesFoundTimerTaskNR extends TimerTask
{
	private DirectoryScannerNotRecursive scanner;
	private List<IScanProgressListener> listeners;

	public FilesFoundTimerTaskNR(DirectoryScannerNotRecursive scanner)
	{
		this.scanner = scanner;
		this.listeners = scanner.getListeners();
	}

	public void run()
	{
		synchronized(listeners)
		{
			ScanProgressEventNR event = new ScanProgressEventNR(this, scanner.getFilesFound(), scanner.getDirectoriesFound(), scanner.getTotalSize(), scanner.getCharIndex());
	
			for(IScanProgressListener listener : listeners)
			{
				listener.scanProgressEvent(event);
			}
		}
	}
}
