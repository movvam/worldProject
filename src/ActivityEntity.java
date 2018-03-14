import processing.core.PImage;
import java.util.List;
import java.util.Random;

abstract public class ActivityEntity extends Entity{
    public static final Random rand = new Random();
    private int actionPeriod;

    public ActivityEntity(String id, Point position,
                            List<PImage> images, int resourceLimit,
                            int resourceCount, int actionPeriod)
    {
        super(id, position, images, resourceLimit, resourceCount);
        this.actionPeriod = actionPeriod;
    }

    protected int getactionPeriod() { return actionPeriod; }


    public abstract void executeActivity(Action action, WorldModel world,
                                         ImageStore imageStore, EventScheduler scheduler);


    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent( this,
                    Activity.createActivityAction(this, world, imageStore),
                    actionPeriod);
    }

}
