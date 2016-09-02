package es.uvigo.ei.sing.vda.gui;

import static es.uvigo.ei.sing.vda.core.RColors.R_COLORS;
import static es.uvigo.ei.sing.vda.gui.UISettings.BG_COLOR;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import es.uvigo.ei.sing.vda.core.VennDiagramCreator;
import es.uvigo.ei.sing.vda.core.VennDiagramDesign;
import es.uvigo.ei.sing.vda.core.entities.NamedRSet;
import es.uvigo.ei.sing.vda.core.io.SerializationVennDiagramDesignWriter;

public class VennDiagramAssistant extends JPanel {
	private static final long serialVersionUID = 1L;
	
	public final static ImageIcon ICON_ADD 	= getResource("icons/add.png");
	public final static ImageIcon ICON_R 	= getResource("icons/r.png");
	public final static ImageIcon ICON_SAVE = getResource("icons/save.png");
	
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

	public VennDiagramAssistant() {
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

	private Component getCenterPane() {
		if(this.centerPane == null) {
			this.centerPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
			this.centerPane.setBackground(BG_COLOR);
			this.centerPane.setOneTouchExpandable(true);
			this.centerPane.setResizeWeight(0.5);
			this.tabbedPane = new JTabbedPane();
			this.centerPane.setLeftComponent(this.tabbedPane);
			this.centerPane.setRightComponent(this.getSouthPane());
			addSet();
		}
		return this.centerPane;
	}
	
	private Component getSouthPane() {
		if(this.southPane == null) {
			this.southPane = new JPanel(new BorderLayout());
			this.southPane.setMinimumSize(new Dimension(100, 200));
			this.codeTA = new JTextArea();
			this.codeTA.setBorder(BorderFactory.createTitledBorder("R code"));
			this.codeTA.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
			this.southPane.add(codeTA, BorderLayout.CENTER);
		}
		return this.southPane;
	}

	private void addSet() {
		String nextSetName = getNextSetName();
		String nextSetColor = R_COLORS[
		     (this.tabbedPane.getTabCount() + 1) % R_COLORS.length];
		SetInput component = new SetInput(nextSetName, nextSetColor);
		component.onComponentNameChanged(() -> {
			componentNameChanged(component);
		});
		this.tabbedPane.addTab(nextSetName, component);
		this.tabbedPane.setSelectedComponent(component);
		this.inputSets.add(component);
	}

	private void componentNameChanged(SetInput component) {
		this.tabbedPane.setTitleAt(
			this.tabbedPane.indexOfComponent(component), component.getSetName()
		);
	}
	
	private String getNextSetName() {
		return "Set " + (this.tabbedPane.getTabCount() + 1);
	}

	private void generateRCode() {
		this.codeTA.setText(this.vennDiagramCreator.getRCode(getSets()));
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
