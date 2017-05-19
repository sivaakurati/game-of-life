/**
 * 
 */
package com.enuminfo.gameoflife;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

import com.enuminfo.util.AboutDialog;
import com.enuminfo.util.AlertBox;
import com.enuminfo.util.EasyFile;
import com.enuminfo.util.LineEnumerator;
import com.enuminfo.util.TextFileDialog;

/**
 * @author Kumar
 */
@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
public class StandaloneGameOfLife extends GameOfLife {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Frame appletFrame;
	private String[] args;
	private GameOfLifeGridIO gridIO;

	public static void main(String args[]) {
		StandaloneGameOfLife gameOfLife = new StandaloneGameOfLife();
		gameOfLife.args = args;
		new AppletFrame("Game of Life", gameOfLife);
	}

	public void init(Frame parent) {
		appletFrame = parent;
		getParams();
		setBackground(new Color(0x999999));
		gameOfLifeGrid = new GameOfLifeGrid(cellCols, cellRows);
		gridIO = new GameOfLifeGridIO(gameOfLifeGrid);
		gameOfLifeCanvas = new CellGridCanvas(gameOfLifeGrid, cellSize);
		try {
			DropTarget dt = new DropTarget(gameOfLifeCanvas, DnDConstants.ACTION_COPY_OR_MOVE, new MyDropListener());
		} catch (NoClassDefFoundError e) {

		}
		controls = new GameOfLifeControls();
		controls.addGameOfLifeControlsListener(this);
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints canvasContraints = new GridBagConstraints();
		setLayout(gridbag);
		canvasContraints.fill = GridBagConstraints.BOTH;
		canvasContraints.weightx = 1;
		canvasContraints.weighty = 1;
		canvasContraints.gridx = GridBagConstraints.REMAINDER;
		canvasContraints.gridy = 0;
		canvasContraints.anchor = GridBagConstraints.CENTER;
		gridbag.setConstraints(gameOfLifeCanvas, canvasContraints);
		add(gameOfLifeCanvas);
		GridBagConstraints controlsContraints = new GridBagConstraints();
		canvasContraints.gridx = GridBagConstraints.REMAINDER;
		canvasContraints.gridy = 1;
		controlsContraints.gridx = GridBagConstraints.REMAINDER;
		gridbag.setConstraints(controls, controlsContraints);
		add(controls);
		setVisible(true);
		validate();
	}

	public void readShape() {
		if (args.length > 0) {
			gridIO.openShape(args[0]);
			reset();
		} else {
			try {
				setShape(ShapeCollection.getShapeByName("Glider"));
			} catch (ShapeException e) {

			}
		}
	}

	public String getParameter(String parm) {
		return System.getProperty(parm);
	}

	public void alert(String s) {
		new AlertBox(appletFrame, "Alert", s);
	}

	public void showStatus(String s) {
		// do nothing
	}

	protected GameOfLifeGridIO getGameOfLifeGridIO() {
		return gridIO;
	}

	class MyDropListener implements DropTargetListener {

		private final DataFlavor urlFlavor = new DataFlavor("application/x-java-url; class=java.net.URL",
				"Game of Life URL");

		public void dragEnter(DropTargetDragEvent event) {
			if (event.isDataFlavorSupported(DataFlavor.javaFileListFlavor) || event.isDataFlavorSupported(urlFlavor)) {
				return;
			}
			event.rejectDrag();
		}

		public void dragExit(DropTargetEvent event) {

		}

		public void dragOver(DropTargetDragEvent event) {

		}

		public void dropActionChanged(DropTargetDragEvent event) {

		}

		public void drop(DropTargetDropEvent event) {
			if (event.isDataFlavorSupported(urlFlavor)) {
				try {
					event.acceptDrop(DnDConstants.ACTION_COPY);
					Transferable trans = event.getTransferable();
					URL url = (URL) (trans.getTransferData(urlFlavor));
					String urlStr = url.toString();
					gridIO.openShape(url);
					reset();
					event.dropComplete(true);
				} catch (Exception e) {
					event.dropComplete(false);
				}
			} else if (event.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
				try {
					event.acceptDrop(DnDConstants.ACTION_COPY);
					Transferable trans = event.getTransferable();
					java.util.List list = (java.util.List) (trans.getTransferData(DataFlavor.javaFileListFlavor));
					File droppedFile = (File) list.get(0);
					gridIO.openShape(droppedFile.getPath());
					reset();
					event.dropComplete(true);
				} catch (Exception e) {
					event.dropComplete(false);
				}
			}
		}
	}

	class GameOfLifeGridIO {

		public final String FILE_EXTENSION = ".cells";
		private GameOfLifeGrid grid;
		private String filename;

		public GameOfLifeGridIO(GameOfLifeGrid grid) {
			this.grid = grid;
		}

		public void openShape() {
			openShape((String) null);
		}

		public void openShape(String filename) {
			int col = 0;
			int row = 0;
			boolean cell;
			boolean nextLine = false;
			EasyFile file;
			try {
				if (filename != null) {
					file = new EasyFile(filename);
				} else {
					file = new EasyFile(appletFrame, "Open Game of Life file");
				}
				openShape(file);
			} catch (FileNotFoundException e) {
				new AlertBox(appletFrame, "File not found", "Couldn't open this file.\n" + e.getMessage());
			} catch (IOException e) {
				new AlertBox(appletFrame, "File read error", "Couldn't read this file.\n" + e.getMessage());
			}
		}

		public void openShape(URL url) {
			int col = 0;
			int row = 0;
			boolean cell;
			boolean nextLine = false;
			EasyFile file;
			String text;
			try {
				if (url != null) {
					file = new EasyFile(url);
					openShape(file);
				}
			} catch (FileNotFoundException e) {
				new AlertBox(appletFrame, "URL not found", "Couldn't open this URL.\n" + e.getMessage());
			} catch (IOException e) {
				new AlertBox(appletFrame, "URL read error", "Couldn't read this URL.\n" + e.getMessage());
			}
		}

		public void openShape(EasyFile file) throws IOException {
			Shape shape = makeShape(file.getFileName(), file.readText());
			setShape(shape);
		}

		public void setShape(Shape shape) {
			int width, height;
			Dimension shapeDim = shape.getDimension();
			Dimension gridDim = grid.getDimension();
			if (shapeDim.width > gridDim.width || shapeDim.height > gridDim.height) {
				Toolkit toolkit = getToolkit();
				Dimension screenDim = toolkit.getScreenSize();
				Dimension frameDim = appletFrame.getSize();
				int cellSize = getCellSize();
				width = frameDim.width + cellSize * (shapeDim.width - gridDim.width);
				height = frameDim.height + cellSize * (shapeDim.height - gridDim.height);
				if (width > screenDim.width || height > screenDim.height) {
					int newCellSize = GameOfLifeControls.SIZE_SMALL;
					width = frameDim.width + newCellSize * shapeDim.width - cellSize * gridDim.width;
					height = frameDim.height + newCellSize * shapeDim.height - cellSize * gridDim.height;
					gameOfLifeCanvas.setAfterWindowResize(shape, newCellSize);
					controls.setZoom(GameOfLifeControls.SMALL);
				} else {
					gameOfLifeCanvas.setAfterWindowResize(shape, cellSize);
				}
				if (width < 400)
					width = 400;
				appletFrame.setSize(width, height);
				return;
			}
			try {
				gameOfLifeCanvas.setShape(shape);
			} catch (ShapeException e) {

			}
		}

		public Shape makeShape(String name, String text) {
			int col = 0;
			int row = 0;
			boolean cell;
			int[][] cellArray;
			Vector cells = new Vector();
			if (text.length() == 0)
				return null;
			grid.clear();
			Enumeration enumeration = new LineEnumerator(text);
			while (enumeration.hasMoreElements()) {
				String line = (String) enumeration.nextElement();
				if (line.startsWith("#") || line.startsWith("!"))
					continue;
				char[] ca = line.toCharArray();
				for (col = 0; col < ca.length; col++) {
					switch (ca[col]) {
					case '*':
					case 'O':
					case 'o':
					case 'X':
					case 'x':
					case '1':
						cell = true;
						break;
					default:
						cell = false;
						break;
					}
					if (cell)
						cells.addElement(new int[] { col, row });
				}
				row++;
			}
			cellArray = new int[cells.size()][];
			for (int i = 0; i < cells.size(); i++)
				cellArray[i] = (int[]) cells.get(i);
			return new Shape(name, cellArray);
		}

		public void saveShape() {
			int colEnd = 0;
			int rowEnd = 0;
			Dimension dim = grid.getDimension();
			int colStart = dim.width;
			int rowStart = dim.height;
			String lineSeperator = System.getProperty("line.separator");
			StringBuffer text = new StringBuffer("!Generator: Game of Life (http://www.bitstorm.org/gameoflife/)"
					+ lineSeperator + "!Variation: 23/3" + lineSeperator + "!" + lineSeperator);
			for (int row = 0; row < dim.height; row++) {
				for (int col = 0; col < dim.width; col++) {
					if (grid.getCell(col, row)) {
						if (row < rowStart)
							rowStart = row;
						if (col < colStart)
							colStart = col;
						if (row > rowEnd)
							rowEnd = row;
						if (col > colEnd)
							colEnd = col;
					}
				}
			}
			for (int row = rowStart; row <= rowEnd; row++) {
				for (int col = colStart; col <= colEnd; col++) {
					text.append(grid.getCell(col, row) ? 'O' : '-');
				}
				text.append(lineSeperator);
			}
			EasyFile file;
			try {
				file = new EasyFile(appletFrame, "Save Game of Life file");
				file.setFileName(filename);
				file.setFileExtension(FILE_EXTENSION);
				file.writeText(text.toString());
			} catch (FileNotFoundException e) {
				new AlertBox(appletFrame, "File error", "Couldn't open this file.\n" + e.getMessage());
			} catch (IOException e) {
				new AlertBox(appletFrame, "File error", "Couldn't write to this file.\n" + e.getMessage());
			}
		}
	}
}

@SuppressWarnings("deprecation")
class AppletFrame extends Frame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final GameOfLife applet;

	public AppletFrame(String title, StandaloneGameOfLife applet) {
		super(title);
		this.applet = applet;
		URL iconURL = this.getClass().getResource("icon.gif");
		Image icon = Toolkit.getDefaultToolkit().getImage(iconURL);
		this.setIconImage(icon);
		enableEvents(Event.WINDOW_DESTROY);
		MenuBar menubar = new MenuBar();
		Menu fileMenu = new Menu("File", true);
		MenuItem readMenuItem = new MenuItem("Open...");
		readMenuItem.addActionListener(new ActionListener() {
			public synchronized void actionPerformed(ActionEvent e) {
				getStandaloneGameOfLife().getGameOfLifeGridIO().openShape();
				getStandaloneGameOfLife().reset();
			}
		});
		MenuItem writeMenuItem = new MenuItem("Save...");
		writeMenuItem.addActionListener(new ActionListener() {
			public synchronized void actionPerformed(ActionEvent e) {
				getStandaloneGameOfLife().getGameOfLifeGridIO().saveShape();
			}
		});
		MenuItem quitMenuItem = new MenuItem("Exit");
		quitMenuItem.addActionListener(new ActionListener() {
			public synchronized void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		Menu helpMenu = new Menu("Help", true);
		MenuItem manualMenuItem = new MenuItem("Manual");
		manualMenuItem.addActionListener(new ActionListener() {
			public synchronized void actionPerformed(ActionEvent e) {
				showManualDialog();
			}
		});
		MenuItem licenseMenuItem = new MenuItem("License");
		licenseMenuItem.addActionListener(new ActionListener() {
			public synchronized void actionPerformed(ActionEvent e) {
				showLicenseDialog();
			}
		});
		MenuItem aboutMenuItem = new MenuItem("About");
		aboutMenuItem.addActionListener(new ActionListener() {
			public synchronized void actionPerformed(ActionEvent e) {
				showAboutDialog();
			}
		});
		fileMenu.add(readMenuItem);
		fileMenu.add(writeMenuItem);
		fileMenu.addSeparator();
		fileMenu.add(quitMenuItem);
		helpMenu.add(manualMenuItem);
		helpMenu.add(licenseMenuItem);
		helpMenu.add(aboutMenuItem);
		menubar.add(fileMenu);
		menubar.add(helpMenu);
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints appletContraints = new GridBagConstraints();
		setLayout(gridbag);
		appletContraints.fill = GridBagConstraints.BOTH;
		appletContraints.weightx = 1;
		appletContraints.weighty = 1;
		gridbag.setConstraints(applet, appletContraints);
		setMenuBar(menubar);
		setResizable(true);
		add(applet);
		Toolkit screen = getToolkit();
		Dimension screenSize = screen.getScreenSize();
		if (screenSize.width >= 640 && screenSize.height >= 480)
			setLocation((screenSize.width - 550) / 2, (screenSize.height - 400) / 2);
		applet.init(this);
		applet.start();
		pack();
		applet.readShape();
		show();
		toFront();
	}

	public void processEvent(AWTEvent e) {
		if (e.getID() == Event.WINDOW_DESTROY)
			System.exit(0);
	}

	private void showAboutDialog() {
		Properties properties = System.getProperties();
		String jvmProperties = "Java VM " + properties.getProperty("java.version") + " from "
				+ properties.getProperty("java.vendor");
		Point point = getLocation();
		new AboutDialog(this, "About the Game of Life", new String[] { "Version 0.0.1-SNAPSHOT - Copyright 2017 Sivakumar AKURATI",
				"", jvmProperties }, "about.jpg", point.x + 100, point.y + 60);
	}

	private void showManualDialog() {
		Point point = getLocation();
		new TextFileDialog(this, "Game of Life Manual", "manual.txt", point.x + 60, point.y + 60);
	}

	private void showLicenseDialog() {
		Point point = getLocation();
		new TextFileDialog(this, "Game of Life License", "license.txt", point.x + 60, point.y + 60);
	}

	private StandaloneGameOfLife getStandaloneGameOfLife() {
		return (StandaloneGameOfLife) applet;
	}
}
