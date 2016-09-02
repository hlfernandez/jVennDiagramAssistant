package es.uvigo.ei.sing.vda.core.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import es.uvigo.ei.sing.vda.core.VennDiagramDesign;

public class SerializationVennDiagramDesignWriter implements VennDiagramDesignWriter {

	@Override
	public void write(VennDiagramDesign design, File file) throws IOException {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
			oos.writeObject(design);
		}
	}

}
