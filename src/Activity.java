public class Activity extends Action{
    private static WorldModel world;
    private static ImageStore imageStore;

    public Activity(Entity entity, WorldModel world, ImageStore imageStore)
    {
        super(entity);
        this.world = world;
        this.imageStore = imageStore;
    }

    public static Activity createActivityAction(Entity entity, WorldModel world,
                                                ImageStore imageStore)
    {
        return new Activity(entity, world, imageStore);
    }

    public void executeAction(EventScheduler scheduler)
    {
        ((ActivityEntity)this.getEntity()).executeActivity(this,world, imageStore, scheduler);
    }

    public WorldModel getWorld() { return world; }
    public ImageStore getImageStore() { return imageStore; }

}
