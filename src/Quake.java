import processing.core.PImage;
import java.util.List;

public class Quake extends AnimationEntity{

    private static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;


    public Quake( String id, Point position,
                 List<PImage> images, int resourceLimit, int resourceCount,
                 int actionPeriod, int animationPeriod)
    {

        super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod, QUAKE_ANIMATION_REPEAT_COUNT);
    }


   public void executeActivity(Action action, WorldModel world,
                                           ImageStore imageStore, EventScheduler scheduler)
   {
      scheduler.unscheduleAllEvents( this);
      world.removeEntity( this);
   }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent( this,
                    Activity.createActivityAction( this, world, imageStore),
                    getactionPeriod());
            scheduler.scheduleEvent( this,
                    Animation.createAnimationAction( this, QUAKE_ANIMATION_REPEAT_COUNT),
                    getanimationPeriod());
    }

}
