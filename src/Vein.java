import processing.core.PImage;
import java.util.List;
import java.util.Optional;

public class Vein extends ActivityEntity{

    private final String ORE_ID_PREFIX = "ore -- ";
    private final int ORE_CORRUPT_MIN = 20000;
    private final int ORE_CORRUPT_MAX = 30000;

    public Vein( String id, Point position,
                 List<PImage> images, int resourceLimit, int resourceCount,
                 int actionPeriod)
    {
        super(id, position, images, 0, 0, actionPeriod);

    }


    public void executeActivity(Action action, WorldModel world,
                                     ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Point> openPt = world.findOpenAround( this.getposition());

        if (openPt.isPresent())
        {
            Entity ore = ParserFactory.createOre(ORE_ID_PREFIX + this.getid(),
                    openPt.get(), ORE_CORRUPT_MIN +
                            rand.nextInt(ORE_CORRUPT_MAX - ORE_CORRUPT_MIN),
                    imageStore.getImageList( ParserFactory.ORE_KEY));
            world.addEntity( ore);
            ((ActivityEntity)ore).scheduleActions( scheduler, world, imageStore);
        }

        scheduler.scheduleEvent(this,
                Activity.createActivityAction(this, world, imageStore),
                getactionPeriod());
    }

}
