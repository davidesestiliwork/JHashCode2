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

import it.dsestili.jhashcode.core.DirectoryWorkerThread;
import it.dsestili.jhashcode.core.ShutdownHook;
import it.dsestili.jhashcode.core.Utils;
import it.dsestili.jhashcode.core.WorkerThread;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Toolkit;
import javax.swing.border.BevelBorder;
import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainWindow extends JFrame
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private WorkerThread workerThread = null;
	private static JMenuItem mnFile, menuExit, mnHash, copyItem, pasteItem, mnLEF, systemLEF, nimbusLEF, metalLEF,
			mnNewMenu, menuAbout, mnLanguage, mnEnglish, mnItalian, mnSpanish;
	private JPanel contentPane;
	private InputPanel inputPanel;
	private StatusPanel statusPanel;
	private JButton btnStart;
	private static final String NAME_AND_VERSION = "JHashCode2";
	private static Locale enLocale, itLocale, esLocale, currentLocale;
	private static ResourceBundle resourceBundle;
	private static String lookAndFeelNotFound;
	private static String notFound;
	private static String cancelOperation, cancelOperationTitle;
	private static String quit, quitTitle;
	private static boolean excludeSymbolicLinks = true, excludeHiddenFiles = true;
	private static String folderToExclude;

	public static String getFolderToExclude()
	{
		return folderToExclude;
	}
	
	public static boolean getExcludeSymbolicLinks()
	{
		return excludeSymbolicLinks;
	}

	public static boolean getExcludeHiddenFiles()
	{
		return excludeHiddenFiles;
	}
	
	public static void setExcludeSymbolicLinks(boolean exclude)
	{
		excludeSymbolicLinks = exclude;
	}

	public static void setExcludeHiddenFiles(boolean exclude)
	{
		excludeHiddenFiles = exclude;
	}
	
	public static JMenuItem getLanguageMenu()
	{
		return mnLanguage;
	}

	public static ResourceBundle getResourceBundle()
	{
		if (resourceBundle == null)
		{
			resourceBundle = ResourceBundle.getBundle("MessagesBundle", currentLocale);
		}
		return resourceBundle;
	}

	public static Locale getCurrentLocale()
	{
		return currentLocale;
	}

	static
	{
		enLocale = new Locale("en", "US");
		itLocale = new Locale("it", "IT");
		esLocale = new Locale("es", "ES");
		setEnglishLocale();
	}

	public static void setEnglishLocale()
	{
		resourceBundle = null;
		currentLocale = enLocale;
		Locale.setDefault(currentLocale);
	}

	private void setEnglishLanguage()
	{
		setEnglishLocale();
		internationalize();
	}

	public static void setItalianLocale()
	{
		resourceBundle = null;
		currentLocale = itLocale;
		Locale.setDefault(currentLocale);
	}

	private void setItalianLanguage()
	{
		setItalianLocale();
		internationalize();
	}

	public static void setSpanishLocale()
	{
		resourceBundle = null;
		currentLocale = esLocale;
		Locale.setDefault(currentLocale);
	}

	private void setSpanishLanguage()
	{
		setSpanishLocale();
		internationalize();
	}

	/**
	 * * Launch the application.
	 */
	public static void main(String[] args)
	{
		if(args.length >= 2)
		{
			try
			{
				excludeSymbolicLinks = (Integer.parseInt(args[0]) == 0) ? false : true;
			}
			catch(NumberFormatException e)
			{
				e.printStackTrace();
				return;
			}
			
			try
			{
				excludeHiddenFiles = (Integer.parseInt(args[1]) == 0) ? false : true;
			}
			catch(NumberFormatException e)
			{
				e.printStackTrace();
				return;
			}
			
			if(args.length > 2)
			{
				folderToExclude = args[2];
			}
		}
		else
		{
			System.out.println("Usage: param 1: exclude symbolic links (0 or 1), param 2: exclude hidden files (0 or 1), param 3: folder to exclude");
			return;
		}
		
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Throwable e)
		{
			System.out.println(lookAndFeelNotFound);
		}
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					MainWindow frame = UIFactory.MainWindowFactory.getInstance();
					centerScreen(frame);
					frame.setVisible(true);
					Runtime.getRuntime().addShutdownHook(new ShutdownHook(frame));
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	public static void centerScreen(Component window)
	{
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int w = window.getSize().width;
		int h = window.getSize().height;
		int x = (dim.width - w) / 2;
		int y = (dim.height - h) / 2;
		window.setLocation(x, y);
	}

	public void closeApp()
	{
		if (workerThread != null)
		{
			workerThread.interruptOperation();
			try
			{
				workerThread.join(10000);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

	private void promptBeforeExit()
	{
		int result = JOptionPane.showConfirmDialog(null, quit, quitTitle, JOptionPane.YES_NO_OPTION);
		if (result == JOptionPane.YES_OPTION)
		{
			System.exit(0);
		}
	}

	public void setTitleAndTime(String elapsedTime)
	{
		setTitle(NAME_AND_VERSION + elapsedTime);
	}

	/**
	 * * Create the frame.
	 */
	public MainWindow()
	{
		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent arg0)
			{
				promptBeforeExit();
			}
		});
		setResizable(false);
		setTitle(NAME_AND_VERSION);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 841, 435);
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		mnFile = new JMenu("File");
		menuBar.add(mnFile);
		menuExit = new JMenuItem("Exit");
		menuExit.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				promptBeforeExit();
			}
		});
		mnFile.add(menuExit);
		mnHash = new JMenu("Hash");
		menuBar.add(mnHash);
		copyItem = new JMenuItem("Copy to clipboard (Generated hash)");
		copyItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				StringSelection stringSelection = new StringSelection(inputPanel.getGeneratedHash());
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				clipboard.setContents(stringSelection, null);
			}
		});
		mnHash.add(copyItem);
		pasteItem = new JMenuItem("Paste from clipboard (Compare with)");
		pasteItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
					String hash = (String) clipboard.getData(DataFlavor.stringFlavor);
					inputPanel.pasteHash(hash);
				}
				catch (Exception ex)
				{
				}
			}
		});
		mnHash.add(pasteItem);
		mnLEF = new JMenu("Look and Feel");
		menuBar.add(mnLEF);
		systemLEF = new JMenuItem("System");
		systemLEF.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					SwingUtilities.updateComponentTreeUI(MainWindow.this);
				}
				catch (Exception ex)
				{
					JOptionPane.showMessageDialog(null, lookAndFeelNotFound, notFound, JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		mnLEF.add(systemLEF);
		nimbusLEF = new JMenuItem("Nimbus");
		nimbusLEF.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
					SwingUtilities.updateComponentTreeUI(MainWindow.this);
				}
				catch (Exception ex)
				{
					JOptionPane.showMessageDialog(null, lookAndFeelNotFound, notFound, JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		mnLEF.add(nimbusLEF);
		metalLEF = new JMenuItem("Metal");
		metalLEF.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
					SwingUtilities.updateComponentTreeUI(MainWindow.this);
				}
				catch (Exception ex)
				{
					JOptionPane.showMessageDialog(null, lookAndFeelNotFound, notFound, JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		mnLEF.add(metalLEF);
		mnLanguage = new JMenu("Language");
		menuBar.add(mnLanguage);
		mnEnglish = new JMenuItem("English");
		mnEnglish.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				setEnglishLanguage();
			}
		});
		mnLanguage.add(mnEnglish);
		mnSpanish = new JMenuItem("Espanol");
		mnSpanish.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				setSpanishLanguage();
			}
		});
		mnLanguage.add(mnSpanish);
		mnItalian = new JMenuItem("Italian");
		mnItalian.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				setItalianLanguage();
			}
		});
		mnLanguage.add(mnItalian);
		mnNewMenu = new JMenu("   ?   ");
		menuBar.add(mnNewMenu);
		menuAbout = new JMenuItem("About");
		menuAbout.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				JOptionPane.showMessageDialog(null, "JHashCode2 (C) 2017 Davide Sestili");
			}
		});
		mnNewMenu.add(menuAbout);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]
		{ 535, 0 };
		gbl_contentPane.rowHeights = new int[]
		{ 129, 37, 0, 0 };
		gbl_contentPane.columnWeights = new double[]
		{ 1.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[]
		{ 0.0, 0.0, 1.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);
		inputPanel = UIFactory.InputPanelFactory.getInstance();
		inputPanel.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		GridBagConstraints gbc_inputPanel = new GridBagConstraints();
		gbc_inputPanel.fill = GridBagConstraints.HORIZONTAL;
		gbc_inputPanel.insets = new Insets(10, 10, 10, 10);
		gbc_inputPanel.gridx = 0;
		gbc_inputPanel.gridy = 0;
		contentPane.add(inputPanel, gbc_inputPanel);
		btnStart = UIFactory.MainButtonFactory.getInstance();
		btnStart.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				validateInput();
			}
		});
		GridBagConstraints gbc_btnStart = new GridBagConstraints();
		gbc_btnStart.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnStart.insets = new Insets(10, 10, 10, 10);
		gbc_btnStart.gridx = 0;
		gbc_btnStart.gridy = 1;
		contentPane.add(btnStart, gbc_btnStart);
		statusPanel = UIFactory.StatusPanelFactory.getInstance();
		statusPanel.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		GridBagConstraints gbc_statusPanel = new GridBagConstraints();
		gbc_statusPanel.insets = new Insets(10, 10, 10, 10);
		gbc_statusPanel.fill = GridBagConstraints.BOTH;
		gbc_statusPanel.gridx = 0;
		gbc_statusPanel.gridy = 2;
		contentPane.add(statusPanel, gbc_statusPanel);
		inputPanel.getTxtFile().addKeyListener(new KeyAdapter()
		{
			public void keyPressed(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					validateInput();
				}
			}
		});
		setEnglishLanguage();
		setSize(841, 470);
	}

	private void internationalize()
	{
		mnFile.setText(getResourceBundle().getString("menu.file"));
		menuExit.setText(getResourceBundle().getString("menu.exit"));
		mnHash.setText(getResourceBundle().getString("menu.hash"));
		copyItem.setText(getResourceBundle().getString("menu.copyItem"));
		pasteItem.setText(getResourceBundle().getString("menu.pasteItem"));
		mnLEF.setText(getResourceBundle().getString("menu.lookandfeel"));
		systemLEF.setText(getResourceBundle().getString("menu.lookandfeel.system"));
		nimbusLEF.setText(getResourceBundle().getString("menu.lookandfeel.nimbus"));
		metalLEF.setText(getResourceBundle().getString("menu.lookandfeel.metal"));
		mnNewMenu.setText(getResourceBundle().getString("menu.newMenu"));
		menuAbout.setText(getResourceBundle().getString("menu.about"));
		mnLanguage.setText(getResourceBundle().getString("menu.language"));
		mnEnglish.setText(getResourceBundle().getString("menu.language.english"));
		mnItalian.setText(getResourceBundle().getString("menu.language.italian"));
		mnSpanish.setText(getResourceBundle().getString("menu.language.spanish"));
		inputPanel.internationalize();
		JButton btnStart = UIFactory.MainButtonFactory.getInstance();
		btnStart.setText(getResourceBundle().getString("button.start"));
		statusPanel.internationalize();
		lookAndFeelNotFound = getResourceBundle().getString("lookAndFeel.notFound.message");
		notFound = getResourceBundle().getString("lookAndFeel.notFound");
		cancelOperation = getResourceBundle().getString("button.start.cancel.message");
		cancelOperationTitle = getResourceBundle().getString("button.start.cancel.message.title");
		quit = getResourceBundle().getString("mainWindow.quit");
		quitTitle = getResourceBundle().getString("mainWindow.quit.title");
	}

	private void validateInput()
	{
		if (inputPanel.isEnabled())
		{
			File file = new File(inputPanel.getFileName());
			if (!file.exists())
			{
				Object[] arguments =
				{ file.getName() };
				String output = Utils.getInternationalizedString(arguments, "button.start.warning.message");
				String label = getResourceBundle().getString("button.start.warning");
				JOptionPane.showMessageDialog(this, output, label, JOptionPane.WARNING_MESSAGE);
				return;
			}
			if (!file.isFile())
			{
				workerThread = new DirectoryWorkerThread(file);
				workerThread.start();
			}
			else
			{
				workerThread = new WorkerThread(file);
				workerThread.start();
			}
		}
		else
		{
			int result = JOptionPane.showConfirmDialog(null, cancelOperation, cancelOperationTitle,
					JOptionPane.YES_NO_OPTION);
			if (result == JOptionPane.YES_OPTION)
			{
				workerThread.interruptOperation();
			}
		}
	}
}
