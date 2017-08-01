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

package it.dsestili.jhashcode.test;

import java.io.File;

import it.dsestili.jhashcode.core.Core;
import it.dsestili.jhashcode.core.DirectoryInfo;
import it.dsestili.jhashcode.core.DirectoryScanner;
import it.dsestili.jhashcode.core.DirectoryScannerRecursive;
import it.dsestili.jhashcode.core.IProgressListener;
import it.dsestili.jhashcode.core.IScanProgressListener;
import it.dsestili.jhashcode.core.ProgressEvent;
import it.dsestili.jhashcode.ui.MainWindow;

public class TestDirectoryScanner implements IProgressListener, IScanProgressListener
{
	private void test(String directoryParam, String algorithm)
	{
		try
		{
			MainWindow.setItalianLocale();
			
			checkIsEmpty(directoryParam);
			
			System.out.println("Directory: " + directoryParam);
			File directory = new File(directoryParam);

			if(!directory.exists())
			{
				throw new Exception("Directory does not exist");
			}
			
			DirectoryScanner scanner = new DirectoryScannerRecursive(directory, true);
			scanner.addIScanProgressListener(this);
			
			DirectoryInfo di = scanner.getFiles();
			File[] files = di.getFiles();
			long totalSize = di.getTotalSize();
			
			System.out.println("Scanning completed, " + files.length + " files found, " + totalSize + " bytes (total size)");
			
			for(File f : files)
			{
				Core core = new Core(f, algorithm);
				core.addIProgressListener(this);
				String hash = core.generateHash();
				System.out.println(hash + " *" + f.getAbsolutePath());
			}
		}
		catch(InterruptedException e)
		{
			System.out.println("Operation canceled!");
		}
		catch(Throwable t)
		{
			t.printStackTrace();
		}
	}
	
	private void checkIsEmpty(String param) throws Exception
	{
		if(param == null || param.isEmpty())
		{
			throw new Exception("Blank parameter!");
		}
	}
	
	public static void main(String[] args) 
	{
		if(args.length > 1)
		{
			new TestDirectoryScanner().test(args[0], args[1]);
		}
		else
		{
			System.out.println("Usage: param1: folder, param2: algorithm");
		}
	}

	public void scanProgressEvent(ProgressEvent event) 
	{
		System.out.println(event);
	}

	public void progressEvent(ProgressEvent event) 
	{
		System.out.println(event);
	}
}
