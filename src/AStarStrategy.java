import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class AStarPathingStrategy
   implements PathingStrategy
{
   public List<Point> computePath(Point start, Point end,
      Predicate<Point> canPassThrough,
      BiPredicate<Point, Point> withinReach,
      Function<Point, Stream<Point>> potentialNeighbors)
   {

      // Create lists and maps
      List<Point> openList = new ArrayList<>();
      List<Point> closedList = new ArrayList<>();
      Map<Point, Integer> fScore = new HashMap<>();
      Map<Point, Integer> gScore = new HashMap<>();
      Map<Point, Point> prevPoint = new HashMap<>();

      // Add start node
      openList.add(start);
      fScore.put(start, heuristic(start, end));
      gScore.put(start, 0);
      prevPoint.put(start, null);

      Point current = start;

      while(!current.adjacent(end) && openList.size() != 0) {

         // Find valid adjacent nodes
         List<Point> neighbors = potentialNeighbors.apply(current)
                 .filter(canPassThrough)
                 .filter(p -> !closedList.contains(p))
                 .collect(Collectors.toList());

         // Add adjacent nodes to closed list
         for (Point p : neighbors) {
            if (!openList.contains(p)) {
               int g = gScore.get(current) + 1;
               int h = heuristic(p, end);
               int f = g + h;
               openList.add(p);

               prevPoint.put(p, current);
               gScore.put(p, g);
               fScore.put(p, f);
            }
         }

         closedList.add(current);
         openList.remove(current);

         if(openList.size() > 0) {
            current = openList.get(0);

            for (Point p : openList) {
               if (fScore.get(p) <= fScore.get(current)) {
                  current = p;
                  break;
               }
            }
         }
      }

      // Make path
      List<Point> path = new ArrayList<>();

      while(prevPoint.get(current) != null) {
         path.add(0, current);
         current = prevPoint.get(current);
      }

      path.add(current);
      return path;
   }

   private int heuristic(Point p1, Point p2) {
      return Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y);
   }
}
