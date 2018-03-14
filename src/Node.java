public class Node{
    private Point point;
    private int fScore;
    private int gScore;

    public Node(Point point,  int gScore, int fScore){
        this.point = point;
        this.gScore = gScore;
        this.fScore = fScore;

    }
    public Point getPoint(){return point;}
    public int getFScore(){return fScore;}
    public void setFScore(int fScore){this.fScore = fScore;}

    public int getGScore(){return gScore;}
    public void setGScore(int gScore){this.gScore = gScore;}

    public String toString(){return "Node: " + point + " fScore: " + fScore + " gScore: " + gScore;}
}

