final class Event
{
   private Action action;
   private long time;
   private Entity entity;

   public Event(Action action, long time, Entity entity)
   {
      this.action = action;
      this.time = time;
      this.entity = entity;

   }

   protected Entity getEntity() {
      return entity;
   }

   protected Action getAction() {
      return action;
   }

   protected long getTime() {

      return time;
   }

}
