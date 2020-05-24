package gui.graph;

import java.util.ArrayList;

public class GraphData {
    private int lapNumber;
    private ArrayList<Integer> values;
    private ArrayList<Integer> distances;

    public GraphData(int lapNumber){
        this.lapNumber = lapNumber;
        values = new ArrayList<>();
        distances = new ArrayList<>();
    }
    public int getLapNumber(){
        return lapNumber;
    }
    public void setValue(int value){
        values.add(value);
    }
    public ArrayList<Integer> getValues(){
        return values;
    }
    public void setDistance(Integer distance){
        distances.add(distance);
    }
    public ArrayList<Integer> getDistances() {
        return distances;
    }
    public int getLastValue(){
        if(values.size() > 1) {
            return values.get(values.size() - 1);
        }
        return 0;
    }
    public int getSecondLastValue(){
        if(values.size() > 2) {
            return values.get(values.size() - 2);
        }
        return 0;
    }
    public int getLastDistance(){
        if(distances.size() > 1){
            return distances.get(distances.size()-1);
        }
        return 0;
    }
    public int getSecondLastDistance(){
        if(distances.size() > 2){
            return distances.get(distances.size()-2);
        }
        return 0;
    }
}
