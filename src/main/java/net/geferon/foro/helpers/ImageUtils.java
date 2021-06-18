package net.geferon.foro.helpers;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.IIOException;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import lombok.AllArgsConstructor;
import lombok.Data;

public class ImageUtils {
	@AllArgsConstructor
	@Data
	public static class ResultImage {
		private String format;
		//private RenderedImage image;
		private ByteArrayOutputStream os;
	}

	public static ResultImage squareImage(InputStream input) throws IOException {
		String formatName = null;
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		
		ImageInputStream imageStream = ImageIO.createImageInputStream(input);
		Iterator<ImageReader> readers = ImageIO.getImageReaders(imageStream);
		ImageReader reader = null;
		if (!readers.hasNext()) {
			imageStream.close();
			throw new RuntimeException("Esta imagen no es valida.");
		}
		reader = readers.next();

		formatName = reader.getFormatName();
		//reader.setInput(imageStream, true, true);
		
		if (formatName == "gif") {
			List<BufferedImage> images = new ArrayList<>(25);
	        List<Integer> delays = new ArrayList<>(25);
	        int delay = 0;

			String[] imageatt = new String[]{
	                "imageLeftPosition",
	                "imageTopPosition",
	                "imageWidth",
	                "imageHeight"
	            };
			
			reader.setInput(imageStream);
			int size = reader.getNumImages(true);
			BufferedImage master = null;
			
			for (int i = 0; i < size; i++) {
				BufferedImage image = reader.read(i);
				IIOMetadata metadata = reader.getImageMetadata(i);
				
				Node tree = metadata.getAsTree("javax_imageio_gif_image_1.0");
                NodeList children = tree.getChildNodes();
                for (int j = 0; j < children.getLength(); j++) {
                	Node nodeItem = children.item(j);
                    if (nodeItem.getNodeName().equals("ImageDescriptor")) {
                        Map<String, Integer> imageAttr = new HashMap<String, Integer>();
                        NamedNodeMap attr = nodeItem.getAttributes();
                        
                        for (int k = 0; k < imageatt.length; k++) {
                            Node attnode = attr.getNamedItem(imageatt[k]);
                            imageAttr.put(imageatt[k], Integer.valueOf(attnode.getNodeValue()));
                        }

                        if (master == null) {
                            master = new BufferedImage(imageAttr.get("imageWidth"), imageAttr.get("imageHeight"), BufferedImage.TYPE_INT_ARGB);
                        }

                        Graphics2D g2d = master.createGraphics();
                        g2d.drawImage(image, imageAttr.get("imageLeftPosition"), imageAttr.get("imageTopPosition"), null);
                        g2d.dispose();

                        BufferedImage frame = copyImage(master);
                        images.add(frame);

                    } else if (nodeItem.getNodeName().equals("GraphicControlExtension")) {
                        NamedNodeMap attr = nodeItem.getAttributes();
                        Node delayNode = attr.getNamedItem("delayTime");
                        if (delayNode != null) {
                            delay = Math.max(delay, Integer.valueOf(delayNode.getNodeValue()));
                            delays.add(delay);
                        }
                    }
                }
			}
			reader.dispose();
			imageStream.close();
			
			ImageOutputStream ios = ImageIO.createImageOutputStream(os);
			GifSequenceWriter writer = new GifSequenceWriter(ios, images.get(0).getType(), delay * 10, true);

            for (int i = 0; i < images.size(); i++) {
                BufferedImage nextImage = images.get(i);
                writer.writeToSequence(nextImage);
            }
            ios.flush();
            ios.close();
		} else {
			reader.setInput(imageStream, true, true);

			BufferedImage foto = reader.read(0);
			reader.dispose();
			imageStream.close();
			
			if (foto == null) {
				throw new RuntimeException("Esta imagen no es valida.");
			}
	
			int size = Math.min(foto.getWidth(), foto.getHeight());
			if (foto.getWidth() != foto.getHeight()) {
				foto = foto.getSubimage(
						(foto.getWidth() - size) / 2,
						(foto.getHeight() - size) / 2,
						size, size
					);
			}
			
			ImageIO.write(foto, formatName, os);
		}
		
		return new ResultImage(formatName, os); 
	}
	
	public static BufferedImage copyImage(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();

        BufferedImage newImage = createCompatibleImage(img);
        Graphics graphics = newImage.createGraphics();

        int x = (width - img.getWidth()) / 2;
        int y = (height - img.getHeight()) / 2;

        graphics.drawImage(img, x, y, img.getWidth(), img.getHeight(), null);
        graphics.dispose();

        return newImage;
    }
	
	public static BufferedImage createCompatibleImage(BufferedImage image) {
		return new BufferedImage(image.getWidth(), image.getHeight(), image.getTransparency());
    }
	
	
	
	/**
	 * This class will generate an animated GIF from a sequence of individual images. Embedded in Wayang
	 * to facilitate capturing animations and interfaces rendered on the Push, for producing great online
	 * documentation.
	 *
	 * Originally created by Elliot Kroo on 2009-04-25. See http://elliot.kroo.net/software/java/GifSequenceWriter/
	 * James Elliott split the constructor into a variety of different versions, to accommodate the needs of Wayang.
	 *
	 * This work is licensed under the Creative Commons Attribution 3.0 Unported License.
	 * To view a copy of this license, visit http://creativecommons.org/licenses/by/3.0/
	 * or send a letter to Creative Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
	 *
	 * @author Elliot Kroo (elliot[at]kroo[dot]net)
	 */
	public static class GifSequenceWriter {
	    protected ImageWriter gifWriter;
	    protected ImageWriteParam imageWriteParam;
	    protected IIOMetadata imageMetaData;


	    /**
	     * Creates a new GifSequenceWriter from an existing buffered image.
	     *
	     * @param outputStream the ImageOutputStream to be written to
	     * @param image the source image that will be written to the output
	     * @param timeBetweenFramesMS the time between frames in miliseconds
	     * @param loopContinuously wether the gif should loop repeatedly
	     * @throws IIOException if no gif ImageWriters are found
	     *
	     * @author James Elliott
	     */
	    public GifSequenceWriter(
	            ImageOutputStream outputStream,
	            RenderedImage image,
	            int timeBetweenFramesMS,
	            boolean loopContinuously) throws IIOException, IOException {
	        this(outputStream, ImageTypeSpecifier.createFromRenderedImage(image), timeBetweenFramesMS,
	                loopContinuously);
	    }

	    /**
	     * Creates a new GifSequenceWriter
	     *
	     * @param outputStream the ImageOutputStream to be written to
	     * @param imageType one of the imageTypes specified in BufferedImage
	     * @param timeBetweenFramesMS the time between frames in miliseconds
	     * @param loopContinuously wether the gif should loop repeatedly
	     * @throws IIOException if no gif ImageWriters are found
	     *
	     * @author Elliot Kroo (elliot[at]kroo[dot]net)
	     */
	    public GifSequenceWriter(
	            ImageOutputStream outputStream,
	            int imageType,
	            int timeBetweenFramesMS,
	            boolean loopContinuously) throws IIOException, IOException {
	        this(outputStream, ImageTypeSpecifier.createFromBufferedImageType(imageType), timeBetweenFramesMS,
	                loopContinuously);

	    }

	    /**
	     * Creates a new GifSequenceWriter
	     *
	     * @param outputStream the ImageOutputStream to be written to
	     * @param imageTypeSpecifier the type of images to be written
	     * @param timeBetweenFramesMS the time between frames in miliseconds
	     * @param loopContinuously wether the gif should loop repeatedly
	     * @throws IIOException if no gif ImageWriters are found
	     *
	     * @author Elliot Kroo (elliot[at]kroo[dot]net)
	     */
	    public GifSequenceWriter(
	            ImageOutputStream outputStream,
	            ImageTypeSpecifier imageTypeSpecifier,
	            int timeBetweenFramesMS,
	            boolean loopContinuously) throws IIOException, IOException {

	        // my method to create a writer
	        gifWriter = getWriter();
	        imageWriteParam = gifWriter.getDefaultWriteParam();

	        imageMetaData =
	                gifWriter.getDefaultImageMetadata(imageTypeSpecifier,
	                        imageWriteParam);

	        String metaFormatName = imageMetaData.getNativeMetadataFormatName();

	        IIOMetadataNode root = (IIOMetadataNode)
	                imageMetaData.getAsTree(metaFormatName);

	        IIOMetadataNode graphicsControlExtensionNode = getNode(
	                root,
	                "GraphicControlExtension");

	        graphicsControlExtensionNode.setAttribute("disposalMethod", "none");
	        graphicsControlExtensionNode.setAttribute("userInputFlag", "FALSE");
	        graphicsControlExtensionNode.setAttribute(
	                "transparentColorFlag",
	                "FALSE");
	        graphicsControlExtensionNode.setAttribute(
	                "delayTime",
	                Integer.toString(timeBetweenFramesMS / 10));
	        graphicsControlExtensionNode.setAttribute(
	                "transparentColorIndex",
	                "0");

	        IIOMetadataNode commentsNode = getNode(root, "CommentExtensions");
	        commentsNode.setAttribute("CommentExtension", "Created by MAH");

	        IIOMetadataNode appEntensionsNode = getNode(
	                root,
	                "ApplicationExtensions");

	        IIOMetadataNode child = new IIOMetadataNode("ApplicationExtension");

	        child.setAttribute("applicationID", "NETSCAPE");
	        child.setAttribute("authenticationCode", "2.0");

	        int loop = loopContinuously ? 0 : 1;

	        child.setUserObject(new byte[]{ 0x1, (byte) (loop & 0xFF), (byte)
	                ((loop >> 8) & 0xFF)});
	        appEntensionsNode.appendChild(child);

	        imageMetaData.setFromTree(metaFormatName, root);

	        gifWriter.setOutput(outputStream);

	        gifWriter.prepareWriteSequence(null);
	    }

	    public void writeToSequence(RenderedImage img) throws IOException {
	        gifWriter.writeToSequence(
	                new IIOImage(
	                        img,
	                        null,
	                        imageMetaData),
	                imageWriteParam);
	    }

	    /**
	     * Close this GifSequenceWriter object. This does not close the underlying
	     * stream, just finishes off the GIF.
	     *
	     * @throws IOException if there is a problem writing the last bytes.
	     */
	    public void close() throws IOException {
	        gifWriter.endWriteSequence();
	    }

	    /**
	     * Returns the first available GIF ImageWriter using
	     * ImageIO.getImageWritersBySuffix("gif").
	     *
	     * @return a GIF ImageWriter object
	     * @throws IIOException if no GIF image writers are returned
	     */
	    private static ImageWriter getWriter() throws IIOException {
	        Iterator<ImageWriter> iter = ImageIO.getImageWritersBySuffix("gif");
	        if(!iter.hasNext()) {
	            throw new IIOException("No GIF Image Writers Exist");
	        } else {
	            return iter.next();
	        }
	    }

	    /**
	     * Returns an existing child node, or creates and returns a new child node (if
	     * the requested node does not exist).
	     *
	     * @param rootNode the <tt>IIOMetadataNode</tt> to search for the child node.
	     * @param nodeName the name of the child node.
	     *
	     * @return the child node, if found or a new node created with the given name.
	     */
	    private static IIOMetadataNode getNode(
	            IIOMetadataNode rootNode,
	            String nodeName) {
	        int nNodes = rootNode.getLength();
	        for (int i = 0; i < nNodes; i++) {
	            if (rootNode.item(i).getNodeName().compareToIgnoreCase(nodeName)
	                    == 0) {
	                return((IIOMetadataNode) rootNode.item(i));
	            }
	        }
	        IIOMetadataNode node = new IIOMetadataNode(nodeName);
	        rootNode.appendChild(node);
	        return(node);
	    }
	}
}
