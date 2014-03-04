package org.bbaw.pdr.ae.export.logic;

import java.util.Comparator;

import org.bbaw.pdr.ae.model.Aspect;

public class NodeComparator implements Comparator<StructNode> {

	private Comparator<Aspect> acomp;
	
	public NodeComparator(Comparator<Aspect> comp) {
		this.acomp = comp;
	}
	
	@Override
	public int compare(StructNode obj1, StructNode obj2) {
		// use aspect comp for aspect nodes
		if (obj1.getContent() instanceof Aspect && obj2.getContent() instanceof Aspect)
			return acomp.compare((Aspect)obj1.getContent(), (Aspect)obj2.getContent());
		return obj1.getLabel().compareTo(obj2.getLabel());
	}

}
