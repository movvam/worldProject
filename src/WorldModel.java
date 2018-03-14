import processing.core.PImage;

import java.util.*;

final class WorldModel
{

   private int numRows;
   private int numCols;
   private Background background[][];
   private Entity occupancy[][];

   private Set<Entity> entities;

   private final int ORE_REACH = 1;

   private final int PROPERTY_KEY = 0;

   protected int getNumRows() {
      return numRows;
   }

   protected int getNumCols() {
      return numCols;
   }

   protected Set<Entity> getEntities() {
      return entities;
   }

   public WorldModel(int numRows, int numCols, Background defaultBackground)
   {
      this.numRows = numRows;
      this.numCols = numCols;
      this.background = new Background[numRows][numCols];
      this.occupancy = new Entity[numRows][numCols];
      this.entities = new HashSet<>();


      for (int row = 0; row < numRows; row++)
      {
         Arrays.fill(this.background[row], defaultBackground);
      }
   }

   public Optional<Point> findOpenAround( Point pos)
   {
      for (int dy = -ORE_REACH; dy <= ORE_REACH; dy++)
      {
         for (int dx = -ORE_REACH; dx <= ORE_REACH; dx++)
         {
            Point newPt = new Point(pos.getX() + dx, pos.getY() + dy);
            if (withinBounds( newPt) &&
                    !isOccupied( newPt))
            {
               return Optional.of(newPt);
            }
         }
      }

      return Optional.empty();
   }

   public void load(Scanner in, ImageStore imageStore)
   {
      int lineNumber = 0;
      while (in.hasNextLine())
      {
         try
         {
            if (!processLine(in.nextLine(), imageStore))
            {
               System.err.println(String.format("invalid entry on line %d",
                       lineNumber));
            }
         }
         catch (NumberFormatException e)
         {
            System.err.println(String.format("invalid entry on line %d",
                    lineNumber));
         }
         catch (IllegalArgumentException e)
         {
            System.err.println(String.format("issue on line %d: %s",
                    lineNumber, e.getMessage()));
         }
         lineNumber++;
      }
   }

   private boolean processLine(String line, ImageStore imageStore)
   {
      String[] properties = line.split("\\s");
      if (properties.length > 0)
      {
         switch (properties[PROPERTY_KEY])
         {
            case ParserFactory.BGND_KEY:
               return ParserFactory.parseBackground(properties, this, imageStore);
            case ParserFactory.MINER_KEY:
               return ParserFactory.parseMiner(properties, this, imageStore);
            case ParserFactory.OBSTACLE_KEY:
               return ParserFactory.parseObstacle(properties, this, imageStore);
            case ParserFactory.ORE_KEY:
               return ParserFactory.parseOre(properties, this, imageStore);
            case ParserFactory.SMITH_KEY:
               return ParserFactory.parseSmith(properties, this, imageStore);
            case ParserFactory.VEIN_KEY:
               return ParserFactory.parseVein(properties, this, imageStore);
         }
      }

      return false;
   }

   public void tryAddEntity(Entity entity)
   {
      if (isOccupied( entity.getposition()))
      {
         // arguably the wrong type of exception, but we are not
         // defining our own exceptions yet
         throw new IllegalArgumentException("position occupied");
      }

      addEntity( entity);
   }

   public boolean withinBounds(Point pos)
   {
      return pos.getY() >= 0 && pos.getY() < numRows &&
              pos.getX() >= 0 && pos.getX() < numCols;
   }

   public boolean isOccupied( Point pos)
   {
      return withinBounds(pos) &&
              getOccupancyCell( pos) != null;
   }

   public void addEntity(Entity entity)
   {
      if (withinBounds( entity.getposition()))
      {
         setOccupancyCell( entity.getposition(), entity);
         entities.add(entity);
      }
   }

   public void moveEntity( Entity entity, Point pos)
   {
      Point oldPos = entity.getposition();
      if (withinBounds( pos) && !pos.equals(oldPos))
      {
         setOccupancyCell( oldPos, null);
         removeEntityAt( pos);
         setOccupancyCell( pos, entity);
         entity.setposition(pos);
      }
   }

   public void removeEntity(Entity entity)
   {
      removeEntityAt( entity.getposition());
   }

   public void removeEntityAt(Point pos)
   {
      if (withinBounds( pos)
              && getOccupancyCell( pos) != null)
      {
         Entity entity = getOccupancyCell( pos);

         /* this moves the entity just outside of the grid for
            debugging purposes */
         entity.setposition( new Point(-1, -1));
         entities.remove(entity);
         setOccupancyCell( pos, null);
      }
   }

   public Optional<PImage> getBackgroundImage(            Point pos)
   {
      if (withinBounds( pos))
      {
         return Optional.of(getBackgroundCell( pos).getCurrentImage());
      }
      else
      {
         return Optional.empty();
      }
   }

   public void setBackground(Point pos,
                                    Background background)
   {
      if (withinBounds( pos))
      {
         setBackgroundCell( pos, background);
      }
   }

   public Optional<Entity> getOccupant(Point pos)
   {
      if (isOccupied( pos))
      {
         return Optional.of(getOccupancyCell( pos));
      }
      else
      {
         return Optional.empty();
      }
   }

   public Entity getOccupancyCell(Point pos)
   {
      return occupancy[pos.getY()][pos.getX()];
   }

   private void setOccupancyCell(Point pos,
                                       Entity entity)
   {
      occupancy[pos.getY()][pos.getX()] = entity;
   }

   public Background getBackgroundCell(Point pos)
   {
      return background[pos.getY()][pos.getX()];
   }

   public Background[][] getBackground() {
      return background;
   }

   public void setBackgroundCell(Point pos,
                                 Background background) {
      this.background[pos.getY()][pos.getX()] = background;
   }

   public Optional<Entity> findNearest(Point pos, Class c) {
      {
         List<Entity> ofType = new LinkedList<>();
         for (Entity entity : getEntities()) {
            if (entity.getClass() == c) {
               ofType.add(entity);
            }
         }

         return nearestEntity(ofType, pos);
      }
   }

   public Optional<Entity> nearestEntity(List<Entity> entities, Point pos){
      if (entities.isEmpty())
      {
         return Optional.empty();
      }
      else
      {
         Entity nearest = entities.get(0);
         int nearestDistance = nearest.getposition().distanceSquared( pos);

         for (Entity other : entities)
         {
            int otherDistance = other.getposition().distanceSquared( pos);

            if (otherDistance < nearestDistance)
            {
               nearest = other;
               nearestDistance = otherDistance;
            }
         }

         return Optional.of(nearest);
      }
   }
}
