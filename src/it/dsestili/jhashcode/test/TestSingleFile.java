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
import java.text.DecimalFormat;
import it.dsestili.jhashcode.core.Core;
import it.dsestili.jhashcode.core.IProgressListener;
import it.dsestili.jhashcode.core.ProgressEvent;
import it.dsestili.jhashcode.ui.MainWindow;

public class TestSingleFile implements IProgressListener 
{

	private void test(String fileName, String algorithm)
	{
		try
		{
			MainWindow.setItalianLocale();
			
			if(fileName == null || fileName.isEmpty())
			{
				System.out.println("Blank file name!");
				return;
			}
			
			System.out.println("File name: " + fileName);
			File file = new File(fileName);
			
			if(!file.exists())
			{
				System.out.println("File does not exist!");
				return;
			}

            if(!file.isFile())
			{
				System.out.println("Is not a file!");
				return;
			}
			
			Core core = new Core(file, algorithm);
			core.addIProgressListener(this);

			long start = System.currentTimeMillis();
			String hash = core.generateHash();
			long elapsed = System.currentTimeMillis() - start;
			
			System.out.println("Hash: " + hash.toUpperCase());
			
			DecimalFormat myFormatter = new DecimalFormat("###.##");
			double seconds = (double)elapsed / 1000.0;
			System.out.println("Elapsed time: " + myFormatter.format(seconds) + " seconds");
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
	
	public static void main(String[] args) 
	{
        if(args.length > 1)
        {
                new TestSingleFile().test(args[0], args[1]);
        }
        else
        {
                System.out.println("Usage: param1: fileName, param2: algorithm");
        }
	}

	public void progressEvent(ProgressEvent event) 
	{
		System.out.println(event);
	}
}
