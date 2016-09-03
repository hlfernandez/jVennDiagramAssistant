package es.uvigo.ei.sing.vda.core;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RColors {
	
	public static String colorList(int size) {
		return 	colorList(Arrays.asList(R_COLORS).subList(0, size));
	}
	
	public static String colorList(List<String> colors) {
		return colors.stream().collect(Collectors.joining("\", \"", "c(\"", "\")"));
	}
	
	public static final String[] R_COLORS = new String[]{
			"red",
			"blue",
			"green",
			"orange",
			"yellow",
			"brown",
			"turquoise",
			"orangered",
			"lavenderblush",
			"lightslateblue",
			"gold4",
			"forestgreen",
			"plum",
			"gray",
			"darkgray",
			"brown"
	};
}