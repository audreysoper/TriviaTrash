package gui.previews;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import gui.AppBoard;

public class PicPreview {

	public PicPreview(String filePath) {
		// TODO Auto-generated constructor stub
		Shell boardWin=Display.getCurrent().getActiveShell();
		Shell prev=new Shell();
		prev.open();
		ImageData imgData=new ImageData(filePath);
		
		int newWidth = ((40000 / imgData.height) * imgData.width)/100;
		int newHeight = ((40000 / imgData.height) * imgData.height)/100;
		
		Image scaled=new Image(Display.getCurrent(),imgData.scaledTo(newWidth, newHeight));
		prev.setBackgroundImage(scaled);
		
		prev.setBounds(prev.computeTrim(boardWin.getBounds().width/2,boardWin.getBounds().height/2,scaled.getBounds().width,scaled.getBounds().height));
	}

}
