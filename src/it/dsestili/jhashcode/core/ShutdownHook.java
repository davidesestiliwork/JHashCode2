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
