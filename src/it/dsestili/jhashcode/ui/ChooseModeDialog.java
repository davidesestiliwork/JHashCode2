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

import it.dsestili.jhashcode.core.FolderMode;

import java.awt.EventQueue;

import javax.swing.JDialog;
import javax.swing.JButton;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;

public class ChooseModeDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private FolderMode folderMode = FolderMode.CANCEL_OPERATION;
	
	private JButton btnNotRecursive;
	private JButton btnRecursive;
	private JButton btnNoSubfolders;
	private String title;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChooseModeDialog dialog = new ChooseModeDialog();
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public FolderMode getFolderMode()
	{
		return folderMode;
	}
	
	/**
	 * Create the dialog.
	 */
	public ChooseModeDialog() {
		setModalityType(ModalityType.APPLICATION_MODAL);

		title = MainWindow.getResourceBundle().getString("chooseModeDialog.title");
		setTitle(title);

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 519, 202);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {0};
		gridBagLayout.rowHeights = new int[] {0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0};
		getContentPane().setLayout(gridBagLayout);
		
		btnNotRecursive = new JButton("Scan subfolders using not-recursive algorithm");
		btnNotRecursive.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				folderMode = FolderMode.SUBFOLDERS_WITH_NOT_RECURSIVE_ALGORITHM;
				dispose();
			}
		});
		GridBagConstraints gbc_btnNotRecursive = new GridBagConstraints();
		gbc_btnNotRecursive.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNotRecursive.insets = new Insets(20, 20, 20, 20);
		gbc_btnNotRecursive.gridx = 0;
		gbc_btnNotRecursive.gridy = 0;
		getContentPane().add(btnNotRecursive, gbc_btnNotRecursive);
		
		btnRecursive = new JButton("Scan subfolders using recursive algorithm");
		btnRecursive.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				folderMode = FolderMode.SUBFOLDERS_WITH_RECURSIVE_ALGORITHM;
				dispose();
			}
		});
		GridBagConstraints gbc_btnRecursive = new GridBagConstraints();
		gbc_btnRecursive.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnRecursive.insets = new Insets(0, 20, 20, 20);
		gbc_btnRecursive.gridx = 0;
		gbc_btnRecursive.gridy = 1;
		getContentPane().add(btnRecursive, gbc_btnRecursive);
		
		btnNoSubfolders = new JButton("Do not scan subfolders");
		btnNoSubfolders.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				folderMode = FolderMode.DO_NOT_SCAN_SUBFOLDERS;
				dispose();
			}
		});
		GridBagConstraints gbc_btnNoSubfolders = new GridBagConstraints();
		gbc_btnNoSubfolders.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNoSubfolders.insets = new Insets(0, 20, 20, 20);
		gbc_btnNoSubfolders.gridx = 0;
		gbc_btnNoSubfolders.gridy = 2;
		getContentPane().add(btnNoSubfolders, gbc_btnNoSubfolders);
		
		internationalize();
	}

	private void internationalize()
	{
		ResourceBundle resourceBundle = MainWindow.getResourceBundle();
		
		btnNotRecursive.setText(resourceBundle.getString("button.btnNotRecursive"));
		btnRecursive.setText(resourceBundle.getString("button.btnRecursive"));
		btnNoSubfolders.setText(resourceBundle.getString("button.btnNoSubfolders"));
	}
}
