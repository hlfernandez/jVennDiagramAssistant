package es.uvigo.ei.sing.vda.gui;

import static es.uvigo.ei.sing.vda.core.RColors.R_COLORS;
import static es.uvigo.ei.sing.vda.gui.UISettings.BG_COLOR;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import es.uvigo.ei.sing.vda.core.entities.NamedRSet;

public class SetInput extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private NamedRSet<String> set;
	private JTextField nameTF;
	private JTextArea elementsTA;
	private Runnable onComponentNameChanged;
	private JComboBox<String> colorCombo;
	
	public SetInput(NamedRSet<String> set) {
		this.set = set;
		this.initComponent();
	}
	
	public void onComponentNameChanged(Runnable r) {
		this.onComponentNameChanged = r;
	}

	private void initComponent() {
		this.setLayout(new BorderLayout());
		this.setBackground(BG_COLOR);
		
		elementsTA = new JTextArea();
		this.set.forEach(s -> {
			elementsTA.append(s + "\n");
		});
		
		this.add(getNorthPanel(), BorderLayout.NORTH);
		this.add(new JScrollPane(elementsTA), BorderLayout.CENTER);
	}

	private Component getNorthPanel() {
		JPanel northPanel = new JPanel(new BorderLayout());
		northPanel.setOpaque(false);
		
		nameTF = new JTextField(this.set.getName());
		nameTF.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				SwingUtilities.invokeLater(onComponentNameChanged);
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				SwingUtilities.invokeLater(onComponentNameChanged);
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
			
			}
		});
		
		colorCombo = new JComboBox<String>(R_COLORS);
		colorCombo.setSelectedItem(this.set.getRSetColor());
		JPanel colorPanel = new JPanel(new BorderLayout());
		colorPanel.setOpaque(false);
		colorPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
		colorPanel.add(new JLabel("Color:"), BorderLayout.WEST);
		colorPanel.add(colorCombo, BorderLayout.CENTER);
		
		northPanel.add(new JLabel("Set name:"), BorderLayout.WEST);
		northPanel.add(nameTF, BorderLayout.CENTER);
		northPanel.add(colorPanel, BorderLayout.EAST);
		northPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
		
		return northPanel;
	}

	public NamedRSet<String> getNamedRSet() {
		NamedRSet<String> toret = new NamedRSet<String>(
			nameTF.getText(), (String) colorCombo.getSelectedItem()
		);
		toret.addAll(getSetElements());
		return toret;
	}
		
	private Set<String> getSetElements() {
		return new HashSet<String>(
			Arrays.asList(this.elementsTA.getText().split("\n"))
		);
	}

	public String getSetName() {
		return nameTF.getText();
	}
}
