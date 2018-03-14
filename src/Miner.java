import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;

abstract class Miner extends AnimationEntity{

    private boolean rotten;

    public Miner(String id, Point position,
                        List<PImage> images, int resourceLimit, int resourceCount,
                        int actionPeriod, int animationPeriod, boolean rotten)
    {

        super(id,position,images,resourceLimit,resourceCount,actionPeriod,animationPeriod, 0);
        this.rotten = rotten;

    }


    public abstract void transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore);

    public Point nextPositionMiner(WorldModel world,
                                    Point destPos, EventScheduler scheduler, ImageStore imageStore)
    {

        PathingStrategy strategy = new AStarPathingStrategy();

        List<Point> path = strategy.computePath(this.getposition(), destPos, p -> (world.withinBounds(p) && !(world.isOccupied(p))),
                (p1, p2) -> p1.adjacent(p2),
                strategy.CARDINAL_NEIGHBORS);
        if(path.size() != 0){
            return path.get(0);
        }
        return this.getposition();
    }

    public boolean checkForRot(WorldModel world){

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                Point point = new Point(getposition().getX() + x, getposition().getY()  + y );
                if(world.withinBounds(point) && world.getBackgroundCell(point).getId().equals("rot")){
                    return true;
                    //remove and recreate in that instant instead of this
                }
            }
        }
        return false;
    }

    public boolean getRotten(){return rotten;}
    public void setRotten(boolean rot){rotten = rot;}

}
