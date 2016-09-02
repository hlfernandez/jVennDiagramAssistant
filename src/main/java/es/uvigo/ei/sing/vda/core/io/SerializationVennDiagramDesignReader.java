package es.uvigo.ei.sing.vda.core.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import es.uvigo.ei.sing.vda.core.VennDiagramDesign;

public class SerializationVennDiagramDesignReader implements VennDiagramDesignReader {

	@Override
	public VennDiagramDesign read(File file) throws IOException {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
			return (VennDiagramDesign) ois.readObject();
		} catch (ClassNotFoundException e) {
			throw new IOException("Unknown LaImagesAnalysis class", e);
		}
	}
	
}
