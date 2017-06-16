package it.dsestili.jhashcode.ui;

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

import javax.swing.JButton;

public class UIFactory 
{

	public static class InputPanelFactory
	{
		private static InputPanel inputPanel = new InputPanel();
		
		public static InputPanel getInstance()
		{
			return inputPanel;
		}
	}
	
	public static class StatusPanelFactory
	{
		private static StatusPanel statusPanel = new StatusPanel();
		
		public static StatusPanel getInstance()
		{
			return statusPanel;
		}
	}
	
	public static class MainButtonFactory
	{
		private static JButton btnStart = new JButton("Generate Hash");
		
		public static JButton getInstance()
		{
			return btnStart;
		}
	}
	
	public static class MainWindowFactory
	{
		private static MainWindow frame = new MainWindow();
		
		public static MainWindow getInstance()
		{
			return frame;
		}
	}
	
}
