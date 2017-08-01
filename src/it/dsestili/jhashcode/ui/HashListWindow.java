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

import it.dsestili.jhashcode.core.FolderMode;
import it.dsestili.jhashcode.core.Utils;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JTable;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

public class HashListWindow extends JDialog 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JScrollPane scrollPane;

	private String errorOccurred;
	private String title;
	private String columnFile;
	private String columnHash;
	private String okButtonText;
	private String saveToFileButtonText;
	private String fileNotSaved;
	private String fileNotSavedTitle;
	
	private boolean saved = false;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) 
	{
		try 
		{
			HashListWindow dialog = new HashListWindow(null, null, null, null, null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} 
		catch(Exception e) 
		{
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public HashListWindow(final Frame frame, final Vector<Vector<String>> hashList, final File hashedFile, final String algorithm, final FolderMode folderMode)
	{
		super(frame, true);

		errorOccurred = MainWindow.getResourceBundle().getString("workerThread.errorOccurred");
		title = MainWindow.getResourceBundle().getString("hashListWindow.title");
		columnFile = MainWindow.getResourceBundle().getString("hashListWindow.column.file");
		columnHash = MainWindow.getResourceBundle().getString("hashListWindow.column.hash");
		okButtonText = MainWindow.getResourceBundle().getString("hashListWindow.button.ok");
		saveToFileButtonText = MainWindow.getResourceBundle().getString("hashListWindow.button.btnSaveToFile");
		fileNotSaved = MainWindow.getResourceBundle().getString("hashListWindow.notSaved");
		fileNotSavedTitle = MainWindow.getResourceBundle().getString("hashListWindow.notSaved.title");

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		setTitle(title);
		setBounds(100, 100, 396, 470);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));

		Vector<String> columnNames = new Vector<String>();
		columnNames.add(columnFile);
		columnNames.add(columnHash);
		
		JTable table = new JTable(hashList, columnNames);
		table.setEnabled(false);
		
		scrollPane = new JScrollPane(table);

		contentPanel.add(scrollPane, BorderLayout.CENTER);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		JButton okButton = new JButton(okButtonText);
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				promptBeforeClosing();
			}
		});

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) 
			{
				promptBeforeClosing();
			}
		});

		JButton btnSaveToFile = new JButton(saveToFileButtonText);
		btnSaveToFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				try 
				{
					if(Utils.saveHashToFile(frame, hashList, hashedFile, algorithm, folderMode))
					{
						saved = true;
					}
				}
				catch(Throwable e) 
				{
					JOptionPane.showMessageDialog(null, errorOccurred, errorOccurred, JOptionPane.ERROR_MESSAGE);
					
					e.printStackTrace();
				}
			}
		});
		buttonPane.add(btnSaveToFile);
		okButton.setActionCommand(okButtonText);
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
		
		setSize(1000, 700);
		MainWindow.centerScreen(this);
		setVisible(true);		
	}

	private void promptBeforeClosing()
	{
		if(!saved)
		{
			int result = JOptionPane.showConfirmDialog(null, fileNotSaved, fileNotSavedTitle, JOptionPane.YES_NO_OPTION);
			if(result == JOptionPane.NO_OPTION)
			{
				return;
			}
		}
		
		dispose();
	}
}
