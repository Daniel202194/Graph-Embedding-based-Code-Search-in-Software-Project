package algorithms.search;

public abstract class AState implements Comparable<AState>{
    private int cost;
    private AState comeFrom;

    public AState(AState comeFrom, int cost) {
        this.cost = cost;
        this.comeFrom = comeFrom;
    }

    public AState(){
        this.cost = 10;
        this.comeFrom = null;
    }

    public int getCost() { return cost; }

    public AState getComeFrom() {
        return comeFrom;
    }


    public void setComeFrom(AState comeFrom) {
        this.comeFrom = comeFrom;
    }

    public int compareTo(AState o){
        if (this.getCost() > o.getCost())
            return 1;
        else if (this.getCost() < o.getCost())
            return -1;
        return 0;
    }
}
