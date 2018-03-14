import processing.core.PImage;
import java.util.List;

abstract public class AnimationEntity extends ActivityEntity{

    private int repeatCount;
    private int animationPeriod;

    public AnimationEntity(String id, Point position,
                             List<PImage> images, int resourceLimit,
                             int resourceCount, int actionPeriod,
                             int animationPeriod, int repeatCount)
    {
        super(id, position, images, resourceLimit, resourceCount, actionPeriod);
        this.repeatCount = repeatCount; //New variable to take in for schedule Actions.
        this.animationPeriod = animationPeriod;
    }
    public int getanimationPeriod() { return animationPeriod; }

    public void nextImage() { setImageIndex((getImageIndex() + 1) % getimages().size()); }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent( this,
                Activity.createActivityAction(this, world, imageStore),
                this.getactionPeriod());
            scheduler.scheduleEvent( this,
                    Animation.createAnimationAction(this, 0), this.getanimationPeriod());
    }
}
