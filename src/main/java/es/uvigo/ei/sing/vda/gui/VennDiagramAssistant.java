package es.uvigo.ei.sing.vda.gui;

import static es.uvigo.ei.sing.vda.core.RColors.R_COLORS;
import static es.uvigo.ei.sing.vda.gui.UISettings.BG_COLOR;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import es.uvigo.ei.sing.hlfernandez.filechooser.JFileChooserPanel;
import es.uvigo.ei.sing.hlfernandez.filechooser.JFileChooserPanel.Mode;
import es.uvigo.ei.sing.hlfernandez.filechooser.JFileChooserPanel.SelectionMode;
import es.uvigo.ei.sing.hlfernandez.ui.icons.Icons;
import es.uvigo.ei.sing.vda.core.VennDiagramCreator;
import es.uvigo.ei.sing.vda.core.VennDiagramDesign;
import es.uvigo.ei.sing.vda.core.entities.NamedRSet;
import es.uvigo.ei.sing.vda.core.io.SerializationVennDiagramDesignWriter;
import es.uvigo.ei.sing.vda.gui.components.ButtonTabComponent;

public class VennDiagramAssistant extends JPanel {
	private static final long serialVersionUID = 1L;
	
	public final static ImageIcon ICON_ADD 	= getResource("icons/add.png");
	public final static ImageIcon ICON_R 	= getResource("icons/r.png");
	public final static ImageIcon ICON_SAVE = getResource("icons/save.png");
	
	public static final VennDiagramDesign DEFAULT_DESIGN = 
		new VennDiagramDesign(Arrays.asList(
			new NamedRSet<String>("Set 1", R_COLORS[0])	
		));
	
	private VennDiagramDesign design;
	private JPanel northPane;
	private JSplitPane centerPane;
	private JPanel southPane;
	private JTextArea codeTA;
	private VennDiagramCreator vennDiagramCreator = 
		VennDiagramCreator.getImplementations()[0];
	
	private List<SetInput> inputSets = new LinkedList<SetInput>();
	private JTabbedPane tabbedPane;
	private JComboBox<VennDiagramCreator> librarySelectionCmb;
	private JPanel librarySelectionPanel;
	private JFileChooserPanel fileChooserPanel;
	private JFileChooser fileChooser =
		new JFileChooser(System.getProperty("user.home"));

	public VennDiagramAssistant() {
		this(DEFAULT_DESIGN);
	}
	
	public VennDiagramAssistant(VennDiagramDesign design) {
		this.design = design;
		this.initComponent();
	}

	private void initComponent() {
		this.setLayout(new BorderLayout());
		this.setBackground(BG_COLOR);
		this.add(getNorthPane(), BorderLayout.NORTH);
		this.add(getCenterPane(), BorderLayout.CENTER);
		this.configure();
	}

	private void configure() {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				centerPane.setDividerLocation(300);
			}
		});
	}

	private Component getNorthPane() {
		if(this.northPane == null) {
			this.northPane = new JPanel();
			this.northPane.setBackground(BG_COLOR);
			this.northPane.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
			BoxLayout layout = new BoxLayout(this.northPane, BoxLayout.X_AXIS);
			this.northPane.setLayout(layout);
			JButton addSet = new JButton(
				new AbstractAction("", ICON_ADD) {
					private static final long serialVersionUID = 1L;
		
					@Override
					public void actionPerformed(ActionEvent e) {
						addSet();
					}
				});
			JButton generateRCode = new JButton(
				new AbstractAction("", ICON_R) {
					private static final long serialVersionUID = 1L;
					
					@Override
					public void actionPerformed(ActionEvent e) {
						generateRCode();
					}
				});
			
			JButton saveButton = new JButton(
				new AbstractAction("", ICON_SAVE) {
					private static final long serialVersionUID = 1L;
					
					@Override
					public void actionPerformed(ActionEvent e) {
						saveDesign();
					}
				});
			
			this.northPane.add(getLibrarySelectionPanel());
			this.northPane.add(Box.createHorizontalStrut(20));
			this.northPane.add(getFileChooserSelectionPanel());
			this.northPane.add(Box.createHorizontalGlue());
			this.northPane.add(addSet);
			this.northPane.add(generateRCode);
			this.northPane.add(saveButton);
		}
		return this.northPane;
	}

	private Component getLibrarySelectionPanel() {
		if(this.librarySelectionPanel == null) {
			this.librarySelectionPanel = new JPanel();
			BoxLayout layout = new BoxLayout(this.librarySelectionPanel, BoxLayout.X_AXIS);
			this.librarySelectionPanel.setLayout(layout);
			this.librarySelectionPanel.setOpaque(false);
			this.librarySelectionPanel.add(new JLabel("R library: "));
			this.librarySelectionPanel.add(getLibrarySelectionComponent());
		}
		return this.librarySelectionPanel;
	}
	
	private Component getLibrarySelectionComponent() {
		if(this.librarySelectionCmb == null) {
			this.librarySelectionCmb = new JComboBox<VennDiagramCreator>(
				VennDiagramCreator.getImplementations());
			this.librarySelectionCmb.setMaximumSize(new Dimension(200, 50));
			this.librarySelectionCmb.addItemListener(e -> {
				 if (e.getStateChange() == ItemEvent.SELECTED) {
					 libraryChanged();
				 }
			});
		}
		return this.librarySelectionCmb ;
	}

	private void libraryChanged() {
		this.vennDiagramCreator = 
			(VennDiagramCreator) this.librarySelectionCmb.getSelectedItem();
	}

	private JComponent getFileChooserSelectionPanel() {
		if(this.fileChooserPanel == null) {
			this.fileChooserPanel = new JFileChooserPanel(
				Mode.SAVE, this.fileChooser, Icons.ICON_LOOKUP_16, 
				"Image file ", "tiff", SelectionMode.FILES
			);
			this.fileChooserPanel.setMaximumSize(new Dimension(250, 50));
			this.fileChooserPanel.setBackground(BG_COLOR);
			this.fileChooserPanel.setSelectedFile(
				new File(System.getProperty("user.home"), "venn-diagram.tiff")
			);
		}
		return this.fileChooserPanel;
	}

	private Component getCenterPane() {
		if(this.centerPane == null) {
			this.centerPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
			this.centerPane.setBackground(BG_COLOR);
			this.centerPane.setOneTouchExpandable(true);
			this.centerPane.setResizeWeight(0.5);
			this.tabbedPane = new JTabbedPane() {
				private static final long serialVersionUID = 1L;
				
				@Override
				public void addTab(String title, Component component) {
					super.addTab(title, component);
					addCloseButton(component);
				}

				private void addCloseButton(Component component) {
					super.setTabComponentAt(
							super.indexOfComponent(component), 
							new ButtonTabComponent(this, () -> {
								componentRemoved(component);
							})
					);
				}
			};
			this.centerPane.setLeftComponent(this.tabbedPane);
			this.centerPane.setRightComponent(this.getSouthPane());
			this.addDesign();
		}
		return this.centerPane;
	}
	
	private void componentRemoved(Component component) {
		this.inputSets.remove(component);
	}
	
	private void addDesign() {
		this.design.getSets().forEach(s -> {
			SetInput component = getSetInputComponent(s);
			addSetInputComponent(s.getName(), component);
		});
	}

	private Component getSouthPane() {
		if(this.southPane == null) {
			this.southPane = new JPanel(new BorderLayout());
			this.southPane.setMinimumSize(new Dimension(100, 200));
			this.codeTA = new JTextArea();
			this.codeTA.setLineWrap(true);
			this.codeTA.setBorder(BorderFactory.createTitledBorder("R code"));
			this.codeTA.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
			this.southPane.add(new JScrollPane(codeTA), BorderLayout.CENTER);
		}
		return this.southPane;
	}

	private void addSet() {
		String nextSetName = getNextSetName();
		String nextSetColor = R_COLORS[
		    (this.tabbedPane.getTabCount()) % R_COLORS.length];
		SetInput component = getSetInputComponent(
			new NamedRSet<String>(nextSetName, nextSetColor)
		);
		addSetInputComponent(nextSetName, component);
	}

	private void addSetInputComponent(String nextSetName, SetInput component) {
		this.tabbedPane.addTab(nextSetName, component);
		this.tabbedPane.setSelectedComponent(component);
		this.inputSets.add(component);
	}

	private SetInput getSetInputComponent(NamedRSet<String> set) {
		SetInput component = new SetInput(set);
		component.onComponentNameChanged(() -> {
			componentNameChanged(component);
		});
		return component;
	}

	private void componentNameChanged(SetInput component) {
		this.tabbedPane.setTitleAt(
			this.tabbedPane.indexOfComponent(component), component.getSetName()
		);
	}
	
	private String getNextSetName() {
		String nextSetName = "";
		int count = 1;
		do {
			nextSetName = "Set " + (this.tabbedPane.getTabCount() + count++);
		} while (!isAvailableSetName(nextSetName));
		return nextSetName;
	}

	private boolean isAvailableSetName(String setName) {
		return 	!getSets().stream()
				.map(NamedRSet::getName)
				.filter( n -> n.equals(setName))
				.findAny().isPresent();
	}

	private void generateRCode() {
		this.codeTA.setText(this.vennDiagramCreator.getRCode(getSets(), getParameters()));
		this.copyCodeToClipboard();
	}
	
	private void copyCodeToClipboard() {
		StringSelection selection = new StringSelection(this.codeTA.getText());
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(selection, selection);
	}

	private Map<String, String> getParameters() {
		Map<String, String> parameters = new HashMap<>();
		parameters.put(
			VennDiagramCreator.OUTPUT_FILE, 
			filePathToR(this.fileChooserPanel.getSelectedFile().getAbsolutePath())
		);
		return parameters;
	}

	private String filePathToR(String absolutePath) {
		return absolutePath.replace('\\', '/');
	}

	private List<NamedRSet<String>> getSets() {
		return 	inputSets.stream()
				.map(SetInput::getNamedRSet)
				.collect(Collectors.toList());
	}

	private void saveDesign() {
		JFileChooser jfc = new JFileChooser();
		jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int result = jfc.showSaveDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = jfc.getSelectedFile();
			
			SerializationVennDiagramDesignWriter writer = 
				new SerializationVennDiagramDesignWriter();
			try {
				writer.write(getVennDiagramDesign(), selectedFile);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(this,
					"An error occurred when saving to " + 
					selectedFile.getAbsolutePath(), 
					"Save error",
					JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private VennDiagramDesign getVennDiagramDesign() {
		return new VennDiagramDesign(getSets());
	}
	
	private static final ImageIcon getResource(String resource) {
		return new ImageIcon(VennDiagramAssistant.class.getResource(resource));
	}
}
