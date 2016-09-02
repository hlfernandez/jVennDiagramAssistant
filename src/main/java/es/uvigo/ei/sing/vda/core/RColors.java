package es.uvigo.ei.sing.vda.core;

import java.util.Arrays;
import java.util.stream.Collectors;

public class RColors {
	public static final String[] R_COLORS = new String[]{
			"red",
			"blue",
			"green",
			"orange",
			"yellow",
			"gray",
			"yellowgreen",
			"violetred3",
			"brown"
	};

	public static String colorList(int size) {
		return 	Arrays.asList(R_COLORS).subList(0, size)
				.stream().collect(Collectors.joining("\", \"", "c(\"", "\")"));
	}
}