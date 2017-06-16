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

import java.util.List;
import java.util.TimerTask;

public class FilesFoundTimerTask extends TimerTask
{
	private DirectoryScannerRecursive scanner;
	private List<IScanProgressListener> listeners;
	
	public FilesFoundTimerTask(DirectoryScannerRecursive scanner)
	{
		this.scanner = scanner;
		this.listeners = scanner.getListeners();
	}

	@Override
	public void run() 
	{
		synchronized(listeners)
		{
			Object source = scanner.getLastEvent().getSource();
			int pCent = scanner.getLastEvent().getPCent();
			long current = scanner.getLastEvent().getCurrent();
			long tot = scanner.getLastEvent().getTot();
			
			ScanProgressEvent event = new ScanProgressEvent(source, pCent, current, tot, scanner.getFilesFound(), scanner.getDirectoriesFound(), scanner.getTotalSize(), scanner.getCharIndex());
			
			for(IScanProgressListener listener : listeners)
			{
				listener.scanProgressEvent(event);
			}
		}
	}
}
