import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import processing.core.*;

public final class VirtualWorld
   extends PApplet
{
    private int counter = 0;

   private final int TIMER_ACTION_PERIOD = 100;

   private static final int VIEW_WIDTH =  640;
   private static final int VIEW_HEIGHT = 480;
   private static final int TILE_WIDTH = 32;
   private static final int TILE_HEIGHT = 32;
   private static final int WORLD_WIDTH_SCALE = 2;
   private static final int WORLD_HEIGHT_SCALE = 2;

   private static final int VIEW_COLS = VIEW_WIDTH / TILE_WIDTH;
   private static final int VIEW_ROWS = VIEW_HEIGHT / TILE_HEIGHT;
   private static final int WORLD_COLS = VIEW_COLS * WORLD_WIDTH_SCALE;
   private static final int WORLD_ROWS = VIEW_ROWS * WORLD_HEIGHT_SCALE;

   private final String IMAGE_LIST_FILE_NAME = "imagelist";
   private final String DEFAULT_IMAGE_NAME = "background_default";
   private final int DEFAULT_IMAGE_COLOR = 0x808080;

   private final String LOAD_FILE_NAME = "gaia.sav";

   private static final String FAST_FLAG = "-fast";
   private static final String FASTER_FLAG = "-faster";
   private static final String FASTEST_FLAG = "-fastest";
   private static final double FAST_SCALE = 0.5;
   private static final double FASTER_SCALE = 0.25;
   private static final double FASTEST_SCALE = 0.10;

   private static double timeScale = 1.0;

   private ImageStore imageStore;
   private WorldModel world;
   private WorldView view;
   private EventScheduler scheduler;

   private long next_time;

   private final int KEYED_RED_IDX = 2;
   private final int KEYED_GREEN_IDX = 3;
   private final int KEYED_BLUE_IDX = 4;

   private final int COLOR_MASK = 0xffffff;
   private final int KEYED_IMAGE_MIN = 5;

   private Random rand = new Random();

   public void settings()
   {
      size(VIEW_WIDTH, VIEW_HEIGHT);
   }

   /*
      Processing entry point for "sketch" setup.
   */
   public void setup()
   {
      imageStore = new ImageStore(
         createImageColored(TILE_WIDTH, TILE_HEIGHT, DEFAULT_IMAGE_COLOR));
      world = new WorldModel(WORLD_ROWS, WORLD_COLS,
         createDefaultBackground(imageStore));
      view = new WorldView(VIEW_ROWS, VIEW_COLS, this, world,
         TILE_WIDTH, TILE_HEIGHT);
      scheduler = new EventScheduler(timeScale);

      loadImages(IMAGE_LIST_FILE_NAME, imageStore, this);
      loadWorld(world, LOAD_FILE_NAME, imageStore);

      scheduleActions(world, scheduler, imageStore);

      next_time = System.currentTimeMillis() + TIMER_ACTION_PERIOD;
   }

   public void draw()
   {
      long time = System.currentTimeMillis();
      counter++;
       if(counter == 350){
           spreadRot(imageStore);
           counter = 0;
       }
      if (time >= next_time)
      {
         scheduler.updateOnTime(time);
         next_time = time + TIMER_ACTION_PERIOD;

      }

      view.drawViewport();
   }

   public void keyPressed()
   {
      if (key == CODED)
      {
         int dx = 0;
         int dy = 0;

         switch (keyCode)
         {
            case UP:
               dy = -1;
               break;
            case DOWN:
               dy = 1;
               break;
            case LEFT:
               dx = -1;
               break;
            case RIGHT:
               dx = 1;
               break;
         }
         view.shiftView(dx, dy);
      }
   }

   public void mousePressed(){
      int x = mouseX / 32;
      int y = mouseY / 32;


      Point pos = view.getViewport().viewportToWorld(x, y);
      changeBackgroundOnClick(pos);
   }



   private void changeBackgroundOnClick(Point position){
       Point down = new Point(position.getX() , position.getY() + 1 );

      List<Point> points = new ArrayList<Point>();
      points.add(position);
      points.add(down);
      points.add(new Point(position.getX() , position.getY() - 1 ));
      points.add( new Point(position.getX() +1, position.getY()));
      points.add( new Point(position.getX() -1, position.getY()));
      points.add( new Point(position.getX() , position.getY()-2));
      points.add( new Point(position.getX() , position.getY() + 2));

      for (Point point : points) {
         if (world.withinBounds(point) && (!(world.isOccupied(point)) || world.getOccupant(point).get().getClass() != Obstacle.class)) {
            world.setBackground(point, new Background("rot",
                    imageStore.getImageList("rot")));
         }
      }
      List<Point> trees = new ArrayList<Point>();
      trees.add(new Point(position.getX() + rand.nextInt(3) + 1, position.getY() + rand.nextInt(3) + 1));
      trees.add( new Point(position.getX() - rand.nextInt(3) - 1, position.getY() - rand.nextInt(3) - 1));

      for (Point point : trees) {
         if (world.withinBounds(point) && (!(world.isOccupied(point)) || world.getOccupant(point).get().getClass() != Obstacle.class)) {
            world.setBackground(point, new Background("tree",
                    imageStore.getImageList("tree")));
         }
      }

       if (world.withinBounds(down) && !world.isOccupied(down)) {
           //world.tryAddEntity(
           AnimationEntity bug = new bug("bug", down, imageStore.getImageList("bug"), 0, 0, 1000, 900);
           world.addEntity( bug);
           (bug).scheduleActions( scheduler, world, imageStore);
       }
       if (world.withinBounds(position) && !world.isOccupied(position)) {
           //world.tryAddEntity(
           AnimationEntity rotMother = new RotMother("mother", position, imageStore.getImageList("mother"), 0, 0, 0, 5);
           world.addEntity( rotMother);
           (rotMother).scheduleActions( scheduler, world, imageStore);
       }

   }

   private Background createDefaultBackground(ImageStore imageStore)
   {
      return new Background(DEFAULT_IMAGE_NAME,
         imageStore.getImageList( DEFAULT_IMAGE_NAME));
   }

   public void spreadRot( ImageStore imageStore){
    List<Point> changeList = new ArrayList<Point>();
     for (int row = 0; row < world.getNumRows(); row++) {
         for (int col = 0; col < world.getNumCols(); col++) {
             Point point = view.getViewport().viewportToWorld(row, col);
             if (world.withinBounds(point) && world.getBackgroundCell(point).getId().equals("rot")) {
                 for (int x = -1; x <= 1; x++) {
                     for (int y = -1; y <= 1; y++) {
                         Point neighbor = new Point(point.getX() + x, point.getY() + y);
                         if (world.withinBounds(neighbor) && (!(world.isOccupied(neighbor))) && !(world.getBackgroundCell(neighbor).getId().equals("tree"))  ) {
                            if ((rand.nextInt(50) + 1) > 40){
                                changeList.add(neighbor);
                                if ((rand.nextInt(50) + 1) > 50){
                                    Point extra = new Point(point.getX() + x, point.getY() + y);
                                    if (world.withinBounds(extra) && (!(world.isOccupied(extra))) && !(world.getBackgroundCell(extra).getId().equals("tree"))  ) {
                                        if ((rand.nextInt(50) + 1) > 40){
                                            changeList.add(extra);
                                        }
                                    }
                                }
                            }
                         }
                     }
                 }
             }
         }
     }
     for(Point point : changeList){
         world.setBackground(point, new Background("rot",
                                     imageStore.getImageList("rot")));
     }
 }

   private PImage createImageColored(int width, int height, int color)
   {
      PImage img = new PImage(width, height, RGB);
      img.loadPixels();
      for (int i = 0; i < img.pixels.length; i++)
      {
         img.pixels[i] = color;
      }
      img.updatePixels();
      return img;
   }

   private void loadImages(String filename, ImageStore imageStore,
      PApplet screen)
   {
      try
      {
         Scanner in = new Scanner(new File(filename));
         loadImages(imageStore, in, screen);
      }
      catch (FileNotFoundException e)
      {
         System.err.println(e.getMessage());
      }
   }

   private void loadImages(ImageStore imageStore, Scanner in, PApplet screen)
   {
      int lineNumber = 0;
      while (in.hasNextLine())
      {
         try
         {
            processImageLine(imageStore.getImages(), in.nextLine(), screen);
         }
         catch (NumberFormatException e)
         {
            System.out.println(String.format("Image format error on line %d",
                    lineNumber));
         }
         lineNumber++;
      }
   }



   private void processImageLine(Map<String, List<PImage>> images,
                                 String line, PApplet screen)
   {
      String[] attrs = line.split("\\s");
      if (attrs.length >= 2)
      {
         String key = attrs[0];
         PImage img = screen.loadImage(attrs[1]);
         if (img != null && img.width != -1)
         {
            List<PImage> imgs = getImages(images, key);
            imgs.add(img);

            if (attrs.length >= KEYED_IMAGE_MIN)
            {
               int r = Integer.parseInt(attrs[KEYED_RED_IDX]);
               int g = Integer.parseInt(attrs[KEYED_GREEN_IDX]);
               int b = Integer.parseInt(attrs[KEYED_BLUE_IDX]);
               setAlpha(img, screen.color(r, g, b), 0);
            }
         }
      }
   }
   /*
     Called with color for which alpha should be set and alpha value.
     setAlpha(img, color(255, 255, 255), 0));
   */
   private void setAlpha(PImage img, int maskColor, int alpha)
   {
      int alphaValue = alpha << 24;
      int nonAlpha = maskColor & COLOR_MASK;
      img.format = PApplet.ARGB;
      img.loadPixels();
      for (int i = 0; i < img.pixels.length; i++)
      {
         if ((img.pixels[i] & COLOR_MASK) == nonAlpha)
         {
            img.pixels[i] = alphaValue | nonAlpha;
         }
      }
      img.updatePixels();
   }

   private List<PImage> getImages(Map<String, List<PImage>> images,
                                  String key)
   {
      List<PImage> imgs = images.get(key);
      if (imgs == null)
      {
         imgs = new LinkedList<>();
         images.put(key, imgs);
      }
      return imgs;
   }

   private void loadWorld(WorldModel world, String filename,
      ImageStore imageStore)
   {
      try
      {
         Scanner in = new Scanner(new File(filename));
         world.load(in, imageStore);
      }
      catch (FileNotFoundException e)
      {
         System.err.println(e.getMessage());
      }
   }

   private void scheduleActions(WorldModel world,
      EventScheduler scheduler, ImageStore imageStore)
   {
      for (Entity entity : world.getEntities())
      {
         if (!(entity instanceof Obstacle || entity instanceof Blacksmith)){
            ((ActivityEntity)entity).scheduleActions( scheduler, world, imageStore);
      }
      }
   }

   private static void parseCommandLine(String [] args)
   {
       for (String arg : args)
      {
         switch (arg)
         {
            case FAST_FLAG:
               timeScale = Math.min(FAST_SCALE, timeScale);
               break;
            case FASTER_FLAG:
               timeScale = Math.min(FASTER_SCALE, timeScale);
               break;
            case FASTEST_FLAG:
               timeScale = Math.min(FASTEST_SCALE, timeScale);
               break;
         }
      }
   }

   public static void main(String [] args)
   {
      parseCommandLine(args);
      PApplet.main(VirtualWorld.class);
   }
}
