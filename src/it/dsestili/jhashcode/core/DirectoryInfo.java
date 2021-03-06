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

import java.io.File;

public class DirectoryInfo
{
	private File[] files;
	private long totalSize;
	private int symbolicLinksExcluded, hiddenFilesExcluded;

	public DirectoryInfo(File[] files, long totalSize, int symbolicLinksExcluded, int hiddenFilesExcluded)
	{
		this.files = files;
		this.totalSize = totalSize;
		this.symbolicLinksExcluded = symbolicLinksExcluded;
		this.hiddenFilesExcluded = hiddenFilesExcluded;
	}
	
	public File[] getFiles() 
	{
		return files;
	}
	
	public long getTotalSize() 
	{
		return totalSize;
	}

	public int getSymbolicLinksExcluded()
	{
		return symbolicLinksExcluded;
	}
	
	public int getHiddenFilesExcluded()
	{
		return hiddenFilesExcluded;
	}
}
