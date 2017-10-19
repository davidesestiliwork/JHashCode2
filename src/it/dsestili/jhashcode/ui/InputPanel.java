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

import javax.swing.ButtonGroup;
import javax.swing.JPanel;

import java.awt.GridBagLayout;

import javax.swing.JRadioButton;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;

public class InputPanel extends JPanel 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel lblAlgorithm;
	private JLabel lblFile;
	private JLabel lblGenerated;
	private JLabel lblCompare;
	private JButton btnCompare;
	private JTextField txtFile;
	private JTextField txtHash;
	private JTextField txtCompare;
	private JRadioButton btnAlgorithmMD2;
	private JRadioButton btnAlgorithmMD5;
	private JRadioButton btnAlgorithmSHA1;
	private JRadioButton btnAlgorithmSHA256;
	private JRadioButton btnAlgorithmSHA384;
	private JRadioButton btnAlgorithmSHA512;

	private String generateHashBeforeMessage = "You have to generate an hash before!";
	private String generateAnHash = "Generate an hash";
	private String pasteAnHashBeforeMessage = "You have to paste an hash before!";
	private String pasteAnHash = "Paste an hash";
	private String hashCodesMatchMessage = "Hash codes match!";
	private String identical = "Identical";
	private String hashCodesDoesNotMatchMessage = "Hash codes does NOT match!";
	private String doesNotMatch = "Does NOT match";

	public JTextField getTxtFile()
	{
		return txtFile;
	}
	
	public String getFileName()
	{
		return txtFile.getText().trim();
	}

	public void setGeneratedHash(String hash)
	{
		txtHash.setText(hash.toUpperCase());
	}

	public String getGeneratedHash()
	{
		return txtHash.getText();
	}
	
	public String getAlgorithm()
	{
		String algorithm = null;
		
		if(btnAlgorithmMD2.isSelected())
		{
			algorithm = "MD2";
		}
		else if(btnAlgorithmMD5.isSelected())
		{
			algorithm = "MD5";
		}
		else if(btnAlgorithmSHA1.isSelected())
		{
			algorithm = "SHA-1";
		}
		else if(btnAlgorithmSHA256.isSelected())
		{
			algorithm = "SHA-256";
		}
		else if(btnAlgorithmSHA384.isSelected())
		{
			algorithm = "SHA-384";
		}
		else if(btnAlgorithmSHA512.isSelected())
		{
			algorithm = "SHA-512";
		}
		
		return algorithm;
	}
	
	/**
	 * Create the panel.
	 */
	public InputPanel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);

		ButtonGroup group = new ButtonGroup();
		
		lblAlgorithm = new JLabel("Choose algorithm:");
		GridBagConstraints gbc_lblAlgorithm = new GridBagConstraints();
		gbc_lblAlgorithm.anchor = GridBagConstraints.EAST;
		gbc_lblAlgorithm.insets = new Insets(10, 10, 10, 10);
		gbc_lblAlgorithm.gridx = 0;
		gbc_lblAlgorithm.gridy = 0;
		add(lblAlgorithm, gbc_lblAlgorithm);
		
		btnAlgorithmMD2 = new JRadioButton("MD2");
		GridBagConstraints gbc_btnAlgorithmMD2 = new GridBagConstraints();
		gbc_btnAlgorithmMD2.insets = new Insets(10, 0, 10, 10);
		gbc_btnAlgorithmMD2.gridx = 1;
		gbc_btnAlgorithmMD2.gridy = 0;
		add(btnAlgorithmMD2, gbc_btnAlgorithmMD2);
		group.add(btnAlgorithmMD2);
		
		btnAlgorithmMD5 = new JRadioButton("MD5");
		GridBagConstraints gbc_btnAlgorithmMD5 = new GridBagConstraints();
		gbc_btnAlgorithmMD5.insets = new Insets(10, 0, 10, 10);
		gbc_btnAlgorithmMD5.gridx = 2;
		gbc_btnAlgorithmMD5.gridy = 0;
		add(btnAlgorithmMD5, gbc_btnAlgorithmMD5);
		group.add(btnAlgorithmMD5);
		
		btnAlgorithmSHA1 = new JRadioButton("SHA-1");
		GridBagConstraints gbc_btnAlgorithmSHA1 = new GridBagConstraints();
		gbc_btnAlgorithmSHA1.insets = new Insets(10, 0, 10, 10);
		gbc_btnAlgorithmSHA1.gridx = 3;
		gbc_btnAlgorithmSHA1.gridy = 0;
		add(btnAlgorithmSHA1, gbc_btnAlgorithmSHA1);
		group.add(btnAlgorithmSHA1);
		
		btnAlgorithmSHA256 = new JRadioButton("SHA-256");
		btnAlgorithmSHA256.setSelected(true);
		GridBagConstraints gbc_btnAlgorithmSHA256 = new GridBagConstraints();
		gbc_btnAlgorithmSHA256.insets = new Insets(10, 0, 10, 10);
		gbc_btnAlgorithmSHA256.gridx = 4;
		gbc_btnAlgorithmSHA256.gridy = 0;
		add(btnAlgorithmSHA256, gbc_btnAlgorithmSHA256);
		group.add(btnAlgorithmSHA256);
		
		btnAlgorithmSHA384 = new JRadioButton("SHA-384");
		GridBagConstraints gbc_btnAlgorithmSHA384 = new GridBagConstraints();
		gbc_btnAlgorithmSHA384.insets = new Insets(10, 0, 10, 10);
		gbc_btnAlgorithmSHA384.gridx = 5;
		gbc_btnAlgorithmSHA384.gridy = 0;
		add(btnAlgorithmSHA384, gbc_btnAlgorithmSHA384);
		group.add(btnAlgorithmSHA384);
		
		btnAlgorithmSHA512 = new JRadioButton("SHA-512");
		GridBagConstraints gbc_btnAlgorithmSHA512 = new GridBagConstraints();
		gbc_btnAlgorithmSHA512.insets = new Insets(10, 0, 10, 10);
		gbc_btnAlgorithmSHA512.gridx = 6;
		gbc_btnAlgorithmSHA512.gridy = 0;
		add(btnAlgorithmSHA512, gbc_btnAlgorithmSHA512);
		group.add(btnAlgorithmSHA512);
		
		lblFile = new JLabel("File or directory:");
		GridBagConstraints gbc_lblFile = new GridBagConstraints();
		gbc_lblFile.anchor = GridBagConstraints.EAST;
		gbc_lblFile.fill = GridBagConstraints.VERTICAL;
		gbc_lblFile.insets = new Insets(10, 10, 10, 10);
		gbc_lblFile.gridx = 0;
		gbc_lblFile.gridy = 1;
		add(lblFile, gbc_lblFile);
		
		txtFile = new JTextField();
		txtFile.setToolTipText("You can drop a file or a directory here");
		GridBagConstraints gbc_txtFile = new GridBagConstraints();
		gbc_txtFile.weighty = 1.0;
		gbc_txtFile.weightx = 1.0;
		gbc_txtFile.gridwidth = 5;
		gbc_txtFile.insets = new Insets(10, 0, 10, 10);
		gbc_txtFile.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtFile.gridx = 1;
		gbc_txtFile.gridy = 1;
		add(txtFile, gbc_txtFile);
		txtFile.setColumns(10);

		txtFile.setTransferHandler(new DropTransferHandler());

		JButton btnFile = new JButton("...");
		btnFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

				if(chooser.showOpenDialog(InputPanel.this) == JFileChooser.APPROVE_OPTION)
				{
					txtFile.setText(chooser.getSelectedFile().getAbsolutePath());
				}
			}
		});
		GridBagConstraints gbc_btnFile = new GridBagConstraints();
		gbc_btnFile.anchor = GridBagConstraints.WEST;
		gbc_btnFile.insets = new Insets(10, 0, 10, 10);
		gbc_btnFile.gridx = 6;
		gbc_btnFile.gridy = 1;
		add(btnFile, gbc_btnFile);
		
		lblGenerated = new JLabel("Generated hash:");
		GridBagConstraints gbc_lblGenerated = new GridBagConstraints();
		gbc_lblGenerated.anchor = GridBagConstraints.EAST;
		gbc_lblGenerated.insets = new Insets(10, 10, 10, 10);
		gbc_lblGenerated.gridx = 0;
		gbc_lblGenerated.gridy = 2;
		add(lblGenerated, gbc_lblGenerated);
		
		txtHash = new JTextField();
		txtHash.setEditable(false);
		GridBagConstraints gbc_txtHash = new GridBagConstraints();
		gbc_txtHash.gridwidth = 5;
		gbc_txtHash.insets = new Insets(10, 0, 10, 10);
		gbc_txtHash.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtHash.gridx = 1;
		gbc_txtHash.gridy = 2;
		add(txtHash, gbc_txtHash);
		txtHash.setColumns(10);
		
		lblCompare = new JLabel("Compare with:");
		GridBagConstraints gbc_lblCompare = new GridBagConstraints();
		gbc_lblCompare.anchor = GridBagConstraints.EAST;
		gbc_lblCompare.insets = new Insets(10, 10, 10, 10);
		gbc_lblCompare.gridx = 0;
		gbc_lblCompare.gridy = 3;
		add(lblCompare, gbc_lblCompare);
		
		txtCompare = new JTextField();
		GridBagConstraints gbc_txtCompare = new GridBagConstraints();
		gbc_txtCompare.gridwidth = 5;
		gbc_txtCompare.insets = new Insets(10, 0, 10, 10);
		gbc_txtCompare.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtCompare.gridx = 1;
		gbc_txtCompare.gridy = 3;
		add(txtCompare, gbc_txtCompare);
		txtCompare.setColumns(10);
		
		btnCompare = new JButton("Compare");
		btnCompare.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				compareHash();
			}
		});
		GridBagConstraints gbc_btnCompare = new GridBagConstraints();
		gbc_btnCompare.anchor = GridBagConstraints.WEST;
		gbc_btnCompare.insets = new Insets(10, 0, 10, 10);
		gbc_btnCompare.gridx = 6;
		gbc_btnCompare.gridy = 3;
		add(btnCompare, gbc_btnCompare);
	}

	public void internationalize()
	{
		ResourceBundle resourceBundle = MainWindow.getResourceBundle();
		
		lblAlgorithm.setText(resourceBundle.getString("label.algorithm"));
		lblFile.setText(resourceBundle.getString("label.file"));
		lblGenerated.setText(resourceBundle.getString("label.generated"));
		lblCompare.setText(resourceBundle.getString("label.compare"));
		btnCompare.setText(resourceBundle.getString("button.compare"));
		
		generateHashBeforeMessage = resourceBundle.getString("message.generateHashBeforeMessage");
		generateAnHash = resourceBundle.getString("message.generateAnHash");
		pasteAnHashBeforeMessage = resourceBundle.getString("message.pasteAnHashBeforeMessage");
		pasteAnHash = resourceBundle.getString("message.pasteAnHash");
		hashCodesMatchMessage = resourceBundle.getString("message.hashCodesMatchMessage");
		identical = resourceBundle.getString("message.identical");
		hashCodesDoesNotMatchMessage = resourceBundle.getString("message.hashCodesDoesNotMatchMessage");
		doesNotMatch = resourceBundle.getString("message.doesNotMatch");
	}

	public void pasteHash(String hash)
	{
		txtCompare.setText(hash);
	}
	
	private void compareHash()
	{
		if(txtHash.getText().equals(""))
		{
			JOptionPane.showMessageDialog(this, generateHashBeforeMessage, generateAnHash, JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		if(txtCompare.getText().trim().equals(""))
		{
			JOptionPane.showMessageDialog(this, pasteAnHashBeforeMessage, pasteAnHash, JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		if(txtHash.getText().equals(txtCompare.getText().replace(" ", "").toUpperCase()))
		{
			JOptionPane.showMessageDialog(this, hashCodesMatchMessage, identical, JOptionPane.INFORMATION_MESSAGE);
		}
		else
		{
			JOptionPane.showMessageDialog(this, hashCodesDoesNotMatchMessage, doesNotMatch, JOptionPane.WARNING_MESSAGE);
		}
	}
	
}
