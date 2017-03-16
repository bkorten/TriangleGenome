package application;

import java.awt.Color;
import javafx.embed.swing.SwingFXUtils;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;

import javafx.scene.image.Image;
import java.awt.Polygon;

public class Tribe extends Thread
{
  static int generations = 0;
  static int genPerSec = 0;
  static int hillClimbChild = 0;
  static Random rand = new Random();

  ArrayList<Polygon> genomeTestTriangles = new ArrayList(GENOME_SIZE);
  ArrayList<Color> genomeTestColors = new ArrayList(GENOME_SIZE);

  BufferedImage tmpGenomeImage;
  public static Image testfximage;

  public static int TotalHillClimbChildern;
  public static int TotalCrossOverChildern;

  final static int GENOME_SIZE = 200;

  short[][] population;

  private final int X1 = 0;
  private final int Y1 = 1;
  private final int X2 = 2;
  private final int Y2 = 3;
  private final int X3 = 4;
  private final int Y3 = 5;

  private final int RED_INDEX = 6;
  private final int BLUE_INDEX = 7;
  private final int GREEN_INDEX = 8;
  private final int ALPHA_INDEX = 9;
  int populationSize;

  int pictureWidth, pictureHeight;
  int fittestIndex;

  static int numberOfIndivualSolutions;
  int CHROMO_SIZE = 10;

  int[] fittestIdices = new int[10];
  float[] initfittness;
  double fittness;

  ImageAnalyser IA;
  Timer timer = new Timer();

  public Tribe(Image testfximage)
  {
    this.testfximage = testfximage;
    this.pictureHeight = (int) testfximage.getHeight();
    this.pictureWidth = (int) testfximage.getWidth();

    numberOfIndivualSolutions = 20;

    populationSize = numberOfIndivualSolutions * GENOME_SIZE;
    initfittness = new float[numberOfIndivualSolutions];

    initTribe();
    initFitness();
  }

  boolean running = true;

  @Override
  public void run()
  {
    int numberOfHillClimbs = 0;
    while (running)
    {

      if (GUI.loop)
      {
        hillClimb();
        numberOfHillClimbs++;
        adaptiveHillClimb();
        TotalHillClimbChildern++;
        if (numberOfHillClimbs == 1000)
        {
          numberOfHillClimbs++;
          
          crossOver();
          TotalCrossOverChildern++;
          numberOfHillClimbs = 0;
        }
      } else
        try
        {
          this.sleep(1);
        } catch (InterruptedException e)
        {
        }
    }
  }

  private boolean keep = false;
  private short geneChange;
  private short triangle;

  void hillClimb()
  {
    boolean improved = false;
    fittness = getFitness(0);

    geneChange = (short) (rand.nextInt(10));
    triangle = (short) (rand.nextInt(200));

    short changed = 0;
    short temp = population[triangle][geneChange];

    if (geneChange < 6)
    {
      if (geneChange % 2 == 0)
      {
        changed = ((short) (rand.nextInt(pictureWidth + 1)));
        population[triangle][geneChange] = changed;
      } else
      {
        changed = ((short) (rand.nextInt(pictureHeight + 1)));
        population[triangle][geneChange] = changed;
      }
    } else if (geneChange > 5 && geneChange < 9)
    {
      changed = population[triangle][geneChange];
      population[triangle][geneChange] = changed;
    } else if (geneChange == 9)
    {
      changed = (short) (rand.nextInt(100));
      population[triangle][geneChange] = changed;
    }

    double newFitness = getFitness(0);
    // System.out.println(fittness + " " + newFitness);

    if (newFitness > fittness)
    {
      // temp = population[triangle][geneChange];
      // improved = true;
      // System.out.println(newFitness + " " + fittness);
      fittness = newFitness;
    } else
      population[triangle][geneChange] = temp;
    /*
     * while (improved) { // System.out.println(fittness); temp = population[triangle][geneChange]; fittness = getFitness(0);
     * 
     * changed += changed / 10; if (geneChange < 6) { if (geneChange % 2 == 0) { changed = ((short) (rand.nextInt(pictureWidth + 1))); population[triangle][geneChange] = changed; } else { changed = ((short) (rand.nextInt(pictureHeight + 1))); population[triangle][geneChange] = changed; } } else if
     * (geneChange > 5 && geneChange < 9) { changed = population[triangle][geneChange]; population[triangle][geneChange] = changed; } else if (geneChange == 9) { changed = (short) (rand.nextInt(100)); population[triangle][geneChange] = changed; } population[triangle][geneChange] = changed;
     * newFitness = getFitness(0);
     * 
     * if (newFitness > fittness) { fittness = newFitness; // System.out.println(changed); improved = true; } else { population[triangle][geneChange] = temp; improved = false; fittness = getFitness(0); } }
     */
  }

  private void adaptiveHillClimb()
  {
    int geneChange = (rand.nextInt(10));
    int triangle = (fittestIndex + (rand.nextInt(200)));
    boolean improved = false;

    short changeFactor = 1;
    short firstVal = population[triangle][geneChange];

    short upperLimit = 0;
    short lowerLimit = 0;

    short changed = firstVal;
    short oldVal = firstVal;

    double currFittness = getFitness(fittestIndex);
    double newFittness;

    switch (geneChange)
    {
    case X1:
    case X2:
    case X3:
      upperLimit = (short) pictureWidth;
      break;

    case Y1:
    case Y2:
    case Y3:
      upperLimit = (short) pictureHeight;
      break;

    case RED_INDEX:
    case GREEN_INDEX:
    case BLUE_INDEX:
      upperLimit = 255;
      break;

    case ALPHA_INDEX:
      upperLimit = 50;
      break;
    }
    // System.out.println(upperLimit);
    while (true)
    {
      if ((changed + changeFactor) < upperLimit)
      {
        changed += changeFactor;
        population[triangle][geneChange] = changed;
        newFittness = getFitness(fittestIndex);
        if (newFittness > currFittness)
        {
          changeFactor *= 2;
          currFittness = newFittness;
          oldVal = changed;
          TotalHillClimbChildern++;
        } else
        {
          population[triangle][geneChange] = oldVal;
          // System.out.println(oldVal+"Increased");
          break;
        }
      }

      if ((changed - changeFactor) > lowerLimit)
      {
        changed -= changeFactor;
        population[triangle][geneChange] = changed;
        newFittness = getFitness(fittestIndex);
        if (newFittness > currFittness)
        {
          changeFactor *= 2;
          currFittness = newFittness;
          oldVal = changed;
          TotalHillClimbChildern++;
        } else
        {
          population[triangle][geneChange] = oldVal;
          // System.out.println(oldVal+"decreased");
          break;
        }
      }
      break;
    }

  }

  private void initTribe()
  {

    population = new short[populationSize][10];

    // 0- x1
    // 1- y1
    // 2- x2
    // 3- y2
    // 4- x3
    // 5- y3
    // 6- red
    // 7- green
    // 8- blue
    // 9- opacity

    for (int i = 0; i < populationSize; i++)
    {
      short x = ((short) (rand.nextInt(pictureWidth-1)));
      short y = ((short) (rand.nextInt(pictureHeight-1)));
      population[i][0] = x;
      // System.out.println(population[i][0]);
      population[i][1] = y;

      // (x2,y2)
      population[i][2] = (short) (x+1);
      population[i][3] = y;

      // (x3,y3)
      population[i][4] = x;
      population[i][5] = (short) (y+1);

      // (r,g,b)
      population[i][6] = (short) (rand.nextInt(256));
      population[i][7] = (short) (rand.nextInt(256));
      population[i][8] = (short) (rand.nextInt(256));

      // opacity
      population[i][9] = (short) (rand.nextInt(100));

    }
    IA = new ImageAnalyser();
  }

  private void initFitness()
  {
    int index = 1;
    initfittness[0] = (float) getFitness(0);
    fittestIndex = 0;
    float tmpFittest = (float) getFitness(0);
    float tmpFit;
    for (int i = 200; i < populationSize; i += GENOME_SIZE)
    {
      tmpFit = (float) getFitness(i);
      initfittness[index] = tmpFit;
      if (tmpFit > tmpFittest)
      {
        tmpFittest = tmpFit;
        fittestIndex = i;
        // System.out.println("fittest Index " + fittestIndex + " fitness: " + tmpFittest);

      }
      index++;

    }
    // System.out.println("fittest Index " + fittestIndex + " fitness: " + tmpFittest);
  }

  private double getFitness(int genomeIndex)
  {
    genomeTestTriangles.clear();
    genomeTestColors.clear();

    for (int i = genomeIndex; i < genomeIndex + GENOME_SIZE; i++)
    {

      int xPoints[] =
      { (int) population[i][0],

          (int) population[i][2],

          (int) population[i][4] };

      int yPoints[] =
      { (int) population[i][1],

          (int) population[i][3],

          (int) population[i][5] };

      float red = ((float) population[i][6]) / 255;
      float blue = ((float) population[i][7]) / 255;
      float green = ((float) population[i][8]) / 255;
      float alpha = ((float) population[i][9]) / 100;

      Polygon tmpPolygon = new Polygon(xPoints, yPoints, 3);
      // System.out.println(" "+red+" "+blue+" "+green+" "+alpha);
      Color tmpColor = new Color(red, blue, green, alpha);
      genomeTestTriangles.add(tmpPolygon);
      genomeTestColors.add(tmpColor);
    }

    IA.setGenomeColorsTriangles(genomeTestColors, genomeTestTriangles);
    IA.initGenoVisual();
    // System.out.println(IA.compareImages()+"");
    return IA.compareImages();

  }

  private double getFitness(short child[][])
  {
    genomeTestTriangles.clear();
    genomeTestColors.clear();

    for (int i = 0; i < GENOME_SIZE; i++)
    {

      int xPoints[] =
      { (int) population[i][0],

          (int) population[i][2],

          (int) population[i][4] };

      int yPoints[] =
      { (int) population[i][1],

          (int) population[i][3],

          (int) population[i][5] };

      float red = ((float) population[i][6]) / 255;
      float blue = ((float) population[i][7]) / 255;
      float green = ((float) population[i][8]) / 255;
      float alpha = ((float) population[i][9]) / 100;

      Polygon tmpPolygon = new Polygon(xPoints, yPoints, 3);
      // System.out.println(" "+red+" "+blue+" "+green+" "+alpha);
      Color tmpColor = new Color(red, blue, green, alpha);
      genomeTestTriangles.add(tmpPolygon);
      genomeTestColors.add(tmpColor);
    }

    IA.setGenomeColorsTriangles(genomeTestColors, genomeTestTriangles);
    IA.initGenoVisual();

    return IA.compareImages();

  }

  private void addGenome(int startIndex, short[][] newGenome)
  {
    int newGenomeIndex = 0;
    for (int i = startIndex; i < GENOME_SIZE + startIndex; i++)
    {
      for (int j = 0; j < CHROMO_SIZE; j++)
      {
        population[i][j] = newGenome[newGenomeIndex][j];
      }
      newGenomeIndex++;
    }
  }

  private void crossOver()
  {
    int coin = rand.nextInt(2);
    int parent2Index = rand.nextInt(numberOfIndivualSolutions) * GENOME_SIZE;
    if (coin == 0)
    {

      int crossOverPoint = 100;
      singleCrossOver(crossOverPoint, fittestIndex, parent2Index);

    } else
    {
      int crossOverPoint1 = rand.nextInt(GENOME_SIZE - (GENOME_SIZE / 2));
      int crossOverPoint2 = (GENOME_SIZE - rand.nextInt(GENOME_SIZE / 2)) + 1;

      doublePointCrossOver(crossOverPoint1, crossOverPoint2, fittestIndex, parent2Index);
    }

  }

  // genome from inside thread
  private int singleCrossOver(int crossOverPoint, int parent1Index, int parent2Index)
  {

    short[][] child = new short[200][10];
    int childGenomeIndex = 0;
    for (int i = parent1Index; i < parent1Index + crossOverPoint; i++)
    {
      for (int j = 0; j < CHROMO_SIZE; j++)
      {
        child[childGenomeIndex][j] = population[i][j];
      }
      childGenomeIndex++;
    }

    for (int i = parent2Index + crossOverPoint; i < GENOME_SIZE + parent2Index; i++)
    {
      for (int j = 0; j < CHROMO_SIZE; j++)
      {
        child[childGenomeIndex][j] = population[i][j];
      }
      childGenomeIndex++;

    }
    double childFitness = getFitness(child);
    double parent1Fitness = getFitness(parent1Index);
    double parent2Fitness = getFitness(parent2Index);
    if (childFitness > parent1Fitness)
    {
      addGenome(parent1Index, child);
      return 0;
    }
    if (childFitness > parent2Fitness)
    {
      addGenome(parent2Index, child);
      return 0;
    }

    return 1;

  }

  // genome for outside thread;
  private void singleCrossOver(int crossOverPoint, int parent1Index, short parent2[][])
  {

    short[][] child = new short[200][10];
    int childGenomeIndex = 0;
    for (int i = 0; i < parent1Index + crossOverPoint; i++)
    {
      for (int j = 0; j < CHROMO_SIZE; j++)
      {
        child[childGenomeIndex][j] = population[i][j];
      }
      childGenomeIndex++;
    }

    for (int i = crossOverPoint; i < GENOME_SIZE; i++)
    {
      for (int j = 0; j < CHROMO_SIZE; j++)
      {
        child[childGenomeIndex][j] = parent2[i][j];
      }
      childGenomeIndex++;

    }
    double childFitness = getFitness(child);
    double parent1Fitness = getFitness(parent1Index);
    double parent2Fitness = getFitness(parent2);

  }

  // with in thread
  private int doublePointCrossOver(int crossOverPoint1, int crossOverPoint2, int parent1Index, int parent2Index)
  {
    short child[][] = new short[200][10];
    int childGenomeIndex = 0;

    for (int i = parent1Index; i < parent1Index + crossOverPoint1; i++)
    {
      for (int j = 0; j < CHROMO_SIZE; j++)
      {
        child[childGenomeIndex][j] = population[i][j];
      }
      childGenomeIndex++;
    }

    for (int i = parent2Index + crossOverPoint1; i < crossOverPoint2 + parent2Index; i++)
    {
      for (int j = 0; j < CHROMO_SIZE; j++)
      {
        child[childGenomeIndex][j] = population[i][j];
      }
      childGenomeIndex++;

    }

    for (int i = parent1Index + crossOverPoint2; i < GENOME_SIZE + parent1Index; i++)
    {
      for (int j = 0; j < CHROMO_SIZE; j++)
      {
        child[childGenomeIndex][j] = population[i][j];
      }
      childGenomeIndex++;

    }
    double childFitness = getFitness(child);
    double parent1Fitness = getFitness(parent1Index);
    double parent2Fitness = getFitness(parent2Index);
    if (childFitness > parent1Fitness)
    {
      addGenome(parent1Index, child);
      return 0;
    }
    if (childFitness > parent2Fitness)
    {
      addGenome(parent2Index, child);
      return 0;
    }

    return 1;

  }

  class ImageAnalyser
  {

    BufferedImage GenomeVisual, TestBufferedImage;

    ArrayList<Color> genomeColors = new ArrayList<Color>();

    ArrayList<Polygon> genomeTriangles = new ArrayList<Polygon>();

    Graphics gc;

    int comparisonResults;

    private ImageAnalyser()
    {
      TestBufferedImage = new BufferedImage((int) testfximage.getWidth(), (int) testfximage.getHeight(), BufferedImage.TYPE_INT_ARGB);
      SwingFXUtils.fromFXImage(testfximage, TestBufferedImage);
      GenomeVisual = new BufferedImage(TestBufferedImage.getWidth(), TestBufferedImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
    }

    private void setGenomeColorsTriangles(ArrayList<Color> genomeColors, ArrayList<Polygon> genomeTriangles)
    {
      this.genomeColors = genomeColors;
      this.genomeTriangles = genomeTriangles;
    }

    private void initGenoVisual()
    {

      gc = GenomeVisual.getGraphics();
      gc.setColor(Color.BLACK);
      gc.fillRect(0, 0, GenomeVisual.getWidth(), GenomeVisual.getHeight());

      for (int i = 0; i < GENOME_SIZE; i++)
      {
        gc.setColor(genomeColors.get(i));
        Polygon tmpP = genomeTriangles.get(i);
        gc.fillPolygon(tmpP);
      }

    }

    private double compareImages()
    {
      double results = 0;

      for (int i = 0; i < TestBufferedImage.getWidth(); i++)
      {
        for (int j = 0; j < TestBufferedImage.getHeight(); j++)
        {
          int testRGB = TestBufferedImage.getRGB(i, j);
          int genomeRGB = GenomeVisual.getRGB(i, j);
          int r1 = (testRGB >> 16);
          int g1 = (testRGB >> 8) & 0xff;
          int b1 = (testRGB) & 0xff;

          int r2 = (genomeRGB >> 16);
          int g2 = (genomeRGB >> 8) & 0xff;
          int b2 = (genomeRGB) & 0xff;
          results += (Math.abs(r1 - r2) + Math.abs(g1 - g2) + Math.abs(b1 - b2)) / 3.0 / 255.0;
          // System.out.println(results);
        }

      }
      generations++;
      //if (genPerSec > 0)
        genPerSec++;
      // System.out.println(results);
      return 1 / results;
    }

  }

}
