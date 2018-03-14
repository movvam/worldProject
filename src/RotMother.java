
import processing.core.PImage;
        import java.util.List;
        import java.util.Optional;

public class RotMother extends AnimationEntity{

    public RotMother(String id, Point position,
               List<PImage> images, int resourceLimit, int resourceCount,
               int actionPeriod, int animationPeriod)
    {

        super(id,position,images,resourceLimit,resourceCount,actionPeriod,animationPeriod, 0);

    }

    public void executeActivity(Action action, WorldModel world,
                                ImageStore imageStore, EventScheduler scheduler)
    {
        long nextPeriod = getactionPeriod();

        scheduler.scheduleEvent( this,
                Activity.createActivityAction(this, world, imageStore),
                nextPeriod);

    }


}
