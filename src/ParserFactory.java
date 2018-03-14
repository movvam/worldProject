import processing.core.PImage;

import java.util.List;

abstract class ParserFactory {

   public static final String SMITH_KEY = "blacksmith";
   private static final int SMITH_NUM_PROPERTIES = 4;
   private static final int SMITH_ID = 1;
   private static final int SMITH_COL = 2;
   private static final int SMITH_ROW = 3;

   public static final String VEIN_KEY = "vein";
   private static final int VEIN_NUM_PROPERTIES = 5;
   private static final int VEIN_ID = 1;
   private static final int VEIN_COL = 2;
   private static final int VEIN_ROW = 3;
   private static final int VEIN_ACTION_PERIOD = 4;

   public static final String MINER_KEY = "miner";
   private static final int MINER_NUM_PROPERTIES = 7;
   private static final int MINER_ID = 1;
   private static final int MINER_COL = 2;
   private static final int MINER_ROW = 3;
   private static final int MINER_LIMIT = 4;
   private static final int MINER_ACTION_PERIOD = 5;
   private static final int MINER_ANIMATION_PERIOD = 6;

   public static final String OBSTACLE_KEY = "obstacle";
   private static final int OBSTACLE_NUM_PROPERTIES = 4;
   private static final int OBSTACLE_ID = 1;
   private static final int OBSTACLE_COL = 2;
   private static final int OBSTACLE_ROW = 3;

   public static final String ORE_KEY = "ore";
   private static final int ORE_NUM_PROPERTIES = 5;
   public static final int ORE_ID = 1;
   private static final int ORE_COL = 2;
   private static final int ORE_ROW = 3;
   private static final int ORE_ACTION_PERIOD = 4;

   public static final String QUAKE_KEY = "quake";
   private static final String QUAKE_ID = "quake";
   private static final int QUAKE_ACTION_PERIOD = 1100;
   private static final int QUAKE_ANIMATION_PERIOD = 100;

   public static final String BGND_KEY = "background";
   private static final int BGND_NUM_PROPERTIES = 4;
   private static final int BGND_ID = 1;
   private static final int BGND_COL = 2;
   private static final int BGND_ROW = 3;

   private static Blacksmith createBlacksmith(String id, Point position,
                                             List<PImage> images)
   {
      return new Blacksmith( id, position, images,
              0, 0);
   }


   public static boolean parseSmith(String [] properties, WorldModel world,
                                    ImageStore imageStore)
   {
      if (properties.length == SMITH_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[SMITH_COL]),
                 Integer.parseInt(properties[SMITH_ROW]));
         Blacksmith entity = createBlacksmith(properties[SMITH_ID],
                 pt, imageStore.getImageList( SMITH_KEY));
         world.tryAddEntity(entity);
      }

      return properties.length == SMITH_NUM_PROPERTIES;
   }

   public static Vein createVein(String id, Point position, int actionPeriod,
                                 List<PImage> images)
   {
      return new Vein(id, position, images, 0, 0,
              actionPeriod);
   }

   public static boolean parseVein(String [] properties, WorldModel world,
                                   ImageStore imageStore)
   {
      if (properties.length == VEIN_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[VEIN_COL]),
                 Integer.parseInt(properties[VEIN_ROW]));
         Entity entity = createVein(properties[VEIN_ID],
                 pt,
                 Integer.parseInt(properties[VEIN_ACTION_PERIOD]),
                 imageStore.getImageList( VEIN_KEY));
         world.tryAddEntity(entity);
      }

      return properties.length == VEIN_NUM_PROPERTIES;
   }

   public static MinerNotFull createMinerNotFull(String id, int resourceLimit,
                                                 Point position, int actionPeriod, int animationPeriod,
                                                 List<PImage> images, boolean rotten)
   {
      return new MinerNotFull(id, position, images,
              resourceLimit, 0, actionPeriod, animationPeriod, rotten);
   }

   public static MinerFull createMinerFull(String id, int resourceLimit,
                                           Point position, int actionPeriod, int animationPeriod,
                                           List<PImage> images, boolean rotten)
   {
      return new MinerFull(id, position, images,
              resourceLimit, resourceLimit, actionPeriod, animationPeriod, rotten);
   }


   public static boolean parseMiner(String [] properties, WorldModel world,
                                    ImageStore imageStore)
   {
      if (properties.length == MINER_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[MINER_COL]),
                 Integer.parseInt(properties[MINER_ROW]));
         Entity entity = createMinerNotFull(properties[MINER_ID],
                 Integer.parseInt(properties[MINER_LIMIT]),
                 pt,
                 Integer.parseInt(properties[MINER_ACTION_PERIOD]),
                 Integer.parseInt(properties[MINER_ANIMATION_PERIOD]),
                 imageStore.getImageList( MINER_KEY), false);
         world.tryAddEntity( entity);
      }

      return properties.length == MINER_NUM_PROPERTIES;
   }

   public static Obstacle createObstacle(String id, Point position,
                                          List<PImage> images)
   {
      return new Obstacle(id, position, images,
              0, 0);
   }


   public static boolean parseObstacle(String [] properties, WorldModel world,
                                       ImageStore imageStore)
   {
      if (properties.length == OBSTACLE_NUM_PROPERTIES)
      {
         Point pt = new Point(
                 Integer.parseInt(properties[OBSTACLE_COL]),
                 Integer.parseInt(properties[OBSTACLE_ROW]));
         Entity entity = createObstacle(properties[OBSTACLE_ID],
                 pt, imageStore.getImageList( OBSTACLE_KEY));
         world.tryAddEntity( entity);
      }

      return properties.length == OBSTACLE_NUM_PROPERTIES;
   }

   public static Ore createOre(String id, Point position, int actionPeriod,
                               List<PImage> images)
   {
      return new Ore(id, position, images, 0, 0,
              actionPeriod);
   }


   public static boolean parseOre(String [] properties, WorldModel world,
                                  ImageStore imageStore)
   {
      if (properties.length == ORE_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[ORE_COL]),
                 Integer.parseInt(properties[ORE_ROW]));
         Entity entity = createOre(properties[ORE_ID],
                 pt, Integer.parseInt(properties[ORE_ACTION_PERIOD]),
                 imageStore.getImageList( ORE_KEY));
         world.tryAddEntity( entity);
      }

      return properties.length == ORE_NUM_PROPERTIES;
   }

   public static OreBlob createOreBlob(String id, Point position,
                                       int actionPeriod, int animationPeriod, List<PImage> images)
   {
      return new OreBlob( id, position, images,
              0, 0, actionPeriod, animationPeriod);
   }

   public static Quake createQuake(Point position, List<PImage> images)
   {
      return new Quake( QUAKE_ID, position, images,
              0, 0, QUAKE_ACTION_PERIOD, QUAKE_ANIMATION_PERIOD);
   }

   public static boolean parseBackground(String [] properties,
                                          WorldModel world, ImageStore imageStore)
   {
      if (properties.length == BGND_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[BGND_COL]),
                 Integer.parseInt(properties[BGND_ROW]));
         String id = properties[BGND_ID];
         world.setBackground( pt,
                 new Background(id, imageStore.getImageList( id)));
      }

      return properties.length == BGND_NUM_PROPERTIES;
   }
}
