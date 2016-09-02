package es.uvigo.ei.sing.vda.core.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class NamedRSet<E> extends HashSet<E> implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private static final Collector<CharSequence, ?, String> JOINING = 
		Collectors.joining("\",\"", "\"", "\"");
	
	private String name;
	private String color;
	
	public NamedRSet(String name, String color) {
		this.name = name;
		this.color = color;
	}

	public String getName() {
		return name;
	}
	
	public String getRCode() {
		String join = this.stream()
			.map(Object::toString)
			.collect(JOINING);

		StringBuilder rCode = new StringBuilder();
		rCode
			.append(getRSetName() + " <- c(")
			.append(join)
			.append(")");
		
		return rCode.toString();
	}

	public String getRSetName() {
		return this.name.replaceAll(" ", ".");
	}
	
	public String getRSetColor() {
		return this.color;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if(!(o instanceof NamedRSet)) {
			return false;
		}
		NamedRSet<?> other = ((NamedRSet<?>) o);
		return 
				this.name.equals(other.name) &&
				this.color.equals(other.color) &&
				super.equals(o);
	}
}
