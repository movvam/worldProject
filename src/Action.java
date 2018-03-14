public abstract class Action
{
   private Entity entity;

   public Action(Entity entity)
   {
      this.entity = entity;
   }

   protected Entity getEntity() { return entity; }

   public abstract void executeAction(EventScheduler scheduler);

}
