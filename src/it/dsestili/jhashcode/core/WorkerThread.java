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

import java.awt.Component;
import java.io.File;
import java.util.Timer;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import it.dsestili.jhashcode.ui.UIFactory;
import it.dsestili.jhashcode.ui.InputPanel;
import it.dsestili.jhashcode.ui.MainWindow;
import it.dsestili.jhashcode.ui.StatusPanel;

public class WorkerThread extends Thread implements IProgressListener
{
	protected Core core;
	protected final StatusPanel statusPanel;
	protected final InputPanel inputPanel;
	protected final JButton btnStart;
	protected final File file;
	protected final String algorithm;
	protected ProgressEvent firstEvent;
	protected Timer timer;
	protected long start;
	protected static String operationCanceled;
	protected static String errorOccurred;
	protected static String generateHash;
	protected static String cancel;
	protected static String elapsedTime;
	protected static String remainingTime;
	protected static String saveHashToFile;
	protected static String saveHash;

	static final int SECOND = 1000;

	public WorkerThread(File file)
	{
		this.btnStart = UIFactory.MainButtonFactory.getInstance();
		this.inputPanel = UIFactory.InputPanelFactory.getInstance();
		this.statusPanel = UIFactory.StatusPanelFactory.getInstance();
		this.file = file;
		this.algorithm = inputPanel.getAlgorithm();
		
		operationCanceled = MainWindow.getResourceBundle().getString("workerThread.operationCanceled");
		errorOccurred = MainWindow.getResourceBundle().getString("workerThread.errorOccurred");
		generateHash = MainWindow.getResourceBundle().getString("button.start");
		cancel = MainWindow.getResourceBundle().getString("button.start.cancel");
		elapsedTime = MainWindow.getResourceBundle().getString("workerThread.elapsedTime");
		remainingTime = MainWindow.getResourceBundle().getString("workerThread.remainingTime");
		saveHashToFile = MainWindow.getResourceBundle().getString("workerThread.saveHashToFile");
		saveHash = MainWindow.getResourceBundle().getString("workerThread.saveHash");
	}

	public void interruptOperation()
	{
		if(core != null)
		{
			core.interrupt();
		}
	}

	@Override
	public void run() 
	{
		try
		{
			manageUI(false);
			startTimer();
			
			showHash("");
			
			core = new Core(file, algorithm);
			core.addIProgressListener(this);

			String hash = core.generateHash();

			showHash(hash);
			saveHashAsync(hash);
		}
		catch(InterruptedException e)
		{
			showStatus(operationCanceled);
		}
		catch(Throwable t)
		{
			showStatus(errorOccurred);
			
			t.printStackTrace();
		}
		finally
		{
			stopTimer();
			manageUI(true);
		}
	}

	protected void saveHashAsync(final String hash)
	{
		SaveHashAsync async = new SaveHashAsync(file, hash, algorithm);
		async.start();
	}
	
	protected void showHash(final String hash)
	{
		SwingUtilities.invokeLater(new Runnable() 
		{
			public void run() 
			{
				inputPanel.setGeneratedHash(hash);
			}
		});
	}
	
	protected void startTimer()
	{
		start = System.currentTimeMillis();
		
		timer = new Timer();
		timer.schedule(new ElapsedTimerTask(), 0, SECOND);
	}

	protected void stopTimer()
	{
		if(timer != null)
		{
			timer.cancel();
	
			long elapsed = System.currentTimeMillis() - start;
			String elapsedTimeOutput = " - " + elapsedTime + " " + Utils.getElapsedTime(elapsed, true);
			MainWindow frame = UIFactory.MainWindowFactory.getInstance();
			setTitleAndTime(frame, elapsedTimeOutput);
		}
	}

	static void setTitleAndTime(final MainWindow frame, final String elapsedTime)
	{
		SwingUtilities.invokeLater(new Runnable() 
		{
			public void run() 
			{
				frame.setTitleAndTime(elapsedTime);
			}
		});
	}
	
	protected void manageUI(final boolean enabled)
	{
		SwingUtilities.invokeLater(new Runnable() 
		{
			public void run() 
			{
				inputPanel.setEnabled(enabled);
				
				for(Component component : inputPanel.getComponents())
				{
					component.setEnabled(enabled);
				}

				btnStart.setText(enabled ? generateHash : cancel);
				
				MainWindow.getLanguageMenu().setEnabled(enabled);
			}
		});
	}

	protected void showStatus(final String status)
	{
		SwingUtilities.invokeLater(new Runnable() 
		{
			public void run() 
			{
				statusPanel.setStatus(status);
			}
		});
	}
	
	public void progressEvent(final ProgressEvent event) 
	{
		final String remaining = getRemainingTime(event);
		final String processingRate = getProcessingRate(event);
		
		SwingUtilities.invokeLater(new Runnable() 
		{
			public void run() 
			{
				statusPanel.setStatus(event.toString() + remaining + processingRate);
				statusPanel.setValue(event.getPCent());
				statusPanel.setTotalStatus("---");
				statusPanel.setTotalValue(event.getPCent());
			}
		});
	}
	
	protected String getRemainingTime(final ProgressEvent event)
	{
		String result = null;
		
		if(event.getPCent() == 0)
		{
			firstEvent = event;
			result = " " + remainingTime + " ?";
		}
		else if(event.getPCent() == 100)
		{
			result = "";
		}
		else
		{
			long elapsed = getElapsedTime(event);
			long tot = elapsed * event.getTot() / event.getCurrent();
			long remaining = tot - elapsed;
			result = " " + remainingTime + " " + Utils.getElapsedTime(remaining, false);
		}
		
		return result;
	}

	private long getElapsedTime(final ProgressEvent event)
	{
		long elapsed = event.getTime() - firstEvent.getTime();
		return elapsed;
	}
	
	protected String getProcessingRate(final ProgressEvent event)
	{
		String result = "";
		
		if(event.getPCent() > 0)
		{
			long elapsed = getElapsedTime(event);
			if(elapsed == 0)
			{
				return result;
			}
			
			double seconds = (double) elapsed / (double) 1000;
			double processingRate = (double) event.getCurrent() / seconds;
			result = " [" + Utils.getFriendlySize((long) processingRate) + "/s]";
		}
		
		return result;
	}

	protected void showMessageAsync(final String message, final String title)
	{
		ShowMessageAsync sma = new ShowMessageAsync(message, title);
		sma.start();
	}
	
	private static class ShowMessageAsync extends Thread
	{
		private final String message;
		private final String title;
		
		public ShowMessageAsync(final String message, final String title)
		{
			this.message = message;
			this.title = title;
		}

		@Override
		public void run() 
		{
			JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	private static class SaveHashAsync extends Thread
	{
		private final File file;
		private final String hash;
		private final String algorithm;
		
		public SaveHashAsync(final File file, final String hash, final String algorithm)
		{
			this.file = file;
			this.hash = hash;
			this.algorithm = algorithm;
		}
		
		@Override
		public void run() 
		{
			saveHash();
		}

		private void saveHash()
		{
			try 
			{
				int result = JOptionPane.showConfirmDialog(null, saveHashToFile, saveHash, JOptionPane.YES_NO_OPTION);
				if(result == JOptionPane.YES_OPTION)
				{
					Vector<Vector<String>> hashList = new Vector<Vector<String>>();
					
					Vector<String> info = new Vector<String>();
					info.add(file.getName());
					info.add(hash);
					
					hashList.add(info);
				
					Utils.saveHashToFile(null, hashList, file, algorithm, FolderMode.SINGLE_FILE_MODE);
				}
			}
			catch(Throwable e) 
			{
				JOptionPane.showMessageDialog(null, errorOccurred, errorOccurred, JOptionPane.ERROR_MESSAGE);
				
				e.printStackTrace();
			}
		}
	}
}
