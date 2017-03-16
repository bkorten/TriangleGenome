package application;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

public class GUI extends Application
{ // loop variables
  static boolean loop = true;

  // whole window
  AnchorPane root;
  VBox vbox;

  // Menu buttons up top
  HBox buttons;
  MenuButton imgSelect;
  MenuItem mona;
  MenuItem poppy;
  MenuItem wave;
  Button play;
  Button next;
  TextField numTribes;
  Slider tribeSelect;

  // image/canvas
  HBox images;
  ImageView img;
  Canvas canvas;
  GraphicsContext gc;

  // triangle selector
  Slider triangleSelect;

  // info stuff
  HBox info;
  Label totalGen;
  Label genPerSec;
  Label fitness;
  Label population;
  Label hillClimb;
  Label crossOver;

  // images
  Image MonaLisa = new Image("file:Resources/mona-lisa-cropted-512x413.png");
  Image PoppyField = new Image("file:Resources/poppyfields-512x384.png");
  Image GreatWave = new Image("file:Resources/the_great_wave_off_kanagawa-512x352.png");
  Image testImage = new Image("file:Resources/test.png");
  Image currentImage = MonaLisa;

  Tribe[] tribes =
  { new Tribe(MonaLisa) };

  int tribeAmount = 0;

  @Override
  public void start(Stage primaryStage) throws Exception
  {
    tribes[0].start();
    AnchorPane root = new AnchorPane();
    vbox = new VBox(15);

    // create menu
    buttons = new HBox(5);
    imgSelect = new MenuButton();
    imgSelect.setText("Image");
    MenuItem ML = new MenuItem("Mona Lisa");
    ML.setOnAction(e ->
    {

      images.getChildren().remove(img);
      images.getChildren().remove(canvas);
      currentImage = MonaLisa;
      img = new ImageView(MonaLisa);
      canvas = new Canvas(MonaLisa.getWidth(), MonaLisa.getHeight());
      gc = canvas.getGraphicsContext2D();
      gc.setFill(Color.BLACK);
      gc.fillRect(0, 0, MonaLisa.getWidth(), MonaLisa.getHeight());
      images.getChildren().addAll(img, canvas);
      for (int i = 0; i < Integer.parseInt(numTribes.getText()); i++)
      {
        tribes[i].running = false;

      }

      tribes = new Tribe[Integer.parseInt(numTribes.getText())];

      for (int i = 0; i < Integer.parseInt(numTribes.getText()); i++)
      {
        tribes[i] = new Tribe(currentImage);
        tribes[i].start();

      }

    });
    MenuItem PF = new MenuItem("Poppy Fields");
    PF.setOnAction(e ->
    {
      images.getChildren().remove(img);
      images.getChildren().remove(canvas);
      currentImage = PoppyField;
      img = new ImageView(PoppyField);
      canvas = new Canvas(currentImage.getWidth(), currentImage.getHeight());
      gc = canvas.getGraphicsContext2D();
      gc.setFill(Color.BLACK);
      gc.fillRect(0, 0, currentImage.getWidth(), currentImage.getHeight());
      images.getChildren().addAll(img, canvas);
      for (int i = 0; i < Integer.parseInt(numTribes.getText()); i++)
      {
        tribes[i].running = false;

      }

      tribes = new Tribe[Integer.parseInt(numTribes.getText())];

      for (int i = 0; i < Integer.parseInt(numTribes.getText()); i++)
      {
        tribes[i] = new Tribe(currentImage);
        tribes[i].start();

      }
    });
    MenuItem GW = new MenuItem("The Great Wave");
    GW.setOnAction(e ->
    {
      images.getChildren().remove(img);
      images.getChildren().remove(canvas);
      currentImage = GreatWave;
      img = new ImageView(currentImage);
      canvas = new Canvas(currentImage.getWidth(), currentImage.getHeight());
      gc = canvas.getGraphicsContext2D();
      gc.setFill(Color.BLACK);
      gc.fillRect(0, 0, currentImage.getWidth(), currentImage.getHeight());
      images.getChildren().addAll(img, canvas);
      for (int i = 0; i < Integer.parseInt(numTribes.getText()); i++)
      {
        tribes[i].running = false;

      }

      tribes = new Tribe[Integer.parseInt(numTribes.getText())];

      for (int i = 0; i < Integer.parseInt(numTribes.getText()); i++)
      {
        tribes[i] = new Tribe(currentImage);
        tribes[i].start();

      }
    });

    MenuItem test = new MenuItem("test");
    test.setOnAction(e ->
    {

      images.getChildren().remove(img);
      images.getChildren().remove(canvas);
      currentImage = testImage;
      img = new ImageView(testImage);
      canvas = new Canvas(testImage.getWidth(), testImage.getHeight());
      gc = canvas.getGraphicsContext2D();
      gc.setFill(Color.BLACK);
      gc.fillRect(0, 0, testImage.getWidth(), testImage.getHeight());
      images.getChildren().addAll(img, canvas);
      for (int i = 0; i < Integer.parseInt(numTribes.getText()); i++)
      {
        tribes[i].running = false;

      }

      tribes = new Tribe[Integer.parseInt(numTribes.getText())];

      for (int i = 0; i < Integer.parseInt(numTribes.getText()); i++)
      {
        tribes[i] = new Tribe(currentImage);
        tribes[i].start();

      }

    });
    imgSelect.getItems().addAll(ML, PF, GW, test);
    play = new Button("Play");
    play.setOnAction(e ->
    {
      loop = !loop;
      if (loop)
        play.setText("Pause");
      else
        play.setText("Play");

    });

    next = new Button("Next");

    next.setOnAction(e_ ->
    {
      nextView();
    });
    numTribes = new TextField();
    numTribes.setText("1");

    tribeSelect = new Slider(1, 1, 0);
    tribeSelect.setMinWidth(400);
    tribeSelect.setShowTickLabels(true);
    tribeSelect.setShowTickMarks(true);
    tribeSelect.setMajorTickUnit(1.0);
    tribeSelect.setMinorTickCount(0);
    tribeSelect.setSnapToTicks(true);
    buttons.getChildren().addAll(imgSelect, play, next, numTribes, tribeSelect);

    // create image views
    images = new HBox(5);
    img = new ImageView(currentImage);
    canvas = new Canvas(currentImage.getWidth(), currentImage.getHeight());

    gc = canvas.getGraphicsContext2D();
    gc.setFill(Color.BLACK);
    gc.fillRect(0, 0, currentImage.getWidth(), currentImage.getHeight());
    images.getChildren().addAll(img, canvas);

    // triangle slider
    triangleSelect = new Slider(0, 200, 200);

    vbox.getChildren().addAll(buttons, images, triangleSelect);
    root.getChildren().add(vbox);

    primaryStage.setTitle("Triangle Genome");
    primaryStage.setScene(new Scene(root, currentImage.getWidth() * 2 + 5, 700));

    primaryStage.show();
    numTribes.setOnAction(e ->
    {
      try
      {
        buttons.getChildren().remove(tribeSelect);

        tribeSelect = new Slider(1, Integer.parseInt(numTribes.getText()), 0);
        tribes = new Tribe[Integer.parseInt(numTribes.getText())];

        for (int i = 0; i < Integer.parseInt(numTribes.getText()); i++)
        {
          tribes[i] = new Tribe(currentImage);
          tribes[i].start();

        }

        tribeSelect.setMinWidth(400);
        tribeSelect.setShowTickLabels(true);
        tribeSelect.setShowTickMarks(true);
        tribeSelect.setMajorTickUnit(1.0);
        tribeSelect.setMinorTickCount(0);
        tribeSelect.setSnapToTicks(true);

        buttons.getChildren().add(tribeSelect);

      } catch (Exception e1)
      {
        System.out.println("too big");
      }

    });
    primaryStage.setOnCloseRequest(e ->
    {
      System.exit(0);
    });
    info = new HBox(5);
    totalGen = new Label("Total Generations: " + Tribe.generations);
    genPerSec = new Label("Generations this Second: "+ Tribe.genPerSec);
    population = new Label("Population: " + tribes.length*Tribe.numberOfIndivualSolutions);
    hillClimb = new Label("Total hill Climb Children: " + Tribe.TotalHillClimbChildern);
    crossOver = new Label("Total cross over Children: " + Tribe.TotalCrossOverChildern);
    
    info.getChildren().addAll(totalGen, genPerSec, population,hillClimb,crossOver);
    vbox.getChildren().addAll(info);
    animationLoop();
  }

  public long currentTime;
  public long startTime = System.currentTimeMillis();
  private void animationLoop()
  {
    new AnimationTimer()
    {  
      public void handle(long currentNanoTime)
      { population.setText("Population: " + tribes.length*Tribe.numberOfIndivualSolutions);
        hillClimb.setText("Total hill Climb Children: " + Tribe.TotalHillClimbChildern);
        crossOver.setText("Total cross over Children: " + Tribe.TotalCrossOverChildern);
        if (loop)
          nextView();
        currentTime = System.currentTimeMillis();
        if(currentTime - startTime >= 1000){
          totalGen.setText("Total Generations: " + Tribe.generations);
          genPerSec.setText("Generations this Second: "+ Tribe.genPerSec);
          
          Tribe.genPerSec = 0;
          startTime = System.currentTimeMillis();
        }
      }

    }.start();
  }

  private void nextView()
  {
    if (loop)
    {
      gc.setFill(Color.BLACK);
      gc.fillRect(0, 0, currentImage.getWidth(), currentImage.getHeight());
      // change to fitest index for selected tribe
      // i+200 < triangleSelect.getValue()
      for (int i = 0; i < triangleSelect.getValue(); i++)
      {
        Color c = Color.rgb((int) tribes[(int) tribeSelect.getValue() - 1].population[i][6], (int) tribes[(int) tribeSelect.getValue() - 1].population[i][7], (int) tribes[(int) tribeSelect.getValue() - 1].population[i][8], (double) tribes[(int) tribeSelect.getValue() - 1].population[i][9] / 100);
        // System.out.println(new double[]
        // { tempTribe.population[i][0], tempTribe.population[i][2], tempTribe.population[i][4] });
        gc.setFill(c);
        gc.fillPolygon(new double[]
        { (double) (tribes[tribeAmount].population[i][0]), (double) (tribes[tribeAmount].population[i][2]), (double) (tribes[tribeAmount].population[i][4]) }, new double[]
        { (double) (tribes[tribeAmount].population[i][1]), (double) (tribes[tribeAmount].population[i][3]), (double) (tribes[tribeAmount].population[i][5]) }, 3);
      }
    }
  }

  public static void main(String[] args)
  {
    launch(args);
  }

}