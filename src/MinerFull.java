import processing.core.PImage;
import java.util.List;
import java.util.Optional;

public class MinerFull extends Miner {


    public MinerFull( String id, Point position,
                 List<PImage> images, int resourceLimit, int resourceCount,
                 int actionPeriod, int animationPeriod, boolean rotten)
    {

        super(id,position,images,resourceLimit,resourceCount,actionPeriod,animationPeriod, rotten);
    }

    public void transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore){
        Entity miner = null;
        miner = ParserFactory.createMinerFull(getid(), getresourceLimit(),
                getposition(), getactionPeriod(), getanimationPeriod(),
                imageStore.getImageList( "darkMiner"), true);
        world.removeEntity( this);
        scheduler.unscheduleAllEvents( this);

        world.addEntity( miner);
        ((ActivityEntity)miner).scheduleActions( scheduler, world, imageStore);

    }

    private void transformFull( WorldModel world,
                                EventScheduler scheduler, ImageStore imageStore)
    {
        Entity miner = null;

        if(!getRotten()) {
            miner = ParserFactory.createMinerNotFull(getid(), getresourceLimit(),
                    getposition(), getactionPeriod(), getanimationPeriod(),
                    getimages(), false);
        }else{
            miner = ParserFactory.createMinerNotFull(getid(), getresourceLimit(),
                    getposition(), getactionPeriod(), getanimationPeriod(),
                    imageStore.getImageList( "darkMiner"), true);
        }
        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(miner);
        ((ActivityEntity) miner).scheduleActions(scheduler, world, imageStore);

    }

    public void executeActivity(Action action, WorldModel world,
                                               ImageStore imageStore, EventScheduler scheduler)
   {
       if(!getRotten()){
           if(checkForRot(world)){
               setRotten(true);
               transform(world,scheduler,imageStore);
           }
       }

      Optional<Entity> fullTarget = null;

       if(!getRotten()) {
           fullTarget = world.findNearest(this.getposition(),
                   Blacksmith.class);
       }else{
           fullTarget = world.findNearest(this.getposition(),
                   RotMother.class);
       }

      if (fullTarget.isPresent() &&
              moveToFull(world, fullTarget.get(), scheduler, imageStore))
      {
         transformFull(world, scheduler, imageStore);
      }
      else
      {
         scheduler.scheduleEvent(this,
                 Activity.createActivityAction(this, world, imageStore),
                 getactionPeriod());
      }
   }

   private boolean moveToFull( WorldModel world,
                                    Entity target, EventScheduler scheduler, ImageStore imageStore)
   {
      if (getposition().adjacent( target.getposition()))
      {
         return true;
      }
      else
      {
         Point nextPos = nextPositionMiner( world, target.getposition(),  scheduler,  imageStore);

         if (!getposition().equals(nextPos))
         {
            Optional<Entity> occupant = world.getOccupant( nextPos);
            if (occupant.isPresent())
            {
               scheduler.unscheduleAllEvents( occupant.get());
            }

            world.moveEntity( this, nextPos);
         }
         return false;
      }
   }


}
