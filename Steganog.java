import java.awt.Color;
import java.io.IOException;

import edu.neumont.ui.Picture;


public class Steganog {
	
	public Picture embedIntoImage(Picture cleanImage, String message) throws IOException
	{
		long startTime = System.currentTimeMillis();
		message = message.toUpperCase();
		int mask = 0x03;
		PrimeIterator iterator = new PrimeIterator(message.length()*20);
		Picture returnedPic = cleanImage;
		for(int i=0; i<message.length(); i++)
		{
			char letter = message.charAt(i);
			int asciiValue = letter - 32;
			int pixel = iterator.next();
			int height = pixel/cleanImage.width();
			int width = pixel-height*cleanImage.width();
			
			try 
			{
				Color c = cleanImage.get(width, height);
				int subtractedRed = getNewColorValue(c.getRed(), mask);
				int subtractedGreen = getNewColorValue(c.getGreen(), mask);
				int subtractedBlue = getNewColorValue(c.getBlue(), mask);
				int redValue = asciiValue/16;
				asciiValue -= 16*redValue;
				int greenValue = asciiValue/4;
				asciiValue -= 4*greenValue;
				int blueValue = asciiValue;
				Color embededColor = new Color(subtractedRed+redValue, subtractedGreen+greenValue, subtractedBlue+blueValue);
				returnedPic.set(width, height, embededColor);
			}
			catch(Exception e)
			{
				System.out.println("The message doesn't fit in the image");
				System.exit(0);
			}
			
		}
		returnedPic.save("OrangeFishMessage.png");
		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime-startTime;
		System.out.println(elapsedTime);
		return returnedPic;
	}
	
	public String retrieveFromImage(Picture imageWithSecretMessage) throws IOException
	{
		long startTime = System.currentTimeMillis();
		PrimeIterator iterator = new PrimeIterator(imageWithSecretMessage.height()*imageWithSecretMessage.width()/2);
		String message = "";
		String escapes = "()][/^&*$#@%_";
		boolean escaped = false;
		int mask = 0x03;
		while(iterator.hasNext() && !escaped)
		{
			int value = iterator.next();
			int height = value/imageWithSecretMessage.width();
			int width = value-height*imageWithSecretMessage.width();
			Color test = imageWithSecretMessage.get(width, height);
			int redValue = (mask & test.getRed()) << 4;
			int greenValue = (mask & test.getGreen()) << 2;
			int blueValue = (mask & test.getBlue());
			int total = redValue + greenValue + blueValue;
			String character = "";
			character += (char)(total+32);
			escaped = (escapes.contains(character));
			message += (!escaped)?(char)(total+32):"";
		}
		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime-startTime;
		System.out.println(elapsedTime);
		System.out.println(message.length());
		return message;
	}
	
	private int getNewColorValue(int original, int mask)
	{
		int subtract = (mask & original);
		return original-subtract;
	}
}
