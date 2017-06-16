package it.dsestili.jhashcode.core;

import java.util.TimerTask;

import it.dsestili.jhashcode.ui.UIFactory;
import it.dsestili.jhashcode.ui.MainWindow;

public class ElapsedTimerTask extends TimerTask 
{
	private long time = -1000;
	
	@Override
	public synchronized void run() 
	{
		time += WorkerThread.SECOND;

		final String elapsedTime = " - " + WorkerThread.elapsedTime + " " + Utils.getElapsedTime(time, false);
		
		final MainWindow frame = UIFactory.MainWindowFactory.getInstance();

		WorkerThread.setTitleAndTime(frame, elapsedTime);
	}

}
