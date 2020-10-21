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

package it.dsestili.jhashcode.ui;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.TransferHandler;

public class DropTransferHandler extends TransferHandler
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) 
	{
		for(DataFlavor flavor : transferFlavors)
		{
			if(flavor.equals(DataFlavor.javaFileListFlavor))
			{
				return true;
			}
		}
		
		return false;
	}

	@Override
	public boolean importData(JComponent comp, Transferable t)
	{
		for(DataFlavor flavor : t.getTransferDataFlavors())
		{
			try
			{
				JTextField field = (JTextField)comp;
				
				if(flavor.equals(DataFlavor.stringFlavor))
				{
					String str = (String)t.getTransferData(flavor);
					
					field.setText(str.replace("file://", "").replace("%20", " "));
					
					return true;
				}
				
				if(flavor.equals(DataFlavor.javaFileListFlavor))
				{
					@SuppressWarnings("unchecked")
					List<File> fileList = (List<File>)t.getTransferData(flavor);
					
					field.setText(fileList.get(0).getAbsolutePath());
					
					return true;
				}
			}
			catch(Throwable throwable)
			{
				throwable.printStackTrace();
			}
		}
		
		return false;
	}
	
}
