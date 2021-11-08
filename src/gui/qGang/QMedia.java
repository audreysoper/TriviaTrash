package gui.qGang;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;

import gui.Board;
import orgObjects.Question;

public class QMedia extends Qbox {

	private final int xWarningLevel = 1920;
	private final int yWarningLevel = 1080;
	private String fileName;
	private String fullPath;
	protected FileDialog chooser;
	protected String[] fileExtension;
	private Board grandparent;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public QMedia(Composite parent, int style, Question q) {
		super(parent, style, q);
		Board grandparent = (Board) parent.getParent().getParent().getParent();
		chooser = new FileDialog(parent.getShell());

		if (q.getTypeDetails().contains("P")) {
			fileExtension = new String[] {"*.jpg;*.png;*.gif;*.jfif;*.bmp;*.jpeg;*.tiff"};
		} else if (q.getTypeDetails().contains("S")) {
			fileExtension = new String[] {"*.mp3"};
		} else {
			fileExtension = new String[] {"*.*"};
			System.out.println(
					"Something is not right, I'm rending a QMedia for a Q with type details: " + q.getTypeDetails());
		}
		chooser.setFilterExtensions(fileExtension);

		qDD.setGrayed(false);

		

		openButton.setVisible(true);
		((GridData) openButton.getLayoutData()).exclude = false;
		openButton.setText("Select Media File");

		GridData openLayoutDetails = new GridData(GridData.FILL, GridData.FILL, true, false, 3, 1);
		openLayoutDetails.widthHint = this.width;
		openButton.setLayoutData(openLayoutDetails);
		this.layout();
		qEdit.setEditable(true);
		GridData qEditLayoutDetails = new GridData(GridData.FILL, GridData.FILL, true, false, 3, 1);
		qEditLayoutDetails.heightHint = qEdit.getBounds().height - (openButton.getBounds().height + vSpace * 2);
		qEditLayoutDetails.widthHint = width;
		qEdit.setLayoutData(qEditLayoutDetails);

		this.layout(true);
		this.pack();
		
		openButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				try {
					chooser.open();
					if (chooser.getFileName().length() > 1) {
						fileName = chooser.getFileName().substring(0, (chooser.getFileName().lastIndexOf('.')));
						fullPath = chooser.getFilterPath() + "\\" + chooser.getFileName();
						
						
						if (fullPath.contains("jpg")) {
							checkForJPGTransparency();
							checkPictureSize();
						}
						else if(q.getTypeDetails().contains("P")){
							changeFileType();
							checkPictureSize();
						}
						
						qEdit.setText(fullPath);
						if (grandparent.useFileNames) {
							setAnswerToFileName();
						}
					}

				} catch (Exception err) {
					// I don't think I actually want to do anything...just like...exist
					err.printStackTrace();
					System.out.print("Ope that didn't work");
				}

			}
		});

	}

	

	public void setAnswerToFileName() {
		if (fileName != null) {
			qAnswer.changeQ(fileName);
		} else if (qEdit.getText().contains(fileExtension[0])) {
			int lastIndex = qEdit.getText().lastIndexOf('.') - 1;
			int firstIndex = qEdit.getText().lastIndexOf('\\') + 1;
			qAnswer.changeQ(qEdit.getText(firstIndex, lastIndex));
		}
	}

	public void checkPictureSize() {
		ImageData img = new ImageData(fullPath);
		if (img.width > xWarningLevel || img.height > yWarningLevel) {
			int newWidth = ((80000 / img.width) * img.width)/100;
			int newHeight = ((80000 / img.width) * img.height)/100;
			MessageBox sizeWarning = new MessageBox(getShell(), SWT.ICON_WARNING | SWT.YES | SWT.NO);
			sizeWarning.setMessage("Your image looks a little large, specifically: " + img.width + "x" + img.height
					+ " pixels, which might be hard to load. Would you like to reduce it's size to: " + newWidth + "x"
					+ newHeight + "?");
			switch(sizeWarning.open()) {
			case SWT.YES:
				String newfullPath=fullPath.substring(0,fullPath.length()-4).concat("SMALL.jpg");
				ImageData smallScale=img.scaledTo(newWidth,newHeight);
				ImageLoader imageLoader = new ImageLoader();
				imageLoader.data=new ImageData[] {smallScale};
				imageLoader.save(newfullPath,SWT.IMAGE_JPEG);
				fullPath=newfullPath;
				break;
			case SWT.NO:
				System.out.println("they hit NO!");
				break;
			}
		}

	}
	
	
	protected void changeFileType() {
		ImageData img = new ImageData(fullPath);
		MessageBox typeWarning = new MessageBox(getShell(), SWT.ICON_WARNING | SWT.YES | SWT.NO);
		typeWarning.setMessage("Your image doesn't appear to be a JPG , and thus won't be accepted. Would you like to create a JPG copy of your image to add?");
		switch(typeWarning.open()) {
		case SWT.YES:
			String newfullPath=fullPath.substring(0,fullPath.length()-4).concat("JPG.jpg");
			if(img.getTransparencyType() == SWT.TRANSPARENCY_NONE) {
				ImageLoader imageLoader = new ImageLoader();
				imageLoader.data=new ImageData[] {img};
				imageLoader.save(newfullPath,SWT.IMAGE_JPEG);
				fullPath=newfullPath;
			}else {
				removeTransparency();
			}
			
			break;
		case SWT.NO:
			System.out.println("they hit NO!");
			break;
		}
	}
	

	private void checkForJPGTransparency() {
		ImageData img = new ImageData(fullPath);
		if(img.getTransparencyType() != SWT.TRANSPARENCY_NONE){
			MessageBox typeWarning = new MessageBox(getShell(), SWT.ICON_WARNING | SWT.YES | SWT.NO);
			typeWarning.setMessage(
					"Your image has some kind of transparency which will break the trivia program, would you like to create a JPG copy without the transparency to add?");
			switch (typeWarning.open()) {
			case SWT.YES:
				removeTransparency();
				break;
			case SWT.NO:
				break;
			}
	}}
	
	
	
	private void removeTransparency() {
		

			String newfullPath = fullPath.substring(0, fullPath.length() - 4).concat("JPG.jpg");
			BufferedImage img;
			try {
				img = ImageIO.read(new File(fullPath));
				BufferedImage copy = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
				Graphics2D g2d = copy.createGraphics();
				g2d.setColor(Color.WHITE); // Or what ever fill color you want...
				g2d.fillRect(0, 0, copy.getWidth(), copy.getHeight());
				g2d.drawImage(img, 0, 0, null);
				g2d.dispose();
				ImageIO.write(copy, "jpg", new File(newfullPath));
				fullPath=newfullPath;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		

	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
