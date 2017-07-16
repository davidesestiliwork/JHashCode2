package it.dsestili.jhashcode.ui;

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

import javax.swing.JPanel;

import java.awt.GridBagLayout;

import javax.swing.JLabel;

import java.awt.GridBagConstraints;
import java.awt.Font;
import java.awt.Insets;
import java.util.ResourceBundle;

import javax.swing.JProgressBar;

public class StatusPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel lblStatus, lblTotal;
	private JProgressBar progressBar, progressBar2;
	private JLabel lblLabel;
	private JLabel lblLabel2;

	public void setStatus(String status)
	{
		lblStatus.setText(status);
	}

	public void setValue(int pCent)
	{
		progressBar.setValue(pCent);
	}
	
	public void setTotalStatus(String status)
	{
		lblTotal.setText(status);
	}

	public void setTotalValue(int pCent)
	{
		progressBar2.setValue(pCent);
	}

	/**
	 * Create the panel.
	 */
	public StatusPanel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		lblLabel = new JLabel("Status:");
		lblLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		GridBagConstraints gbc_lblLabel = new GridBagConstraints();
		gbc_lblLabel.insets = new Insets(10, 10, 10, 10);
		gbc_lblLabel.gridx = 0;
		gbc_lblLabel.gridy = 0;
		add(lblLabel, gbc_lblLabel);
		
		lblStatus = new JLabel("");
		GridBagConstraints gbc_lblStatus = new GridBagConstraints();
		gbc_lblStatus.anchor = GridBagConstraints.WEST;
		gbc_lblStatus.gridwidth = 4;
		gbc_lblStatus.insets = new Insets(10, 10, 10, 10);
		gbc_lblStatus.gridx = 1;
		gbc_lblStatus.gridy = 0;
		add(lblStatus, gbc_lblStatus);
		
		progressBar = new JProgressBar();
		GridBagConstraints gbc_progressBar = new GridBagConstraints();
		gbc_progressBar.fill = GridBagConstraints.HORIZONTAL;
		gbc_progressBar.gridwidth = 5;
		gbc_progressBar.insets = new Insets(10, 10, 10, 10);
		gbc_progressBar.gridx = 0;
		gbc_progressBar.gridy = 1;
		add(progressBar, gbc_progressBar);

		lblLabel2 = new JLabel("Total:");
		lblLabel2.setFont(new Font("Tahoma", Font.BOLD, 11));
		GridBagConstraints gbc_lblLabel2 = new GridBagConstraints();
		gbc_lblLabel2.insets = new Insets(10, 10, 10, 10);
		gbc_lblLabel2.gridx = 0;
		gbc_lblLabel2.gridy = 2;
		add(lblLabel2, gbc_lblLabel2);
		
		lblTotal = new JLabel("");
		GridBagConstraints gbc_lblStatus2 = new GridBagConstraints();
		gbc_lblStatus2.anchor = GridBagConstraints.WEST;
		gbc_lblStatus2.gridwidth = 4;
		gbc_lblStatus2.insets = new Insets(10, 10, 10, 10);
		gbc_lblStatus2.gridx = 1;
		gbc_lblStatus2.gridy = 2;
		add(lblTotal, gbc_lblStatus2);
		
		progressBar2 = new JProgressBar();
		GridBagConstraints gbc_progressBar2 = new GridBagConstraints();
		gbc_progressBar2.fill = GridBagConstraints.HORIZONTAL;
		gbc_progressBar2.gridwidth = 5;
		gbc_progressBar2.insets = new Insets(10, 10, 10, 10);
		gbc_progressBar2.gridx = 0;
		gbc_progressBar2.gridy = 3;
		add(progressBar2, gbc_progressBar2);

	}

	public void internationalize()
	{
		ResourceBundle resourceBundle = MainWindow.getResourceBundle();
		
		lblLabel.setText(resourceBundle.getString("label.status"));
		lblLabel2.setText(resourceBundle.getString("label.total"));
	}
	
}
