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

import it.dsestili.jhashcode.ui.MainWindow;

public class ShutdownHook extends Thread
{
	private MainWindow mainWindow;
	
	public ShutdownHook(MainWindow mainWindow)
	{
		this.mainWindow = mainWindow;
	}
	
	@Override
	public void run() 
	{
		mainWindow.closeApp();
	}
}
