import java.util.*;
import java.util.function.Predicate;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import java.util.List;
import java.lang.Math;

class AStarPathingStrategy
        implements PathingStrategy {
    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors) {

        List<Point> closedSet = new ArrayList<Point>();
        List<Point> openSet = new ArrayList<Point>();
        Map<Point, Point> cameFrom = new HashMap<Point, Point>();
        Map<Point, Integer> gScore = new HashMap<Point, Integer>();
        Map<Point, Integer> fScore = new HashMap<Point, Integer>();
        //Point current = start;

        openSet.add(start);
        gScore.put(start, 0);
        fScore.put(start, heuristic(start, end) + gScore.get(start));
        while (openSet.size() != 0) {
            Point current = findLowest(fScore, openSet);

            if(withinReach.test(current,end)){
                cameFrom.put(end, current);
                List<Point> path =  reconstruct_path(cameFrom, current);
                Collections.reverse(path);
                return path;
            }

            openSet.remove(current);
            closedSet.add(current);

            List<Point> neighbors = potentialNeighbors.apply(current)
                    .filter(canPassThrough)
//                 .filter(p -> !closedSet.contains(p))
//                 .filter(p -> !openSet.contains(p))
                    .collect(Collectors.toList());

            for (Point n : neighbors) {
                if(closedSet.contains(n)){
                    continue;
                }
                if(!(openSet.contains(n))){
                    int tentative_gScore = gScore.get(current) + 1;
                    openSet.add(n);
                    cameFrom.put(n, current);
                    gScore.put(n, tentative_gScore);
                    fScore.put(n, gScore.get(n) + heuristic(n, end));
                }else{
                    cameFrom.put(n, current);
                }
            }

        }
        return new ArrayList<Point>();

    }


    public Point findLowest(Map<Point, Integer> fScore, List<Point> openSet) {

        List<Point> filteredList = fScore.keySet().stream()
                .filter(p -> openSet.contains(p))
                .collect(Collectors.toList());
        Point lowest = filteredList.get(0);

        for (Point p : filteredList){
            if (fScore.get(p) < fScore.get(lowest)){
                lowest = p;
            }
        }
        return lowest;
    }

    public int heuristic(Point start, Point end) {
        return Math.abs(start.x - end.x) + Math.abs(start.y - end.y);
    }


    public List<Point> reconstruct_path(Map<Point, Point> cameFrom, Point current) {
        List<Point> total_path = new ArrayList<Point>();
        total_path.add(current);
        while(cameFrom.keySet().contains(current)){
            current = cameFrom.get(current);
            total_path.add(current);
        }
        total_path.remove(total_path.size() - 1);
        return total_path;
    }


}
