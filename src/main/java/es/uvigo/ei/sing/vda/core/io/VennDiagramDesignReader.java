package es.uvigo.ei.sing.vda.core.io;

import java.io.File;
import java.io.IOException;

import es.uvigo.ei.sing.vda.core.VennDiagramDesign;


public interface VennDiagramDesignReader {
	public VennDiagramDesign read(File file) throws IOException;
}
