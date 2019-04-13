package sample;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Controller {

    //GUI properties
    private int BOARD_TILE_WIDTH = 4;
    private int BOARD_TILE_HEIGHT = 6;
    private double TILE_WIDTH = 270/BOARD_TILE_WIDTH;
    private double TILE_HEIGHT = 400/BOARD_TILE_HEIGHT;
    private Stage stage;

    //Core AI properties
    private Double learn = 0.5;
    private int era = 0;
    private Double err = 0.01;
    private Double[][] startingWeights = new Double[2][24];
    private Double[][] weights = new Double[2][24];
    private Double[][] inputArray = new Double[24][1];
    private Tile[][] tileArray = new Tile[BOARD_TILE_HEIGHT][BOARD_TILE_WIDTH];
    private Double[][] thresholdArray = new Double[2][1];

    //FXML properties
    @FXML private GridPane board;
    @FXML private TextField uczenie;
    @FXML private TextField epoki;
    @FXML private TextField prog;

    //Training arrays
    private Double[][] trainingZero = new Double[24][1];
    private Double[][] trainingOne = new Double[24][1];
    private Double[][] trainingTwo = new Double[24][1];
    private Double[][] binaryZero = new Double[2][1];
    private Double[][] binaryOne = new Double[2][1];
    private Double[][] binaryTwo = new Double[2][1];

    //Cały program ciężko jest zakomentować ponieważ starałem się go robić do uniwersalnego zastosowania.
    //Same nazwy metod bardzo dobrze opisują swoje zadanie.
    //Mimo wszystko postaram się nad każdą klasą dodac odpowiedni komentarz streszczający jej zadanie

    //Zainicjowanie macierzy trenujących po dwie dla każdej cyfry jaką ma odgadywać program
    //Jedna w formie zamalowanych kratek w programie i druga w formie reprezentacji binarnej
    public void setTrainingArrays(){
        binaryZero = new Double[][]{
                                        {0.0},
                                        {0.0},};
        binaryOne = new Double[][]{
                                        {0.0},
                                        {1.0},};
        binaryTwo = new Double[][]{
                                        {1.0},
                                        {0.0},};
        trainingZero = new Double[][]{
                                        {0.0},
                                        {1.0},
                                        {1.0},
                                        {0.0},
                                        {1.0},
                                        {0.0},
                                        {0.0},
                                        {1.0},
                                        {1.0},
                                        {0.0},
                                        {0.0},
                                        {1.0},
                                        {1.0},
                                        {0.0},
                                        {0.0},
                                        {1.0},
                                        {1.0},
                                        {0.0},
                                        {0.0},
                                        {1.0},
                                        {0.0},
                                        {1.0},
                                        {1.0},
                                        {0.0},
                                            };
        trainingOne = new Double[][]{
                                        {0.0},
                                        {0.0},
                                        {1.0},
                                        {0.0},
                                        {0.0},
                                        {0.0},
                                        {1.0},
                                        {0.0},
                                        {0.0},
                                        {0.0},
                                        {1.0},
                                        {0.0},
                                        {0.0},
                                        {0.0},
                                        {1.0},
                                        {0.0},
                                        {0.0},
                                        {0.0},
                                        {1.0},
                                        {0.0},
                                        {0.0},
                                        {0.0},
                                        {1.0},
                                        {0.0}};
        trainingTwo = new Double[][]{
                                        {1.0},
                                        {1.0},
                                        {1.0},
                                        {0.0},
                                        {0.0},
                                        {0.0},
                                        {0.0},
                                        {1.0},
                                        {0.0},
                                        {0.0},
                                        {1.0},
                                        {0.0},
                                        {0.0},
                                        {1.0},
                                        {0.0},
                                        {0.0},
                                        {1.0},
                                        {0.0},
                                        {0.0},
                                        {0.0},
                                        {1.0},
                                        {1.0},
                                        {1.0},
                                        {1.0}};
    }

    //Pobranie zaznaczonych elementów w GUI do macierzy
    public void setInputArray(){
        List<Double> tmpList = new ArrayList<>();
        for(int i = 0; i< BOARD_TILE_HEIGHT; i++){
            for(int j = 0; j< BOARD_TILE_WIDTH; j++){
                tmpList.add(tileArray[i][j].getState());
            }
        }

        for(int i=0 ; i<tmpList.size() ; i++){
            inputArray[i][0] = tmpList.get(i);
        }
    }

    //Losowanie wartości macierzy przy dostarczeniu tablicy
    //Wykorzystywane w losowaniu macierzy wag początkowych oraz wartości progowej
    public Double[][] randomizeMatrix(Double[][] matrix){
        Random r = new Random();
        for(int i=0 ; i<matrix.length ; i++){
            for(int j=0 ; j<matrix[0].length ; j++){
                matrix[i][j] = round(-1 + 2 * r.nextDouble(),4);
            }
        }
        return matrix;
    }

    //Prost funkcja zaokrąglająca podany wynik do miejsc po przeninku podanych jako drugi argument
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    //Funkcja dodająca siatke do naszego GUI
    public void setGrid(){
        for(int i = 0; i< BOARD_TILE_HEIGHT; i++){
            for(int j = 0; j< BOARD_TILE_WIDTH; j++){
                Tile tile = new Tile(new Pane(), 0.0);
                tileArray[i][j] = tile;
                tileArray[i][j].getPane().setStyle("-fx-background-color: lightgrey;" +
                                                   "-fx-border-color: black;" +
                                                   "-fx-border-width: 0.3px 0.3px 0.3px 0.3px;");
                tileArray[i][j].getPane().setPrefWidth(TILE_WIDTH);
                tileArray[i][j].getPane().setPrefHeight(TILE_HEIGHT);
                GridPane.setConstraints(tileArray[i][j].getPane(),j,i);
                board.getChildren().add(tileArray[i][j].getPane());
            }
        }
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }

    //Dwie funkcje dodające możliwość rysowania po siatce
    public void setMouseClickedListener(){
        board.addEventHandler(MouseEvent.MOUSE_CLICKED,
                me -> {
                    if(me.getX()>=0 && me.getX()<270 && me.getY()>=0 && me.getY()<400) {
                        if (me.getButton().equals(MouseButton.PRIMARY)) {
                            tileArray[(int) (me.getY() / TILE_WIDTH)][(int) (me.getX() / TILE_HEIGHT)].getPane().setStyle("-fx-background-color: red;" +
                                    "-fx-border-color: black;" +
                                    "-fx-border-width: 0.3px 0.3px 0.3px 0.3px;");
                            tileArray[(int) (me.getY() / TILE_WIDTH)][(int) (me.getX() / TILE_HEIGHT)].setState(1.0);
                        }
                        if (me.getButton().equals(MouseButton.SECONDARY)) {
                            tileArray[(int) (me.getY() / TILE_WIDTH)][(int) (me.getX() / TILE_HEIGHT)].getPane().setStyle("-fx-background-color: lightgrey;" +
                                    "-fx-border-color: black;" +
                                    "-fx-border-width: 0.3px 0.3px 0.3px 0.3px;");
                            tileArray[(int) (me.getY() / TILE_WIDTH)][(int) (me.getX() / TILE_HEIGHT)].setState(0.0);
                        }
                    }
                });
    }

    public void setMouseDraggedListener(){
        board.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                me -> {
                    if(me.getX()>=0 && me.getX()<270 && me.getY()>=0 && me.getY()<400){
                        if(me.isPrimaryButtonDown()) {
                            tileArray[(int) (me.getY() / TILE_WIDTH)][(int) (me.getX() / TILE_HEIGHT)].getPane().setStyle("-fx-background-color: red;" +
                                    "-fx-border-color: black;" +
                                    "-fx-border-width: 0.3px 0.3px 0.3px 0.3px;");
                            tileArray[(int) (me.getY() / TILE_WIDTH)][(int) (me.getX() / TILE_HEIGHT)].setState(1.0);
                        }
                        if(me.isSecondaryButtonDown()) {
                            tileArray[(int) (me.getY() / TILE_WIDTH)][(int) (me.getX() / TILE_HEIGHT)].getPane().setStyle("-fx-background-color: lightgrey;" +
                                    "-fx-border-color: black;" +
                                    "-fx-border-width: 0.3px 0.3px 0.3px 0.3px;");
                            tileArray[(int) (me.getY() / TILE_WIDTH)][(int) (me.getX() / TILE_HEIGHT)].setState(0.0);
                        }
                    }
                });
    }

    //Funkcja wypisująca na konsoli macierz. Obecnie nigdzie nie jest wykorzystywana.
    public <T> void showArray(T[][] array){
        System.out.print("[");
        for(int i=0 ; i<array.length ; i++){
            System.out.print("[");
            for(int j=0 ; j<array[0].length ; j++){
                if(!(j==array.length-1)){
                    System.out.print(array[i][j] + ",");
                }
                else{
                    System.out.print(array[i][j]);
                }
            }
            if(!(i==array.length-1)){
                System.out.print("],");
                System.out.println();
            }
            else {
                System.out.print("]");
            }
        }
        System.out.print("]");
        System.out.println();
        System.out.println();
    }

    //Funkcja pobierająca dane z pól tekstowych w GUI
    public void getDataFromTextBox(){
        if(!uczenie.getText().equals("") && !epoki.getText().equals("") && !prog.getText().equals("")) {
            learn = Double.parseDouble(uczenie.getText());
            era = Integer.parseInt(epoki.getText());
            err = Double.parseDouble(prog.getText());
        }
    }

    //Funkcja konwertująca macierz wynikową na zrozumiały dla zwykłego użytkownika język
    public void convertResult(Double[][] A){
        if(A[0][0] == 0.0 && A[1][0] == 0.0){
            System.out.println("Ta liczba to 0");
        } else if(A[0][0] == 0.0 && A[1][0] == 1.0){
            System.out.println("Ta liczba to 1");
        } else if(A[0][0] == 1.0 && A[1][0] == 0.0){
            System.out.println("Ta liczba to 2");
        } else if(A[0][0] == 1.0 && A[1][0] == 1.0){
            System.out.println("Nieznany wynik");
        }
    }

    //Funkcja mnożąca dwie macierze podane jako argumenty
    public Double[][] multiplyMatrix(Double[][] A, Double[][] B) {

        int aRows = A.length;
        int aColumns = A[0].length;
        int bRows = B.length;
        int bColumns = B[0].length;

        if (aColumns != bRows) {
            throw new IllegalArgumentException("A:Rows: " + aColumns + " did not match B:Columns " + bRows + ".");
        }

        Double[][] C = new Double[aRows][bColumns];
        for (int i = 0; i < aRows; i++) {
            for (int j = 0; j < bColumns; j++) {
                C[i][j] = 0.00000;
            }
        }

        for (int i = 0; i < aRows; i++) { // aRow
            for (int j = 0; j < bColumns; j++) { // bColumn
                for (int k = 0; k < aColumns; k++) { // aColumn
                    C[i][j] += A[i][k] * B[k][j];
                    C[i][j] = round(C[i][j] , 4);
                }
            }
        }

        return C;
    }

    //Funkcja odejmująca od siebie dwie macierze podane jako argumenty
    public Double[][] substractMatrix(Double[][] A, Double[][] B){
        Double[][] returnArray = new Double[A.length][A[0].length];

        for(int i=0 ; i<A.length ; i++){
            for (int j=0 ; j<A[0].length ; j++){
                returnArray[i][j] = round(A[i][j] - B[i][j],4);
            }
        }
        return returnArray;
    }

    //Funkcja obliczająca NET po podaniu macierzy W,X oraz B
    public Double[][] countNET(Double[][] W, Double[][] X, Double[][] B){
        Double[][] multiplyArray = multiplyMatrix(W,X);
        Double[][] returnArray = substractMatrix(multiplyArray,B);
        return returnArray;
    }

    //Funkcja zmieniająca elementy podanej macierzy na podstawie funkcji progowej unipolarnej
    public Double[][] getBinarySigmoidalFunction(Double[][] A){
        Double[][] returnArray = new Double[A.length][A[0].length];

        for(int i=0 ; i<A.length ; i++){
            for(int j=0 ; j<A[0].length ; j++){
                if(A[i][j] >= 0.0){
                    returnArray[i][j] = 1.0;
                }
                else{
                    returnArray[i][j] = 0.0;
                }
            }
        }

        return returnArray;
    }

    //Funkcja obliczająca błąd uczenia
    public Double countError(double d1,double y1,double d2, double y2){
        Double error = null;

        error = 0.5 * (Math.pow((d1-y1),2.0) + Math.pow((d2-y2),2.0));

        return error;
    }

    //Funkcja przeprowadzająca uczenie na podstawie podanej macierzy uczącej
    public void train( Double learn, Double[][] result,Double[][] trainingArray,Double[][] binaryResult, Double error){
        Double delB = null;
        Double[][] delB1 = null;
        Double tmpError = countError(binaryResult[0][0], result[0][0],binaryResult[1][0], result[1][0]);
        for(int i=0 ; i<result.length ; i++){
            if(binaryResult[i][0].equals(result[i][0]) || tmpError<error){

            }else {
                delB = learn * (binaryResult[i][0] - result[i][0]);
                delB1 = new Double[1][trainingArray.length];

                thresholdArray[i][0] = thresholdArray[i][0] + delB;

                for(int j=0; j<delB1[0].length ; j++){
                    delB1[0][j] = trainingArray[j][0] * delB;
                }
                for(int j=0; j<weights[i].length ; j++){
                    weights[i][j] = round((weights[i][j] + delB1[0][j]),4);
                }

            }
        }
    }

    //Funkcja nałożona na przycisk check
    //Pobiera dane z siatki a następnie sprawdza na podstawie sieci neuronowej jaka to cyfra
    public void checkClick(MouseEvent mouseEvent) {
        setInputArray();
        Double[][] net = countNET(weights,inputArray,thresholdArray);
        Double[][] sigBiFun = getBinarySigmoidalFunction(net);
        convertResult(sigBiFun);
    }

    //Funkcja nałożona na przycisk train
    //Pobiera dane z pól tekstowych
    //Losuje macierze wag i wartości progowych
    //Następnie na podstawie podanych danych przeprowadza uczenie dla kolejnych cyfr
    //Jedna epoka uczenia to tak na prawde 3 uczenia. Po jednym dla każdej cyfry
    public  void trainClick(MouseEvent mouseEvent){
        getDataFromTextBox();
        startingWeights = randomizeMatrix(startingWeights);
        thresholdArray = randomizeMatrix(thresholdArray);
        weights = startingWeights;
        Double[][] net;
        Double[][] sigBiFun;

        for(int i=0; i<era ; i++){
            net = countNET(weights,trainingZero,thresholdArray);
            sigBiFun = getBinarySigmoidalFunction(net);
            train(learn,sigBiFun,trainingZero,binaryZero,err);

            net = countNET(weights,trainingOne,thresholdArray);
            sigBiFun = getBinarySigmoidalFunction(net);
            train(learn,sigBiFun,trainingOne,binaryOne,err);

            net = countNET(weights,trainingTwo,thresholdArray);
            sigBiFun = getBinarySigmoidalFunction(net);
            train(learn,sigBiFun,trainingTwo,binaryTwo,err);
        }
    }
}
