package es.uvigo.ei.sing.vda.core;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import es.uvigo.ei.sing.vda.core.entities.NamedRSet;

public class DefaultVennDiagramCreator implements VennDiagramCreator {

	private static final Collector<CharSequence, ?, String> JOINING = 
		Collectors.joining(", ");

	@Override
	public String getRCode(List<NamedRSet<String>> sets) {
		StringBuilder sb = new StringBuilder();
		sets.forEach(sI -> sb.append(sI.getRCode() + "\n"));
		sb
			.append("\nlibrary(gplots)\n\n")
			.append("venn(list(")
			.append(getSetRNames(sets).map(s -> s.concat("=" + s)).collect(JOINING))
			.append("))\n");
		return sb.toString();
	}

	private Stream<String> getSetRNames(List<NamedRSet<String>> sets) {
		return sets.stream().map(NamedRSet::getRSetName);
	}

	@Override
	public String getDescription() {
		return "gplots";
	}

	@Override
	public String toString() {
		return getDescription();
	}
}
