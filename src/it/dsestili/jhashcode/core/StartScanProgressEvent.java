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

import it.dsestili.jhashcode.ui.MainWindow;

public class StartScanProgressEvent extends ProgressEvent
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public StartScanProgressEvent(Object source) 
	{
		super(source, 0, 0, 0);
	}

	@Override
	public String toString() 
	{
		return MainWindow.getResourceBundle().getString("workerThread.startScanProgressEvent");
	}

}
