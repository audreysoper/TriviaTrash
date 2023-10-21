package gui.qGang;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import gui.Board;
import gui.previews.PicPreview;
import orgObjects.Question;

public class QMedia extends Qbox {

	
	private final int xWarningLevel = 1920;
	private final int yWarningLevel = 1080;
	private String fileName;
	private String relativePath;
	private String fullPath;
	private String pathToHome;
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
		try{
			fullPath=q.getQuestion();
		}catch(Exception e) {
			fullPath="";
		}
		grandparent = (Board) parent.getParent().getParent().getParent();
		chooser = new FileDialog(parent.getShell());

		if (q.getFormat().contains("P")) {
			fileExtension = new String[] {"*.jpg;*.png;*.gif;*.jfif;*.bmp;*.jpeg;*.tiff"};
			openButton.setText("Select Picture");
			viewButton.setImage(SWTResourceManager.getImage(Qbox.class, "Zoom16.gif"));
		
		} else if (q.getFormat().contains("S")) {
			fileExtension = new String[] {"*.mp3"};
			openButton.setText("Select Audio");
			viewButton.setImage(SWTResourceManager.getImage(Qbox.class, "Volume16.gif"));
		} else {
			fileExtension = new String[] {"*.*"};
			System.out.println(
					"Something is not right, I'm rending a QMedia for a Q with type details: " + q.getFormat());
		}
		chooser.setFilterExtensions(fileExtension);
		chooser.setText(openButton.getText());
		qDD.setGrayed(false);

		try{
			if(fullPath.length()>0) {
				setRelativePath(grandparent.homeFolder);
				
			}
			
		}catch(Exception e) {
			System.out.println("Can't grab Relatve Path from "+fullPath);
		}

		openButton.setVisible(true);
		viewButton.setVisible(true);
		((GridData) openButton.getLayoutData()).exclude = false;
		((GridData) viewButton.getLayoutData()).exclude=false;

		//GridData openLayoutDetails = new GridData(GridData.FILL, GridData.FILL, true, false, 3, 1);
		//openLayoutDetails.widthHint = this.width;
		//openButton.setLayoutData(openLayoutDetails);
		
		qEdit.setEditable(true);
		GridData qEditLayoutDetails = new GridData(GridData.FILL, GridData.FILL, true, false, 3, 1);
		qEditLayoutDetails.heightHint = qEdit.getBounds().height - (openButton.getBounds().height + vSpace * 2);
		qEditLayoutDetails.widthHint = width;
		qEdit.setLayoutData(qEditLayoutDetails);
		
		qEdit.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if(grandparent.useRelativePaths) {
					relativePath=((Text) e.widget).getText();
					fullPath=pathToHome+relativePath;
				}else {
					fullPath=((Text) e.widget).getText();
					setRelativePath(grandparent.homeFolder);
				}
				
			}
			
		});
		//this.layout(true);
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
						else if(q.getFormat().contains("P")){
							changeFileType();
							checkPictureSize();
						}
						
						if(grandparent.useRelativePaths) {
							viewRelativePath();
						}else {
							setRelativePath(grandparent.homeFolder);
							qEdit.setText(fullPath);
						}
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
		
		
		viewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				File test= new File(fullPath);
				if(test.exists() && q.getFormat().contains("P")) {
					PicPreview pre=new PicPreview(fullPath);
				}else {
				MessageBox ope = new MessageBox(((Control) e.widget).getShell(), SWT.OK);
					if(q.getFormat().contains("S")) {
						ope.setMessage("Sorry, audio previews don't exist yet");
						ope.setText("No Audio Previews Yet");
					}
					else {
						ope.setMessage("I can't seem to find a file at "+fullPath);
						ope.setText("Can't Find File!");
					}
				ope.open();
				
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
	
	public void viewRelativePath() {
		setRelativePath(grandparent.homeFolder);
		qEdit.setText(relativePath);
		
	}
	
	public QMedia setRelativePath(String homeFolder) {
		if(fullPath.length()<1 || !fullPath.contains("\\"))return null;
		int index=fullPath.lastIndexOf(homeFolder);
		try {
			if(index>0)relativePath=fullPath.substring(index);
			//relativePath=initial.substring(index);
			else {
				index=fullPath.lastIndexOf("\\");
				pathToHome=fullPath.substring(0,index);
				relativePath=fullPath.substring(index);
			}
		}catch(Exception e) {
			relativePath="";
			System.out.println("Looks like the home folder wasn't in: "+fullPath);
			return this;
		}
		index=fullPath.indexOf(relativePath);
		if(index>0)pathToHome=fullPath.substring(0,index);
		
		return null;
	}
	
	public void swapPathFront(String pathToHome) {
		fullPath=pathToHome+relativePath;
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	@Override
	public String getText() {
		return fullPath;
	}



	public void viewFullPath() {
		qEdit.setText(fullPath);
		
	}



	

}
