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

import java.awt.Frame;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;
import javax.swing.SwingUtilities;

import it.dsestili.jhashcode.ui.ChooseModeDialog;
import it.dsestili.jhashcode.ui.UIFactory;
import it.dsestili.jhashcode.ui.UIFactory.MainWindowFactory;
import it.dsestili.jhashcode.ui.HashListWindow;
import it.dsestili.jhashcode.ui.MainWindow;

public class DirectoryWorkerThread extends WorkerThread implements IScanProgressListener
{
	private volatile File currentFile;
	private volatile int currentIndex, totFiles;
	private final Frame frame;
	private DirectoryScanner scanner = null;
	private long currentProgressSize = 0L, totalSize;
	private int lastPCent = -1;
	private long startTime;
	private boolean recursively;
	private String emptyFolder;
	private String warning;
	private String avg;
	private volatile String avgProcessingRate = "";
	private ChooseModeDialog dialog;
	
	public DirectoryWorkerThread(File file)	
	{
		super(file);
		
		this.frame = UIFactory.MainWindowFactory.getInstance();
		
		emptyFolder = MainWindow.getResourceBundle().getString("workerThread.emptyFolder");
		warning = MainWindow.getResourceBundle().getString("workerThread.warning");
		avg = MainWindow.getResourceBundle().getString("workerThread.average");
	}

	@Override
	public void run()
	{
		try
		{
			manageUI(false);
			
			showHash("");
			
			chooseMode();
			scanner.addIScanProgressListener(this);
			
			startTimer();

			DirectoryInfo di = scanner.getFiles();
			File[] files = di.getFiles();
			totalSize = di.getTotalSize();

			if(di.getSymbolicLinksExcluded() > 0 || di.getHiddenFilesExcluded() > 0)
			{
				Object[] arguments = { di.getSymbolicLinksExcluded(), di.getHiddenFilesExcluded() };
				String symbolicLinkExcludedMessage = Utils.getInternationalizedString(arguments, "workedThread.excluded.message");
				String symbolicLinkExcludedTitle = MainWindow.getResourceBundle().getString("workerThread.excluded.message.title");
				showMessageAsync(symbolicLinkExcludedMessage, symbolicLinkExcludedTitle);
			}
			
			if(files.length == 0)
			{
				throw new EmptyFolderException();
			}
			
			Vector<Vector<String>> hashList = new Vector<Vector<String>>();
			
			totFiles = files.length;
			
			startTime = System.currentTimeMillis();

			String folderToExclude = MainWindowFactory.getInstance().getFolderToExclude();

			for(int i = 0; i < files.length; i++)
			{
				currentIndex = i;
				currentFile = files[i];

				if(folderToExclude != null && (!folderToExclude.trim().equals("")) && currentFile.getAbsolutePath().startsWith(folderToExclude))
				{
					continue;
				}
				
				core = new Core(currentFile, algorithm);
				core.addIProgressListener(this);

				Vector<String> info = new Vector<String>();
				info.add(recursively ? Utils.getRelativePath(file, currentFile) : currentFile.getName());

				try
				{
					String hash = core.generateHash();
					info.add(hash);
				}
				catch(FileNotFoundException e)
				{
					manageException(e, info);
				}
				catch(IOException e)
				{
					manageException(e, info);
				}
				
				hashList.add(info);
				currentProgressSize += currentFile.length();
			}

			showHash(hashList);
		}
		catch(EmptyFolderException e)
		{
			showStatus(emptyFolder);
			showMessageAsync(emptyFolder, emptyFolder);
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

	private void chooseMode() throws Throwable
	{
		dialog = new ChooseModeDialog();
		MainWindow.centerScreen(dialog);
		dialog.setVisible(true);
		
		if(dialog.getFolderMode() == FolderMode.SUBFOLDERS_WITH_NOT_RECURSIVE_ALGORITHM)
		{
			scanner = new DirectoryScannerNotRecursive(file, true);
			recursively = true;
		}
		else if(dialog.getFolderMode() == FolderMode.SUBFOLDERS_WITH_RECURSIVE_ALGORITHM)
		{
			scanner = new DirectoryScannerRecursive(file, true);
			recursively = true;
		}
		else if(dialog.getFolderMode() == FolderMode.DO_NOT_SCAN_SUBFOLDERS)
		{
			scanner = new DirectoryScannerRecursive(file, false);
			recursively = false;
		}
		else if(dialog.getFolderMode() == FolderMode.CANCEL_OPERATION)
		{
			throw new InterruptedException();
		}
	}
	
	private void manageException(Exception e, Vector<String> info)
	{
		e.printStackTrace();
		info.add(warning + " " + e.toString());
		showStatus(warning + " " + e.toString());
	}
	
	@Override
	public void interruptOperation()
	{
		if(scanner != null)
		{
			scanner.interrupt();
		}
		
		super.interruptOperation();
	}
	
	protected void showHash(final Vector<Vector<String>> hashList)
	{
		SwingUtilities.invokeLater(new Runnable() 
		{
			public void run() 
			{
				new HashListWindow(frame, hashList, file, algorithm, dialog.getFolderMode());
			}
		});
	}

	@Override
	public void progressEvent(final ProgressEvent event) 
	{
		final String remaining = getRemainingTime(event);
		final String processingRate = getProcessingRate(event);
		
		final long current = currentProgressSize + event.getCurrent();
		
		final long totalPCent;
		
		if(totalSize == 0)
		{
			totalPCent = 100L;
		}
		else
		{
			totalPCent = (current * 100L) / totalSize;
		}
		
		SwingUtilities.invokeLater(new Runnable() 
		{
			public void run() 
			{
				Object[] messageArguments = {
					    new Integer(currentIndex + 1),
					    new Integer(totFiles),
					    currentFile.getName(),
					    event.toString() + remaining + processingRate
					};

				String output = Utils.getInternationalizedString(messageArguments, "workerThread.status.batch");

				statusPanel.setStatus(output);
				statusPanel.setValue(event.getPCent());

				if(totalPCent > lastPCent)
				{
					String totalRemainig = " " + remainingTime + " ?";
					
					if(totalPCent > 0 && current > 0)
					{
						long elapsed = event.getTime() - startTime;
						long totalTime = (elapsed * totalSize) / current;
						long totalRemainingTime = totalTime - elapsed;
						totalRemainig = " " + remainingTime + " " + Utils.getElapsedTime(totalRemainingTime, false);
						
						avgProcessingRate = getAverageProcessingRate(elapsed, current);
					}

					Object[] arguments = {
						    new Integer((int) totalPCent),
						    Utils.getFriendlySize(current),
						    Utils.getFriendlySize(totalSize)
						};
					
					String totalStatus = Utils.getInternationalizedString(arguments, "workerThread.status.total.batch") + totalRemainig + avgProcessingRate;
					statusPanel.setTotalStatus(totalStatus);
					statusPanel.setTotalValue((int) totalPCent);

					lastPCent = (int) totalPCent;
				}
			}
		});
	}

	private String getAverageProcessingRate(final long elapsed, final long current)
	{
		double seconds = (double) elapsed / (double) 1000;
		double avgProcessingRate = (double) current / seconds;
		return " [" + avg + " " + Utils.getFriendlySize((long) avgProcessingRate) + "/s]";
	}
	
	public void scanProgressEvent(final ProgressEvent event) 
	{
		SwingUtilities.invokeLater(new Runnable() 
		{
			public void run() 
			{
				statusPanel.setStatus(event.toString());
				statusPanel.setValue(event.getPCent());

				statusPanel.setTotalStatus("");
				statusPanel.setTotalValue(0);
			}
		});
	}

	private static class EmptyFolderException extends Exception
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public EmptyFolderException()
		{
			super();
		}
	}
}
