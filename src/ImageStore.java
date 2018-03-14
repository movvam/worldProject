import java.util.*;

import processing.core.PImage;

final class ImageStore
{
   private Map<String, List<PImage>> images;
   private List<PImage> defaultImages;


   public ImageStore(PImage defaultImage)
   {
      this.images = new HashMap<>();
      defaultImages = new LinkedList<>();
      defaultImages.add(defaultImage);
   }

   protected List<PImage> getImageList( String key)
   {
      return images.getOrDefault(key, defaultImages);
   }

   protected Map<String, List<PImage>> getImages(){return images;}

}
