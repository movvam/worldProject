import processing.core.PImage;
import java.util.List;

public class Ore extends ActivityEntity{

    private final String BLOB_KEY = "blob";
    private final String BLOB_ID_SUFFIX = " -- blob";
    private final int BLOB_PERIOD_SCALE = 4;
    private final int BLOB_ANIMATION_MIN = 50;
    private final int BLOB_ANIMATION_MAX = 150;


    public Ore( String id, Point position,
                List<PImage> images, int resourceLimit, int resourceCount,
                int actionPeriod)
    {

        super(id, position, images, 0, 0, actionPeriod);
        }




    public void executeActivity( Action action,WorldModel world,
                                    ImageStore imageStore, EventScheduler scheduler)
    {
        Point pos = getposition();  // store current position before removing

        world.removeEntity( this);
        scheduler.unscheduleAllEvents( this);

        AnimationEntity blob = ParserFactory.createOreBlob(getid() + BLOB_ID_SUFFIX,
                pos, getactionPeriod() / BLOB_PERIOD_SCALE,
                (BLOB_ANIMATION_MIN +
                        rand.nextInt(BLOB_ANIMATION_MAX - BLOB_ANIMATION_MIN)),
                imageStore.getImageList( BLOB_KEY));

        world.addEntity( blob);
        (blob).scheduleActions( scheduler, world, imageStore);
    }

}
