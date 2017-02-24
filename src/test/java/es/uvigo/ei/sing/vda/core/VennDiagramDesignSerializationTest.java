package es.uvigo.ei.sing.vda.core;

import static es.uvigo.ei.sing.vda.core.RColors.R_COLORS;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import es.uvigo.ei.sing.vda.core.entities.NamedRSet;
import es.uvigo.ei.sing.vda.core.io.SerializationVennDiagramDesignReader;
import es.uvigo.ei.sing.vda.core.io.SerializationVennDiagramDesignWriter;
import es.uvigo.ei.sing.vda.core.io.VennDiagramDesignReader;
import es.uvigo.ei.sing.vda.core.io.VennDiagramDesignWriter;

public class VennDiagramDesignSerializationTest {

	private static final VennDiagramDesign VDD = new VennDiagramDesign(asList(
		new NamedRSet<String>("A", R_COLORS[0]))
	);
	
	static {
		VDD.getSets().get(0).add("Item 1");
		VDD.getSets().get(0).add("Item 2");
	}
	
	VennDiagramDesignWriter writer = new SerializationVennDiagramDesignWriter();
	VennDiagramDesignReader reader = new SerializationVennDiagramDesignReader();
	
	@Test
	public void testVennDiagramDesignSerialization() throws IOException {
		final File dest = File.createTempFile("venndiagramtest", "vda");
		dest.deleteOnExit();
		
		writer.write(VDD, dest);

		VennDiagramDesign readed = reader.read(dest);
		
		assertEquals(VDD, readed);
	}

}
