package utils;

import javafx.scene.image.Image;

public class ImageUtil {

    /**
     * Safely loads an image from resources. If not found, returns a fallback image.
     * @param path path to the image (e.g. "/icons/floppydisk.jpg")
     * @return Image object, or fallback placeholder if image not found
     */
    public static Image loadImage(String path) {
        try {
            return new Image(ImageUtil.class.getResourceAsStream(path));
        } catch (Exception e) {
            System.err.println("⚠ Image not found at: " + path + " — using fallback.");
            // Return a blank 1x1 transparent pixel or you can provide a fallback image
            return new Image("https://via.placeholder.com/20");  // online fallback
            // OR
            // return new Image(ImageUtil.class.getResourceAsStream("/icons/placeholder.jpg"));
        }
    }
}
