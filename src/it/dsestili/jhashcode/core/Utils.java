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
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import it.dsestili.jhashcode.ui.MainWindow;

public class Utils 
{
	private static final int GB = 1073741824;
	private static final int MB = 1048576;
	private static final int KB = 1024;
	
	private static final long HOUR = 3600000;
	private static final long MINUTE = 60000;
	private static final long SECOND = 1000;

	private static final String COMPUTERNAME = "COMPUTERNAME";
	
	public static String getElapsedTime(long elapsed, boolean millis)
	{
		long hours = elapsed / HOUR;
		elapsed = elapsed % HOUR;
		long minutes = elapsed / MINUTE;
		elapsed = elapsed % MINUTE;
		long seconds = elapsed / SECOND;
		elapsed = elapsed % SECOND;
		
		if(millis)
		{
			return String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds) + "." + String.format("%03d", elapsed);
		}
		else
		{
			return String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds);
		}
	}
	
	public static String getFriendlySize(final long size)
	{
		String friendlySize;
		DecimalFormat myFormatter = new DecimalFormat("###.##");
		myFormatter.setDecimalFormatSymbols(new DecimalFormatSymbols(MainWindow.getCurrentLocale()));
		
		if(size >= GB)
		{
			final double gb = (double)size / (double)GB;
			friendlySize = myFormatter.format(gb) + " GB";
		}
		else if(size >= MB)
		{
			final double mb = (double)size / (double)MB;
			friendlySize = myFormatter.format(mb) + " MB";
		}
		else if(size >= KB)
		{
			final double kb = (double)size / (double)KB;
			friendlySize = myFormatter.format(kb) + " KB";
		}
		else
		{
			friendlySize = size + " B";
		}
		
		return friendlySize;
	}
	
	private static String getOutputFileName(final File hashedFile, final String algorithm, final FolderMode folderMode)
	{
		String extension = algorithm.replace("-", "").toLowerCase();
		
		String fileName = hashedFile.getName();
		if(fileName.equals("") && (hashedFile.getPath().length() == 2 || hashedFile.getPath().length() == 3))
		{
			String unit = hashedFile.getPath().substring(0, 1);

			Object[] messageArguments = {
					unit
				};

			fileName = Utils.getInternationalizedString(messageArguments, "utils.saveHashToFile.unit");
		}

		String mode = "";
		if(folderMode != FolderMode.SINGLE_FILE_MODE)
		{
			if(folderMode == FolderMode.SUBFOLDERS_WITH_NOT_RECURSIVE_ALGORITHM)
			{
				mode = "_" + MainWindow.getResourceBundle().getString("utils.saveHashToFile.notRecursive");
			}
			else if(folderMode == FolderMode.SUBFOLDERS_WITH_RECURSIVE_ALGORITHM)
			{
				mode = "_" + MainWindow.getResourceBundle().getString("utils.saveHashToFile.recursive");
			}
			else if(folderMode == FolderMode.DO_NOT_SCAN_SUBFOLDERS)
			{
				mode = "_" + MainWindow.getResourceBundle().getString("utils.saveHashToFile.noSubfolders");
			}
		}
		
		Calendar calendar = Calendar.getInstance();
		String dateTime = "_";
		dateTime += calendar.get(Calendar.YEAR);
		dateTime += "-" + String.format("%02d", (calendar.get(Calendar.MONTH) + 1));
		dateTime += "-" + String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH));
		dateTime += "_" + String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY));
		dateTime += "-" + String.format("%02d", calendar.get(Calendar.MINUTE));
		dateTime += "-" + String.format("%02d", calendar.get(Calendar.SECOND));
		
		String outputFileName = getMachineName() + fileName + mode + dateTime + "." + extension;
		return outputFileName;
	}
	
	public static boolean saveHashToFile(final Frame frame, final Vector<Vector<String>> hashList, final File hashedFile, final String algorithm, final FolderMode folderMode) throws Throwable
	{
		boolean saved = false;
		
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		String outputFileName = getOutputFileName(hashedFile, algorithm, folderMode);
		chooser.setSelectedFile(new File(outputFileName));
		
		if(chooser.showSaveDialog(frame) != JFileChooser.APPROVE_OPTION)
		{
			return saved;
		}
		
		File file = chooser.getSelectedFile();

		if(file.exists())
		{
			Object[] messageArguments = {
					file.getName()
				};

			String output = Utils.getInternationalizedString(messageArguments, "utils.saveHashToFile.message");
			
			String label = MainWindow.getResourceBundle().getString("utils.saveHashToFile.label");
			
			int result = JOptionPane.showConfirmDialog(null, output, label, JOptionPane.YES_NO_OPTION);
			if(result == JOptionPane.NO_OPTION)
			{
				return saved;
			}
		}
		
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;

		try
		{
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			
			for(Vector<String> info : hashList)
			{
				String line = info.get(1) + " *" + info.get(0) + "\n";
				byte[] data = line.getBytes();
				bos.write(data, 0, data.length);
			}
		}
		finally
		{
			if(bos != null)
			{
				try
				{
					bos.close();
					saved = true;
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		
		return saved;
	}
	
	public static String getInternationalizedString(Object[] messageArguments, String key)
	{
		MessageFormat formatter = new MessageFormat("");
		formatter.setLocale(MainWindow.getCurrentLocale());

		formatter.applyPattern(MainWindow.getResourceBundle().getString(key));
		String output = formatter.format(messageArguments);
		
		return output;
	}

	/*
	 * This function has been written by Davide Sestili in 2005
	 */
	public static boolean isNullOrBlank(String param) 
	{
	    return param == null || param.trim().length() == 0;
	}

	private static String getMachineName()
	{
		String result = "";
		
		Map<String, String> env = System.getenv();
		if(env != null)
		{
			String computerName = env.get(COMPUTERNAME);
			if(!isNullOrBlank(computerName))
			{
				result = computerName + "_";
			}
		}
		
		return result;
	}
	
	public static String getRelativePath(File baseDir, File f)
	{
		return f.getAbsolutePath().substring(baseDir.getAbsolutePath().length(), f.getAbsolutePath().length());
	}
	
	public static String getRelativePath(String baseDir, String f)
	{
		return f.substring(baseDir.length(), f.length());
	}

	public static boolean isSimbolikLink(File file)
	{
		Path path = file.toPath();
		return Files.isSymbolicLink(path);
	}
}
