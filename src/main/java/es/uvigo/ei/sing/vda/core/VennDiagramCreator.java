package es.uvigo.ei.sing.vda.core;

import java.util.List;
import java.util.Map;

import es.uvigo.ei.sing.vda.core.entities.NamedRSet;

public interface VennDiagramCreator {
	
	public static final String OUTPUT_FILE = "property.output-file";
	
	public String getRCode(List<NamedRSet<String>> sets);
	public String getRCode(List<NamedRSet<String>> sets, Map<String, String> parameters);

	public String getDescription();

	public static VennDiagramCreator[] getImplementations() {
		return new VennDiagramCreator[] { 
			new RLibraryVennDiagramCreator(),
			new DefaultVennDiagramCreator() 
		};
	}
}
