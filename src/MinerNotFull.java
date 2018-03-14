import processing.core.PImage;

import java.awt.*;
import java.util.List;
import java.util.Optional;

public class MinerNotFull extends Miner{


    public MinerNotFull( String id, Point position,
                 List<PImage> images, int resourceLimit, int resourceCount,
                 int actionPeriod, int animationPeriod, boolean rotten)
    {

        super(id,position,images,resourceLimit,resourceCount,actionPeriod,animationPeriod, rotten);

    }

    public void transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore){
        Entity miner = null;

        miner = ParserFactory.createMinerNotFull(getid(), getresourceLimit(),
                    getposition(), getactionPeriod(), getanimationPeriod(),
                    imageStore.getImageList( "darkMiner"), true);
            world.removeEntity( this);
            scheduler.unscheduleAllEvents( this);

            world.addEntity( miner);
            ((ActivityEntity)miner).scheduleActions( scheduler, world, imageStore);
    }

    private boolean transformNotFull(WorldModel world,
                                     EventScheduler scheduler, ImageStore imageStore)
    {
        Entity miner = null;
        if (getresourceCount() >= getresourceLimit())
        {
            if(!getRotten()) {
                miner = ParserFactory.createMinerFull(getid(), getresourceLimit(),
                        getposition(), getactionPeriod(), getanimationPeriod(),
                        getimages(), false);
            }else{
                miner = ParserFactory.createMinerFull(getid(), getresourceLimit(),
                        getposition(), getactionPeriod(), getanimationPeriod(),
                        imageStore.getImageList( "darkMiner"), true);
            }
            world.removeEntity( this);
            scheduler.unscheduleAllEvents( this);

            world.addEntity( miner);
            ((ActivityEntity)miner).scheduleActions( scheduler, world, imageStore);

            return true;
        }

        return false;
    }

       public void executeActivity(Action action, WorldModel world, ImageStore imageStore, EventScheduler scheduler)
   {
       if(!getRotten()){
           if(checkForRot(world)){
               setRotten(true);
               transform(world,scheduler,imageStore);
           }
       }


      Optional<Entity> notFullTarget = world.findNearest( getposition(),
              Ore.class);

      if (!notFullTarget.isPresent() ||
              !moveToNotFull(world, notFullTarget.get(), scheduler, imageStore) ||
              !transformNotFull(world, scheduler, imageStore))
      {
         scheduler.scheduleEvent( this,
                 Activity.createActivityAction(this, world, imageStore),
                 getactionPeriod());
      }
   }


   private boolean moveToNotFull(WorldModel world,
                                 Entity target, EventScheduler scheduler, ImageStore imageStore)
   {
      if (getposition().adjacent( target.getposition()))
      {
         setResourceCount(getresourceCount()+1);
         world.removeEntity( target);
         scheduler.unscheduleAllEvents( target);

         return true;
      }
      else
      {
         Point nextPos = nextPositionMiner( world, target.getposition(), scheduler, imageStore);

         if (!getposition().equals(nextPos))
         {
            Optional<Entity> occupant = world.getOccupant( nextPos);
            if (occupant.isPresent())
            {
               scheduler.unscheduleAllEvents(occupant.get());
            }

            world.moveEntity( this, nextPos);
         }
         return false;
      }
   }


}
