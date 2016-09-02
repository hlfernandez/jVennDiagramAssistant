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
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import es.uvigo.ei.sing.vda.core.VennDiagramCreator;
import es.uvigo.ei.sing.vda.core.entities.NamedRSet;

public class VennDiagramAssistant extends JPanel {
	private static final long serialVersionUID = 1L;
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
			JButton addSet = new JButton(new AbstractAction("Add set") {
				private static final long serialVersionUID = 1L;

				@Override
				public void actionPerformed(ActionEvent e) {
					addSet();
				}
			});
			

			JButton generateRCode = new JButton(new AbstractAction("Generate R code") {
				private static final long serialVersionUID = 1L;
				
				@Override
				public void actionPerformed(ActionEvent e) {
					generateRCode();
				}

			});
			
			this.northPane.add(addSet);
			this.northPane.add(Box.createHorizontalGlue());
			this.northPane.add(getLibrarySelectionPanel());
			this.northPane.add(Box.createHorizontalGlue());
			this.northPane.add(generateRCode);
		}
		return this.northPane;
	}
	
	private Component getLibrarySelectionPanel() {
		if(this.librarySelectionPanel == null) {
			this.librarySelectionPanel = new JPanel(new FlowLayout());
			this.librarySelectionPanel.setOpaque(false);
			this.librarySelectionPanel.add(new JLabel("R library"));
			this.librarySelectionPanel.add(getLibrarySelectionComponent());
		}
		return this.librarySelectionPanel;
	}
	
	private Component getLibrarySelectionComponent() {
		if(this.librarySelectionCmb == null) {
			this.librarySelectionCmb = new JComboBox<VennDiagramCreator>(
				VennDiagramCreator.getImplementations());
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
		List<NamedRSet<String>> collect = inputSets.stream()
			.map(SetInput::getNamedRSet)
			.collect(Collectors.toList());
		this.codeTA.setText(this.vennDiagramCreator.getRCode(collect));
	}
}
