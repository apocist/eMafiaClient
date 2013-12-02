package com.inverseinnovations.eMafiaClient.classes.jobjects;

import java.awt.*;
//import java.awt.image.*;
import javax.swing.*;

/**
 *  Support custom painting on a panel in the form of
 *
 *  a) images - that can be scaled, tiled or painted at original size
 *  b) non solid painting - that can be done by using a Paint object
 *
 *  Also, any component added directly to this panel will be made
 *  non-opaque so that the custom painting can show through.
 */
public class BackgroundPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	public static final int SCALED = 0;
	public static final int TILED = 1;
	public static final int ACTUAL = 2;
	public static final int DETAIL_HOR = 3;
	public static final int DETAIL_VERT = 4;
	public static final int BORDER = 5;

	private Paint painter;
	private Image image;
	private Image imageL;
	private Image imageR;
	private Image imageTL;
	private Image imageTM;
	private Image imageTR;
	private Image imageBL;
	private Image imageBM;
	private Image imageBR;
	private int style = SCALED;
	private float alignmentX = 0.5f;
	private float alignmentY = 0.5f;
	private boolean isTransparentAdd = true;

	/**
	 *  Set image as the background with the SCALED style
	 */
	public BackgroundPanel(Image image)
	{
		this(image, SCALED);
	}

	/**
	 *  Set image as the background with the specified style
	 * @param image path to image pack
	 * @param style 0 SCALED 1 TILED 2 ACTUAL
	 */
	public BackgroundPanel(Image image, int style)
	{
		setImage( image );
		setStyle( style );
		setLayout( new BorderLayout() );
	}

	/** Set image as the background with the specified style
	 *  Used only with detail tiled panel
	 * @param image path to image pack
	 * @param style 0 SCALED 1 TILED 2 ACTUAL 3 Left/Right Borders 4 Up/Down Borders 5 Borders
	 */
	public BackgroundPanel(String image, int style){
		if(style <= ACTUAL){
			setImage( new ImageIcon(image+".png").getImage() );
		}
		if(style == DETAIL_HOR || style == DETAIL_VERT || style == BORDER){
			setImage( new ImageIcon(image+"M.png").getImage() );
			this.imageL = new ImageIcon(image+"L.png").getImage();
			this.imageR = new ImageIcon(image+"R.png").getImage();
		}
		if(style == BORDER){
			this.imageTL = new ImageIcon(image+"TL.png").getImage();
			this.imageTM = new ImageIcon(image+"TM.png").getImage();
			this.imageTR = new ImageIcon(image+"TR.png").getImage();
			this.imageBL = new ImageIcon(image+"BL.png").getImage();
			this.imageBM = new ImageIcon(image+"BM.png").getImage();
			this.imageBR = new ImageIcon(image+"BR.png").getImage();
		}
		//setImage( image );
		setStyle( style );
		setLayout( new BorderLayout() );
	}
	/**
	 *  Set image as the backround with the specified style and alignment
	 */
	public BackgroundPanel(Image image, int style, float alignmentX, float alignmentY)
	{
		setImage( image );
		setStyle( style );
		setImageAlignmentX( alignmentX );
		setImageAlignmentY( alignmentY );
		setLayout( new BorderLayout() );
	}

	/**
	 *  Use the Paint interface to paint a background
	 */
	public BackgroundPanel(Paint painter)
	{
		setPaint( painter );
		setLayout( new BorderLayout() );
	}

	/**
	 *	Set the image used as the background
	 */
	public void setImage(Image image)
	{
		this.image = image;
		repaint();
	}

	/**
	 *	Set the style used to paint the background image
	 */
	public void setStyle(int style)
	{
		this.style = style;
		repaint();
	}

	/**
	 *	Set the Paint object used to paint the background
	 */
	public void setPaint(Paint painter)
	{
		this.painter = painter;
		repaint();
	}

	/**
	 *  Specify the horizontal alignment of the image when using ACTUAL style
	 */
	public void setImageAlignmentX(float alignmentX)
	{
		this.alignmentX = alignmentX > 1.0f ? 1.0f : alignmentX < 0.0f ? 0.0f : alignmentX;
		repaint();
	}

	/**
	 *  Specify the horizontal alignment of the image when using ACTUAL style
	 */
	public void setImageAlignmentY(float alignmentY)
	{
		this.alignmentY = alignmentY > 1.0f ? 1.0f : alignmentY < 0.0f ? 0.0f : alignmentY;
		repaint();
	}

	/**
	 *  Override method so we can make the component transparent
	 */
	public void add(JComponent component)
	{
		add(component, null);
	}

	/**
	 *  Override to provide a preferred size equal to the image size
	 */
	@Override
	public Dimension getPreferredSize()
	{
		if (image == null)
			return super.getPreferredSize();
		else
			return new Dimension(image.getWidth(null), image.getHeight(null));
	}

	/**
	 *  Override method so we can make the component transparent
	 */
	public void add(JComponent component, Object constraints)
	{
		if (isTransparentAdd)
		{
			makeComponentTransparent(component);
		}

		super.add(component, constraints);
	}

	/**
	 *  Controls whether components added to this panel should automatically
	 *  be made transparent. That is, setOpaque(false) will be invoked.
	 *  The default is set to true.
	 */
	public void setTransparentAdd(boolean isTransparentAdd)
	{
		this.isTransparentAdd = isTransparentAdd;
	}

	/**
	 *	Try to make the component transparent.
	 *  For components that use renderers, like JTable, you will also need to
	 *  change the renderer to be transparent. An easy way to do this it to
	 *  set the background of the table to a Color using an alpha value of 0.
	 */
	private void makeComponentTransparent(JComponent component)
	{
		component.setOpaque( false );

		if (component instanceof JScrollPane)
		{
			JScrollPane scrollPane = (JScrollPane)component;
			JViewport viewport = scrollPane.getViewport();
			viewport.setOpaque( false );
			Component c = viewport.getView();

			if (c instanceof JComponent)
			{
				((JComponent)c).setOpaque( false );
			}
		}
	}

	/**
	 *  Add custom painting
	 */
	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		//  Invoke the painter for the background

		if (painter != null)
		{
			Dimension d = getSize();
			Graphics2D g2 = (Graphics2D) g;
			g2.setPaint(painter);
			g2.fill( new Rectangle(0, 0, d.width, d.height) );
		}

		//  Draw the image

		if (image == null ) return;

		switch (style)
		{
			case SCALED :
				drawScaled(g);
				break;

			case TILED  :
				drawTiled(g);
				break;

			case ACTUAL :
				drawActual(g);
				break;

			case DETAIL_HOR :
				drawDetailHor(g);
				break;

			case BORDER :
				drawBorder(g);
				break;

			default:
				drawScaled(g);
		}
	}

	/**
	 *  Custom painting code for drawing a SCALED image as the background
	 */
	private void drawScaled(Graphics g)
	{
		Dimension d = getSize();
		g.drawImage(image, 0, 0, d.width, d.height, null);
	}

	/**
	 *  Custom painting code for drawing TILED images as the background
	 */
	private void drawTiled(Graphics g)
	{
		Dimension d = getSize();
		int width = image.getWidth( null );
		int height = image.getHeight( null );

		for (int x = 0; x < d.width; x += width)
		{
			for (int y = 0; y < d.height; y += height)
			{
				g.drawImage( image, x, y, null, null );
			}
		}
	}

	/**
	 *  Custom painting code for drawing the ACTUAL image as the background.
	 *  The image is positioned in the panel based on the horizontal and
	 *  vertical alignments specified.
	 */
	private void drawActual(Graphics g)
	{
		Dimension d = getSize();
		Insets insets = getInsets();
		int width = d.width - insets.left - insets.right;
		int height = d.height - insets.top - insets.left;
		float x = (width - image.getWidth(null)) * alignmentX;
		float y = (height - image.getHeight(null)) * alignmentY;
		g.drawImage(image, (int)x + insets.left, (int)y + insets.top, this);
	}

	/**
	 *  Custom painting code for drawing Detailed tiled images as the background
	 */
	private void drawDetailHor(Graphics g)
	{
		Dimension d = getSize();
		int widthL = imageL.getWidth( null );
		//int heightL = imageL.getHeight( null );
		int widthR = imageR.getWidth( null );
		//int heightL = imageL.getHeight( null );
		int widthC = image.getWidth( null );
		int heightC = image.getHeight( null );

		for (int y = 0; y < d.height; y += heightC){
			g.drawImage( imageL, 0, y, null, null );
		}

		for (int x = widthL; x < d.width-widthR; x += widthC){
			for (int y = 0; y < d.height; y += heightC){
				g.drawImage( image, x, y, null, null );
			}
		}

		for (int y = 0; y < d.height; y += heightC){
			g.drawImage( imageR, d.width-widthR, y, null, null );
		}
	}

	private void drawBorder(Graphics g){//not complete
		Dimension d = getSize();
		int widthTL = imageTL.getWidth( null );int heightTL = imageTL.getHeight( null );
		int widthTM = imageTM.getWidth( null );int heightTM = imageTM.getHeight( null );
		int widthTR = imageTR.getWidth( null );int heightTR = imageTR.getHeight( null );
		int widthL = imageL.getWidth( null );int heightL = imageL.getHeight( null );
		int widthC = image.getWidth( null );int heightC = image.getHeight( null );
		int widthR = imageR.getWidth( null );int heightR = imageR.getHeight( null );
		int widthBL = imageBL.getWidth( null );int heightBL = imageBL.getHeight( null );
		int widthBM = imageBM.getWidth( null );int heightBM = imageBM.getHeight( null );
		int widthBR = imageBR.getWidth( null );int heightBR = imageBR.getHeight( null );


		//TOPLEFT
		g.drawImage( imageTL, 0, 0, null, null );
		//TOPMIDDLE
		for (int x = widthTL; x < d.width-widthTR; x += widthTM){
			g.drawImage( imageTM, x, 0, null, null );
		}
		//TOPRIGHT
		g.drawImage( imageTR, d.width-widthTR, 0, null, null );
		//LEFT
		for (int y = heightTL; y < d.height-heightBL; y += heightL){
			g.drawImage( imageL, 0, y, null, null );
		}
		//CENTER
		for (int x = widthL; x < d.width-widthR; x += widthC){
			for (int y = heightTM; y < d.height-heightBM; y += heightC){
				g.drawImage( image, x, y, null, null );
			}
		}
		//RIGHT
		for (int y = heightTR; y < d.height-heightBR; y += heightR){
			g.drawImage( imageR, d.width-widthR, y, null, null );
		}
		//BOTTOMLEFT
		g.drawImage( imageBL, 0, d.height-heightBL, null, null );
		//BOTTOMMIDDLE
		for (int x = widthBL; x < d.width-widthBR; x += widthBM){//stop here
			g.drawImage( imageBM, x, d.height-heightBM, null, null );
		}
		//BOTTOMRIGHT
		g.drawImage( imageBR, d.width-widthBR, d.height-heightBR, null, null );
	}
}
