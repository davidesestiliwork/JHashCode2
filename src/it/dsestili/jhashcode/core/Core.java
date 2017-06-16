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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

public class Core 
{
	private static final int BUFFER_SIZE = 128 * 1024;
	
	private File file;
	private MessageDigest md;
	private List<IProgressListener> listeners = new ArrayList<IProgressListener>();
	private volatile boolean interrupt = false;
	
	public Core(File file, String algorithm) throws NoSuchAlgorithmException
	{
		this.file = file;
		md = MessageDigest.getInstance(algorithm);
	}

	public String generateHash() throws Throwable
	{
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		
		try
		{
			fis = new FileInputStream(file);
			bis = new BufferedInputStream(fis);

			final long fileSize = file.length();
			
			generateEvents(0, 0, fileSize);
			
			int pCent, lastPcent = 0;
			byte[] buf = new byte[BUFFER_SIZE];
			int len;
			long position = 0;
			
			while((len = bis.read(buf, 0, buf.length)) != -1 )
			{
				if(interrupt)
				{
					throw new InterruptedException();
				}

				md.update(buf, 0, len);
				
				position += len;
				
				pCent = (int)((position * 100) / fileSize);
				if(pCent > lastPcent)
				{
					lastPcent = pCent;
					generateEvents(pCent, position, fileSize);
				}
			}
			
			if(fileSize == 0)
			{
				generateEvents(100, fileSize, fileSize);
			}
		}
		finally
		{
			close(bis);
		}
		
		byte[] data = md.digest();
		String hash = byteArray2Hex(data);
		
		return hash;
	}

	public void interrupt()
	{
		interrupt = true;
	}
	
	public void addIProgressListener(IProgressListener listener)
	{
		listeners.add(listener);
	}
	
	private void generateEvents(int pCent, long current, long tot)
	{
		final ProgressEvent event = new ProgressEvent(this, pCent, current, tot);
		
		for(IProgressListener listener : listeners)
		{
			listener.progressEvent(event);
		}
	}
	
	private static String byteArray2Hex(final byte[] hash) 
	{
	    Formatter formatter = new Formatter();
	    
	    for(byte b : hash) 
	    {
	        formatter.format("%02x", b);
	    }
	    
	    String result = formatter.toString();
	    formatter.close();
	    return result;
	}

	private static void close(InputStream is)
	{
		if(is != null)
		{
			try 
			{
				is.close();
			} 
			catch(IOException e) 
			{
				e.printStackTrace();
			}
		}
	}
}
