package es.uvigo.ei.sing.vda.core;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import es.uvigo.ei.sing.vda.core.entities.NamedRSet;

public class RLibraryVennDiagramCreator implements VennDiagramCreator {

	private static final Collector<CharSequence, ?, String> JOINING = 
		Collectors.joining(", ");

	@Override
	public String getRCode(List<NamedRSet<String>> sets) {
		StringBuilder sb = new StringBuilder();
		sets.forEach(sI -> sb.append(sI.getRCode() + "\n"));
		sb
			.append("\nlibrary(VennDiagram)\n\n")
			.append("venn.diagram(x=list(")
			.append(getSetsRNames(sets).map(s -> s.concat("=" + s)).collect(JOINING))
			.append(")")
			.append(", filename=\"venn-diagram.tiff\"")
			.append(", fill = ")
			.append(RColors.colorList(sets.size()))
			.append(")");
		return sb.toString();
	}

	private Stream<String> getSetsRNames(List<NamedRSet<String>> sets) {
		return sets.stream().map(NamedRSet::getRSetName);
	}

	@Override
	public String getDescription() {
		return "VennDiagram";
	}

	@Override
	public String toString() {
		return getDescription();
	}
}