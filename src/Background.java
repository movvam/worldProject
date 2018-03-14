import java.util.List;
import processing.core.PImage;

 class Background
{
   private String id;
   private List<PImage> images;
   private int imageIndex = 0;

   public Background(String id, List<PImage> images)
   {
      this.id = id;
      this.images = images;
   }

   protected PImage getCurrentImage()
   {
      return images.get(imageIndex);
   }
   protected String getId(){return id;}
}
