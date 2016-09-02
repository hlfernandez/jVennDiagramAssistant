package es.uvigo.ei.sing.vda.core;

import java.util.List;

import es.uvigo.ei.sing.vda.core.entities.NamedRSet;

public interface VennDiagramCreator {
	public String getRCode(List<NamedRSet<String>> sets);

	public String getDescription();

	public static VennDiagramCreator[] getImplementations() {
		return new VennDiagramCreator[] { 
			new RLibraryVennDiagramCreator(),
			new DefaultVennDiagramCreator() 
		};
	}
}
