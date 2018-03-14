import java.util.List;


import processing.core.PImage;

abstract public class Entity
{
    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int resourceLimit;
    private int resourceCount;
    // private int actionPeriod;       ---   This is found in Activity Entity
    // private int animationPeriod;    ---   This is found in Animated Entity

    public Entity(String id, Point position,
                          List<PImage> images, int resourceLimit,
                          int resourceCount)
    {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;

    }

    protected Point getposition() { return position; }
    protected void setposition(Point point) { this.position = point; }
    protected List<PImage> getimages() { return images; }
    protected String getid() { return id; }
    protected int getImageIndex() { return imageIndex; }

    protected void setImageIndex(int integer) { imageIndex = integer; }
    protected int getresourceCount() { return resourceCount; }
    protected int getresourceLimit() { return resourceLimit; }
    protected void setResourceCount(int resourceCount){ this.resourceCount = resourceCount;}

}




