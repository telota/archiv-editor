package net.sf.vex.editor.ae;

import net.sf.vex.editor.config.Style;
import net.sf.vex.swt.VexWidget;

public interface IAEVexDialog
{
	VexWidget getVexWidget();

	void setStyle(Style style);

	Style getStyle();
}
