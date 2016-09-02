package es.uvigo.ei.sing.vda.core;

import java.io.Serializable;
import java.util.List;

import es.uvigo.ei.sing.vda.core.entities.NamedRSet;

public class VennDiagramDesign implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<NamedRSet<String>> sets;

	public VennDiagramDesign(List<NamedRSet<String>> sets) {
		this.sets = sets;
	}
	
	public List<NamedRSet<String>> getSets() {
		return sets;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if(!(o instanceof VennDiagramDesign)) {
			return false;
		}
		VennDiagramDesign other = (VennDiagramDesign) o;
		return this.sets.equals(other.sets);
	}
}
