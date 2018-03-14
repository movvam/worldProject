import java.util.List;
import processing.core.PImage;

public class Blacksmith extends Entity{

    public Blacksmith( String id, Point position,
                 List<PImage> images, int resourceLimit, int resourceCount)
    {
        super(id, position, images, resourceLimit, resourceCount);
    }

}
