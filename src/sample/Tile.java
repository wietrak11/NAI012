package sample;

import javafx.scene.layout.Pane;

public class Tile {

    private Pane pane;
    private Double state;

    public Tile(Pane pane,Double state){
        this.pane = pane;
        this.state = state;
    }

    public Pane getPane(){ return pane; }

    public void setState(Double state) { this.state=state; }

    public Double getState(){ return state;}

    public String toString(){
        return Integer.toString(state.intValue());
    }
}
