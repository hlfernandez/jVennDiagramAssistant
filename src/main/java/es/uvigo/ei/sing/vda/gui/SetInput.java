package es.uvigo.ei.sing.vda.gui;

import static es.uvigo.ei.sing.vda.gui.UISettings.BG_COLOR;

import java.awt.BorderLayout;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import es.uvigo.ei.sing.vda.core.entities.NamedRSet;

public class SetInput extends JPanel {
	private static final long serialVersionUID = 1L;
	private String name;
	private JTextField nameTF;
	private JTextArea elementsTA;
	private Runnable onComponentNameChanged;
	
	public SetInput(String name) {
		this.name = name;
		this.initComponent();
	}
	
	public void onComponentNameChanged(Runnable r) {
		this.onComponentNameChanged = r;
	}

	private void initComponent() {
		this.setLayout(new BorderLayout());
		this.setBackground(BG_COLOR);
		JPanel northPanel = new JPanel(new BorderLayout());
		northPanel.setOpaque(false);
		nameTF = new JTextField(this.name);
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
		northPanel.add(new JLabel("Set name:"), BorderLayout.WEST);
		northPanel.add(nameTF, BorderLayout.CENTER);
		
		elementsTA = new JTextArea();
		
		this.add(northPanel, BorderLayout.NORTH);
		this.add(elementsTA, BorderLayout.CENTER);
	}

	public NamedRSet<String> getNamedRSet() {
		NamedRSet<String> toret = new NamedRSet<String>(nameTF.getText());
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
