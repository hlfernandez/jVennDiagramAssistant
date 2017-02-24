package es.uvigo.ei.sing.vda.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import es.uvigo.ei.sing.vda.core.entities.NamedRSet;

public class RLibraryVennDiagramCreator implements VennDiagramCreator {

	private static final Collector<CharSequence, ?, String> JOINING = 
		Collectors.joining(", ");
	
	private static final Map<String, String> DEFAULT_PARAMETERS;

	static {
		DEFAULT_PARAMETERS = new HashMap<>();
		DEFAULT_PARAMETERS.put(OUTPUT_FILE, "venn-diagram.tiff");
	}

	@Override
	public String getRCode(List<NamedRSet<String>> sets) {
		return getRCode(sets, DEFAULT_PARAMETERS);
	}

	@Override
	public String getRCode(List<NamedRSet<String>> sets,
		Map<String, String> parameters
	) {
		StringBuilder sb = new StringBuilder();
		sets.forEach(sI -> sb.append(sI.getRCode() + "\n"));
		sb
			.append("\nlibrary(VennDiagram)\n\n")
			.append("filename <- \"")
			.append(parameters.get(OUTPUT_FILE))
			.append("\"\n\n")
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