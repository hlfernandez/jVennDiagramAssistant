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
			.append("filename <- \"venn-diagram.tiff\"\n\n")
			.append("venn.diagram(\n\tx=list(")
			.append(getSetsRNames(sets).map(s -> s.concat("=" + s)).collect(JOINING))
			.append(")")
			.append(",\n\tfilename=filename")
			.append(",\n\tfill = ")
			.append(RColors.colorList(getSetsRColors(sets)))
			.append("\n)\n");
		return sb.toString();
	}

	private static Stream<String> getSetsRNames(List<NamedRSet<String>> sets) {
		return sets.stream().map(NamedRSet::getRSetName);
	}
	
	private static List<String> getSetsRColors(List<NamedRSet<String>> sets) {
		return 	sets.stream()
				.map(NamedRSet::getRSetColor)
				.collect(Collectors.toList());
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