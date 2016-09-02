package es.uvigo.ei.sing.vda.core.io;

import java.io.File;
import java.io.IOException;

import es.uvigo.ei.sing.vda.core.VennDiagramDesign;


public interface VennDiagramDesignWriter {
	public void write(VennDiagramDesign design, File file)
	throws IOException;
}
