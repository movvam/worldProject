import processing.core.PImage;
import java.util.List;
import java.util.Optional;

public class OreBlob extends AnimationEntity{


    public OreBlob( String id, Point position,
                 List<PImage> images, int resourceLimit, int resourceCount,
                 int actionPeriod, int animationPeriod)
    {

        super(id,position,images,resourceLimit,resourceCount,actionPeriod,animationPeriod, 0);

    }


       private Point nextPositionOreBlob( WorldModel world,
                                           Point destPos)
       {
          PathingStrategy strategy = new AStarPathingStrategy();

          List<Point> path = strategy.computePath(this.getposition(), destPos, p -> (world.withinBounds(p) && (!(world.isOccupied(p)) || world.getOccupant(p).get().getClass() == this.getClass())),
               (p1, p2) -> p1.adjacent( p2),
               strategy.CARDINAL_NEIGHBORS);
          if(path.size() != 0){
             return path.get(0);
          }
          return this.getposition();
       }


    public void executeActivity(Action action, WorldModel world,
                                             ImageStore imageStore, EventScheduler scheduler)
   {

      Optional<Entity> blobTarget = world.findNearest(
              this.getposition(), Vein.class);
      long nextPeriod = getactionPeriod();

      if (blobTarget.isPresent())
      {
         Point tgtPos = blobTarget.get().getposition();

         if (moveToOreBlob( world, blobTarget.get(), scheduler))
         {
            Entity quake = ParserFactory.createQuake(tgtPos,
                    imageStore.getImageList(ParserFactory.QUAKE_KEY));

            world.addEntity( quake);
            nextPeriod += getactionPeriod();
             ((ActivityEntity)quake).scheduleActions( scheduler, world, imageStore);
         }
      }

      scheduler.scheduleEvent( this,
              Activity.createActivityAction(this, world, imageStore),
              nextPeriod);


   }

       private boolean moveToOreBlob( WorldModel world,
                                       Entity target, EventScheduler scheduler)
   {
      if (getposition().adjacent(target.getposition()))
      {
         world.removeEntity( target);
         scheduler.unscheduleAllEvents( target);
         return true;
      }
      else
      {
         Point nextPos = nextPositionOreBlob( world, target.getposition());

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
