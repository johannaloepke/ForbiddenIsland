// Forbidden Island Game
// Johanna Loepke

import java.util.ArrayList;
import java.util.Iterator;
import javalib.impworld.*;
import javalib.worldimages.*;
import java.awt.Color;

// represents an iterator through an IList
class IListIterator<T> implements Iterator<T> {
  IList<T> src;
  IListIterator(IList<T> src) {
    this.src = src;
  }

  //checks if there is at least one more value
  public boolean hasNext() {
    return this.src.isCons();
  }

  //return next item
  //EFFECT: advances the iterator
  public T next() {
    Cons<T> consItems = this.src.asCons();
    T answer = consItems.first;
    this.src = consItems.rest;
    return answer;
  }

  // method not used
  public void remove() {
    //not implemented
  }
}

//represents a predicate
interface IPred<T> {
  boolean apply(T t);
}

// checks whether cell is land
class IsLand implements IPred<Cell> {
  public boolean apply(Cell c) {
    return !c.isFlooded;
  }
}

//represents a generic list
interface IList<T> extends Iterable<T> {
  // filters this list according to the given pred
  IList<T> filter(IPred<T> pred);

  // converts this list to a non-empty list
  Cons<T> asCons();

  // checks if this list is non-empty
  boolean isCons();

  // appends this list with the given list
  IList<T> append(IList<T> that);

  // returns the length of this list
  int length();

  // iterates over this list
  Iterator<T> iterator();

  // returns a random element in this list
  T getCellAt(int n);
}

//represents an empty generic list
class MT<T> implements IList<T> {
  MT() {
    // this is empty because there are no fields
  }

  // filters this empty list according to the given pred
  public IList<T> filter(IPred<T> pred) { return this; }

  // converts this empty list to non-empty
  public Cons<T> asCons() {
    throw new RuntimeException("mt cannot be as cons");
  }

  // checks if this empty list is non-empty
  public boolean isCons() {
    return false;
  }

  // appends this empty list with the given list
  public IList<T> append(IList<T> that) { return that; }

  // returns the length of this empty list
  public int length() { return 0; }

  // iterates over this empty list
  public Iterator<T> iterator() {
    return new IListIterator<T>(this);
  }

  // gets a random element in this empty list
  public T getCellAt(int n) {
    throw new RuntimeException("nothing to return");
  }
}

//represents a non-empty generic list
class Cons<T> implements IList<T> {
  T first;
  IList<T> rest;
  Cons(T first, IList<T> rest) {
    this.first = first;
    this.rest = rest;
  }

  // filters this non empty list according to the given pred
  public IList<T> filter(IPred<T> pred) {
    if (pred.apply(this.first)) {
      return new Cons<T>(this.first, this.rest.filter(pred));
    }
    else {
      return this.rest.filter(pred);
    }
  }

  // converts this non-empty list to non-empty
  public Cons<T> asCons() {
    return this;
  }

  // checks if this non-empty list is non-empty
  public boolean isCons() {
    return true;
  }

  // appends this non empty list with the given list
  public IList<T> append(IList<T> that) {
    return new Cons<T>(this.first, this.rest.append(that));
  }

  // returns the length of this non empty list
  public int length() { return 1 + this.rest.length(); }

  // iterates over this non-empty list
  public Iterator<T> iterator() {
    return new IListIterator<T>(this);
  }

  // gets a random element in this non-empty list
  public T getCellAt(int n) {
    if (n == 0) {
      return this.first;
    }
    else {
      return this.rest.getCellAt(n - 1);
    }
  }
}

//Represents a single square of the game area
class Cell {
  // represents absolute height of this cell, in feet
  double height;
  // In logical coordinates, with the origin at the top-left corner of the
  // screen
  int x;
  int y;
  // the four adjacent cells to this one
  Cell left;
  Cell top;
  Cell right;
  Cell bottom;
  // reports whether this cell is flooded or not
  boolean isFlooded;

  Cell(double height, int x, int y, Cell left, Cell top, Cell right,
       Cell bottom, boolean isFlooded) {
    this.height = height;
    this.x = x;
    this.y = y;
    this.left = left;
    this.top = top;
    this.right = right;
    this.bottom = bottom;
    this.isFlooded = isFlooded;
  }

  // sets the left of this cell to the given cell
  public void setLeft(Cell c) {
    this.left = c;
  }

  // sets the top of this cell to the given cell
  public void setTop(Cell c) {
    this.top = c;
  }

  // sets the right of this cell to the given cell
  public void setRight(Cell c) {
    this.right = c;
  }

  // sets the bottom of this cell to the given cell
  public void setBottom(Cell c) {
    this.bottom = c;
  }

  // returns an image of this cell
  public WorldImage image(int waterHeight) {
    int color = (int) ((this.height - waterHeight) *
            (255 / ForbiddenIslandWorld.HIGH_HEIGHT));

    if ((this.height - waterHeight) < 0 && !this.isFlooded) {
      return new RectangleImage(ForbiddenIslandWorld.CELL_SIZE,
              ForbiddenIslandWorld.CELL_SIZE, OutlineMode.SOLID,
              new Color((int) Math.max(0, (Math.min(120 + -color,
                      255))), 0, 0));
    }
    if ((this.height - waterHeight) < 0) {
      return new RectangleImage(ForbiddenIslandWorld.CELL_SIZE,
              ForbiddenIslandWorld.CELL_SIZE, OutlineMode.SOLID,
              new Color(0, 0, (int) Math.max(0, (255 + color))));
    }
    else {
      return new RectangleImage(ForbiddenIslandWorld.CELL_SIZE,
              ForbiddenIslandWorld.CELL_SIZE, OutlineMode.SOLID,
              new Color((int) Math.min(255, color), 255,
                      Math.min(255, color)));
    }
  }

  // floods a particular cell
  // EFFECT: changes isFlooded status to true
  void floodCell(int wh) {
    if (this.height < wh && !this.isFlooded && (this.left.isFlooded ||
            this.top.isFlooded || this.right.isFlooded ||
            this.bottom.isFlooded)) {
      this.isFlooded = true;
      this.top.floodCell(wh);
      this.left.floodCell(wh);
      this.right.floodCell(wh);
      this.bottom.floodCell(wh);
    }
  }
}

// represents an ocean cell of the game area
class OceanCell extends Cell {
  OceanCell(double height, int x, int y, Cell left, Cell top, Cell right,
            Cell bottom, boolean isFlooded) {
    super(height, x, y, left, top, right, bottom, isFlooded);
  }
  // returns an image of this cell
  public WorldImage image(int waterHeight) {
    return new RectangleImage(ForbiddenIslandWorld.CELL_SIZE,
            ForbiddenIslandWorld.CELL_SIZE, OutlineMode.SOLID, Color.BLUE);
  }
}

// represents the game world
class ForbiddenIslandWorld extends World {
  // Defines an int constant
  static final int ISLAND_SIZE = 64;
  static final int CENTER = ISLAND_SIZE / 2;
  static final double HIGH_HEIGHT = CENTER + 2;
  static final int CELL_SIZE = 28;
  static final int ROW = ISLAND_SIZE * CELL_SIZE;
  // All the cells of the game, including the ocean
  IList<Cell> board;
  // the current height of the ocean
  int waterHeight;
  // keeps track of ticks
  int tick;
  // world player
  Player player;
  // initial list of targets
  ArrayList<Target> targets;
  HelicopterTarget h;
  // win or lose booleans
  boolean win;
  boolean lose;
  // whether or not to show menu
  boolean menu;
  // how long the menu has been up
  int menuTick;

  // initializes a regular island
  ForbiddenIslandWorld() {
    this.board = this.convert(this.regular());
    this.waterHeight = 0;
    this.tick = 0;
    this.player = new Player(this.board.getCellAt(500),
            this.board.filter(new IsLand())).setPlayer();
    this.h = new HelicopterTarget(this.board.getCellAt(CENTER), this.board,
            false, false, false);
    this.targets = this.getTargets();
    this.win = false;
    this.lose = false;
    this.menu = true;
    this.menuTick = 0;
  }

  // initializes a random island
  ForbiddenIslandWorld(int waterHeight) {
    this.board = this.convert(this.random());
    this.waterHeight = waterHeight;
    this.tick = 0;
    this.player = new Player(this.board.getCellAt(500),
            this.board.filter(new IsLand())).setPlayer();
    this.h = new HelicopterTarget(this.board.getCellAt(CENTER), this.board,
            false, false, false);
    this.targets = this.getTargets();
    this.win = false;
    this.lose = false;
    this.menu = true;
    this.menuTick = 0;
  }

  // initializes a terrain island
  ForbiddenIslandWorld(int waterHeight, int tick) {
    this.board = this.convert(this.terrain());
    this.waterHeight = waterHeight;
    this.tick = tick;
    this.player = new Player(this.board.getCellAt(500),
            this.board.filter(new IsLand())).setPlayer();
    this.h = new HelicopterTarget(this.board.getCellAt(CENTER), this.board,
            false, false, false);
    this.targets = this.getTargets();
    this.win = false;
    this.lose = false;
    this.menu = true;
    this.menuTick = 0;
  }

  // creates initial ArrayList of targets
  ArrayList<Target> getTargets() {
    ArrayList<Target> targ = new ArrayList<Target>();
    targ.add(new Target(board.getCellAt(5), this.board.filter(new IsLand()),
            false).setTarget());
    targ.add(new Target(board.getCellAt(5), this.board.filter(new IsLand()),
            false).setTarget());
    targ.add(new Target(board.getCellAt(5), this.board.filter(new IsLand()),
            false).setTarget());
    targ.add(new Target(board.getCellAt(5), this.board.filter(new IsLand()),
            false).setTarget());
    return targ;
  }

  // checks if player has picked up targets
  // EFFECT: removes overlapping targets from ArrayList
  void checkTargets() {
    for (int i = this.targets.size() - 1; i >= 0; i -= 1) {
      if (this.targets.get(i).cur == this.player.cur) {
        this.targets.remove(i);
      }
    }
  }

  // creates list of regular mountain cell heights
  ArrayList<ArrayList<Double>> regular() {
    ArrayList<ArrayList<Double>> ht = new ArrayList<ArrayList<Double>>();
    // gets list of heights for each cell
    for (int i = 0; i <= ISLAND_SIZE; i += 1) {
      ArrayList<Double> heights = new ArrayList<Double>();
      for (int j = 0; j <= ISLAND_SIZE; j += 1) {
        int x = Math.abs(CENTER - i);
        int y = Math.abs(CENTER - j);
        if (x + y >= CENTER) {
          heights.add(0d);
        }
        else {
          heights.add(HIGH_HEIGHT - (x + y));
        }
      }
      ht.add(heights);
    }
    return ht;
  }

  // creates list of random mountain cell heights
  ArrayList<ArrayList<Double>> random() {
    ArrayList<ArrayList<Double>> ht = new ArrayList<ArrayList<Double>>();
    // gets list of heights for each cell
    for (int i = 0; i <= ISLAND_SIZE; i += 1) {
      ArrayList<Double> heights = new ArrayList<Double>();
      for (int j = 0; j <= ISLAND_SIZE; j += 1) {
        int x = Math.abs(CENTER - i);
        int y = Math.abs(CENTER - j);
        if (x + y >= CENTER) {
          heights.add(0.);
        }
        else {
          heights.add(Math.floor(Math.random() * HIGH_HEIGHT) + 1);
        }
      }
      ht.add(heights);
    }
    return ht;
  }

  // creates list of terrain mountain cell heights
  ArrayList<ArrayList<Double>> terrain() {
    ArrayList<ArrayList<Double>> ht = new ArrayList<ArrayList<Double>>();
    for (int i = 0; i <= ISLAND_SIZE; i += 1) {
      ArrayList<Double> arr = new ArrayList<Double>();
      for (int j = 0; j <= ISLAND_SIZE; j += 1) {
        arr.add(0.0);
      }
      ht.add(arr);
    }
    int half = ISLAND_SIZE / 2;
    // initialize center to max height
    ht.get(half).set(half, HIGH_HEIGHT);
    // initialize middles of four edges to just above water
    ht.get(0).set(half, 1.0);
    ht.get(half).set(0, 1.0);
    ht.get(half).set(ISLAND_SIZE, 1.0);
    ht.get(ISLAND_SIZE).set(half, 1.0);

    // Top-Left
    this.terrainHelp(ht, 0, half, 0, half);
    // Top-Right
    this.terrainHelp(ht, 0, half, half, ISLAND_SIZE);
    // Bottom-Left
    this.terrainHelp(ht, half, ISLAND_SIZE, 0, half);
    // Bottom-Right
    this.terrainHelp(ht, half, ISLAND_SIZE, half, ISLAND_SIZE);

    return ht;
  }

  // creates random number
  double nudge(int scale) {
    return ((Math.random() * .5) - .3) * scale;
  }

  // recursively initializes heights based on an algorithm
  void terrainHelp(ArrayList<ArrayList<Double>> arr,
                   int rLo, int rHi, int cLo, int cHi) {

    if ((rHi - rLo) != 1) {
      int scale = (int) Math.sqrt((rHi - rLo) * (cHi - cLo));
      double tl = arr.get(rLo).get(cLo);
      double tr = arr.get(rLo).get(cHi);
      double bl = arr.get(rHi).get(cLo);
      double br = arr.get(rHi).get(cHi);

      double t = Math.min(HIGH_HEIGHT, nudge(scale) + ((tl + tr) / 2.0));
      double b = Math.min(HIGH_HEIGHT, nudge(scale) + ((bl + br) / 2.0));
      double l = Math.min(HIGH_HEIGHT, nudge(scale) + ((tl + bl) / 2.0));
      double r = Math.min(HIGH_HEIGHT, nudge(scale) + ((tr + br) / 2.0));

      double m = Math.min(HIGH_HEIGHT,
              nudge(scale) + (tl + tr + bl + br) / 4.0);


      // Computes the midpoints
      int cmid = (cHi + cLo) / 2;
      int rmid = (rHi + rLo) / 2;

      // sets t, l, r, b
      if (arr.get(rLo).get(cmid) == 0) {
        arr.get(rLo).set(cmid, t);
      }
      if (arr.get(rmid).get(cLo) == 0) {
        arr.get(rmid).set(cLo, l);
      }
      if (arr.get(rmid).get(cHi) == 0) {
        arr.get(rmid).set(cHi, r);
      }
      if (arr.get(rHi).get(cmid) == 0) {
        arr.get(rHi).set(cmid, b);
      }
      arr.get(rmid).set(cmid, m);

      // calls to topLeft, topRight, bottomLeft, bottomRight boxes
      this.terrainHelp(arr, rLo, rmid, cLo, cmid);
      this.terrainHelp(arr, rLo, rmid, cmid, cHi);
      this.terrainHelp(arr, rmid, rHi, cLo, cmid);
      this.terrainHelp(arr, rmid, rHi, cmid, cHi);
    }
  }


  // converts given ArrayList<ArrayList<Double>> to IList<Cell>
  IList<Cell> convert(ArrayList<ArrayList<Double>> hList) {
    ArrayList<ArrayList<Cell>> c = new ArrayList<ArrayList<Cell>>();
    // creates list of cells with given heights
    for (int i = 0; i <= ISLAND_SIZE; i += 1) {
      ArrayList<Cell> cells = new ArrayList<Cell>();
      ArrayList<Double> height = hList.get(i);
      for (int j = 0; j <= ISLAND_SIZE; j += 1) {
        if (height.get(j) <= 0.0) {
          cells.add(new OceanCell(0.0, i, j, null, null, null, null,
                  true));
        }
        else {
          cells.add(new Cell(height.get(j), i, j, null, null,
                  null, null, false));
        }
      }
      c.add(cells);
    }

    IList<Cell> last = new MT<Cell>();
    // sets left/top/right/bottom cells
    for (int i = 0; i <= ISLAND_SIZE; i += 1) {
      ArrayList<Cell> col = c.get(i);
      ArrayList<Cell> left = col;
      ArrayList<Cell> right = col;
      if (i != 0) {
        left = c.get(i - 1);
      }
      if (i != ISLAND_SIZE) {
        right = c.get(i + 1);
      }
      for (int j = 0; j <= ISLAND_SIZE; j += 1) {
        Cell thisC = col.get(j);
        Cell leftC = left.get(j);
        Cell topC = thisC;
        Cell rightC = right.get(j);
        Cell botC = thisC;
        if (j != 0) {
          topC = col.get(j - 1);
        }
        if (j != ISLAND_SIZE) {
          botC = col.get(j + 1);
        }
        thisC.setLeft(leftC);
        thisC.setTop(topC);
        thisC.setRight(rightC);
        thisC.setBottom(botC);

        last = last.append(new Cons<Cell>(thisC, new MT<Cell>()));
      }
    }
    return last;
  }

  // floods the island
  // EFFECT: changes the isFlooded status of cells
  void flooding() {
    for (Cell c : this.board) {
      c.floodCell(this.waterHeight);
    }
  }

  // renders the scene
  public WorldScene makeScene() {
    WorldScene bg = getEmptyScene();
    int sizehalf = CELL_SIZE / 2;
    int sizedub = CELL_SIZE * 2;
    // draws the initial island
    for (Cell cell : board) {
      bg.placeImageXY(cell.image(this.waterHeight), (int)((cell.x *
                      CELL_SIZE) + (sizehalf)),
              (int)((cell.y * CELL_SIZE) + sizehalf));
    }
    // displays countdown clock
    bg.placeImageXY(new ScaleImage(new TextImage("Time Left: " +
            Double.toString(HIGH_HEIGHT + 1 - this.tick), Color.WHITE),
            CELL_SIZE / 8), CELL_SIZE * 6, sizedub);
    // displays score
    bg.placeImageXY(new ScaleImage(new TextImage("Score: " +
            Integer.toString(this.player.steps), Color.WHITE),
            CELL_SIZE / 8), ROW - CELL_SIZE * 3, sizedub);
    // checks if a player has been drowned and displays death message
    if (this.player.cur.isFlooded && !this.player.hasScuba) {
      bg.placeImageXY(new ScaleImage(new TextImage("You Died",
                      Color.WHITE), CELL_SIZE), ISLAND_SIZE * sizehalf,
              ROW / 2);
      bg.placeImageXY(new ScaleImage(new TextImage("Restart with: 'm' 'r'"
              + " or 't'", Color.WHITE), CELL_SIZE / 2.6), ISLAND_SIZE *
              sizehalf, ROW - CELL_SIZE * 20);
      this.lose = true;
    }
    // draws the targets
    for (Target t : this.targets) {
      bg.placeImageXY(t.image, t.cur.x * CELL_SIZE, t.cur.y * CELL_SIZE);
    }
    // draws the player
    bg.placeImageXY(this.player.image, this.player.cur.x * CELL_SIZE,
            this.player.cur.y * CELL_SIZE);
    // draws the helicopter
    bg.placeImageXY(h.image, h.cur.x * CELL_SIZE, h.cur.y * CELL_SIZE);
    // checks if player has activated scuba suit and draws it
    if (this.player.hasScuba && this.player.scubaTime < 5) {
      bg.placeImageXY(new ScaleImage(new FromFileImage("scuba.png"),
                      ISLAND_SIZE / 30.), this.player.cur.x * CELL_SIZE,
              this.player.cur.y * CELL_SIZE);
    }
    // makes suit flash when time is running out
    if (this.player.scubaTime >= 5 && this.player.scubaTime <= 8) {
      if (this.player.scubaTime % 2 == 0) {
        bg.placeImageXY(new ScaleImage(new FromFileImage("scuba.png"),
                        ISLAND_SIZE / 30.), this.player.cur.x * CELL_SIZE,
                this.player.cur.y * CELL_SIZE);
      }
    }
    // deactivates scuba-suit
    if (this.player.scubaTime > 8) {
      this.player.hasScuba = false;
    }
    // checks if a player has collected all the targets and reached the heli
    if ((this.targets.size() == 0) && (this.player.cur == this.h.cur)) {
      bg.placeImageXY(new ScaleImage(new TextImage("You Escaped!",
                      Color.WHITE), CELL_SIZE / 1.26), ISLAND_SIZE * sizehalf,
              ROW / 2);
      bg.placeImageXY(new ScaleImage(new TextImage("Restart with: 'm' 'r'"
              + " or 't'", Color.WHITE), CELL_SIZE / 2.6), ISLAND_SIZE *
              sizehalf, ROW - CELL_SIZE * 20);
      this.win = true;
    }
    // displays opening menu screen
    int font = CELL_SIZE * 2;
    int center = CELL_SIZE * CENTER;
    if (this.menu) {
      bg.placeImageXY(new RectangleImage((ISLAND_SIZE + 2) * CELL_SIZE,
              (ISLAND_SIZE + 2) * CELL_SIZE,
              OutlineMode.SOLID, Color.BLACK), center, center);
      if (this.menuTick <= 2) {
        bg.placeImageXY(new TextImage("Ouch. Your whole body aches. ",
                font, Color.WHITE), center, center);
      }
      if (this.menuTick > 2 && this.menuTick <= 8) {
        bg.placeImageXY(new TextImage("You remember flying your "
                + "helicopter around the Carribbean. ", font,
                Color.WHITE), center, center);
      }
      if (this.menuTick > 8 && this.menuTick <= 11) {
        bg.placeImageXY(new ScaleImage(new FromFileImage("heli.jpg"),
                        CELL_SIZE / (CELL_SIZE * .08 + CELL_SIZE)) , center,
                center);
      }
      if (this.menuTick > 11 && this.menuTick <= 16) {
        bg.placeImageXY(new TextImage(" Suddenly, the engine dies"
                + " and you start hurtling towards the ground! ",
                font, Color.WHITE), center, center);
      }
      if (this.menuTick > 16 && this.menuTick <= 19) {
        bg.placeImageXY(new ScaleImage(new FromFileImage("crash.jpg"),
                CELL_SIZE / (CELL_SIZE - CELL_SIZE * .3)) , center, center);
      }
      if (this.menuTick > 19 && this.menuTick <= 23) {
        bg.placeImageXY(new TextImage("The memory fades and you slowly "
                        + "open your eyes...", font, Color.WHITE), center,
                center);
      }
      if (this.menuTick > 23) {
        bg.placeImageXY(new ScaleImage(new FromFileImage("jungle.jpg"),
                        CELL_SIZE / (CELL_SIZE * .15 + CELL_SIZE)) , center,
                center);
        bg.placeImageXY(new TextImage("You are stranded on an island and"
                        + " your helicopter is in pieces.", font, Color.WHITE),
                center, CELL_SIZE * 2);
        bg.placeImageXY(new TextImage("To escape, you must "
                + "collect all the      " + " and return to the "
                + "helicopter.", font, Color.WHITE), center, CELL_SIZE * 5);
        bg.placeImageXY(new ScaleImage(new FromFileImage("wrench.png"),
                ISLAND_SIZE / 30.), CELL_SIZE * 35, CELL_SIZE * 5);
        bg.placeImageXY(new TextImage("Careful! The island is quickly "
                + "flooding.", font, Color.WHITE), center, CELL_SIZE * 8);
        bg.placeImageXY(new TextImage("Green = above sea level", font,
                Color.WHITE), center, CELL_SIZE * 13);
        bg.placeImageXY(new TextImage("White = way above sea level", font,
                Color.WHITE), center, CELL_SIZE * 16);
        bg.placeImageXY(new TextImage("Red = below sea level but not yet"
                + " flooded", font, Color.WHITE), center, CELL_SIZE * 19);
        bg.placeImageXY(new TextImage("Blue or Black = flooded", font,
                Color.WHITE), center, CELL_SIZE * 22);
        bg.placeImageXY(new TextImage("Use the arrow keys to move.", font,
                Color.WHITE), center, CELL_SIZE * 27);
        bg.placeImageXY(new TextImage("To restart, hit 'm' for regular, "
                + "'r' for random, and 't' for terrain.",
                font, Color.WHITE), center, CELL_SIZE * 30);
        bg.placeImageXY(new TextImage("Use the 's' key to use your scuba "
                + "gear. ", font, Color.WHITE), center, CELL_SIZE * 35);
        bg.placeImageXY(new TextImage("Only while you're wearing this suit "
                + "can you enter the water without dying.", font,
                Color.WHITE), center, CELL_SIZE * 38);
        bg.placeImageXY(new TextImage("WARNING: This will only work once "
                        + "and for a limited time.", font, Color.WHITE),
                center, CELL_SIZE * 41);
        bg.placeImageXY(new TextImage("Press the spacebar to pause / see "
                        + "these instructions again.", font, Color.WHITE), center,
                CELL_SIZE * 46);
        bg.placeImageXY(new TextImage(" After you've collected all the "
                + "pieces, return to the helicopter and escape!", font,
                Color.WHITE), center, CELL_SIZE * 49);
        bg.placeImageXY(new TextImage("Best of luck.", font, Color.WHITE),
                center, CELL_SIZE * 52);
        if (this.menuTick % 2 == 0) {
          bg.placeImageXY(new TextImage("Select either 'm', 'r', or"
                  + " 't' to start", font * 2,
                  Color.WHITE), center, CELL_SIZE * 60);
        }
      }
    }
    return bg;
  }

  // advances time
  // EFFECT: Produces a new world each tick
  public void onTick() {
    if (this.player.hasScuba && !this.win && !this.lose) {
      this.player.scubaTime += 1;
    }
    if (!(this.win || this.lose || this.menu)) {
      this.waterHeight += 1;
      this.flooding();
      this.tick += 1;
    }
    else {
      this.menuTick += 1;
    }
  }

  // responds to keystrokes
  // EFFECT: Produces a new world based on key events
  public void onKeyEvent(String ke) {
    // creates regular mountain
    if (ke.equals("m")) {
      this.board = this.convert(this.regular());
      this.waterHeight = 0;
      this.tick = 0;
      this.player = new Player(this.board.getCellAt(500),
              this.board.filter(new IsLand())).setPlayer();
      this.h = new HelicopterTarget(this.board.getCellAt(CENTER),
              this.board, false, false, false);
      this.targets = this.getTargets();
      this.win = false;
      this.lose = false;
      this.menu = false;
    }
    // creates random mountain
    if (ke.equals("r")) {
      this.board = this.convert(this.random());
      this.waterHeight = 0;
      this.tick = 0;
      this.player = new Player(this.board.getCellAt(500),
              this.board.filter(new IsLand())).setPlayer();
      this.h = new HelicopterTarget(this.board.getCellAt(CENTER),
              this.board, false, false, false);
      this.targets = this.getTargets();
      this.win = false;
      this.lose = false;
      this.menu = false;

    }
    // creates terrain mountain
    if (ke.equals("t")) {
      this.board = this.convert(this.terrain());
      this.waterHeight = 0;
      this.tick = 0;
      this.player = new Player(this.board.getCellAt(500),
              this.board.filter(new IsLand())).setPlayer();
      this.h = new HelicopterTarget(this.board.getCellAt(CENTER),
              this.board, false, false, false);
      this.targets = this.getTargets();
      this.win = false;
      this.lose = false;
      this.menu = false;
    }
    // activates menu and pauses game
    if (ke.equals(" ")) {
      this.menu = true;
    }
    // activates scuba suit
    if (ke.equals("s") && !this.win && !this.lose) {
      this.player.hasScuba = true;
    }
    // otherwise move plater and check if the targets have been reached
    if (!((this.targets.size() == 0) && (this.player.cur == this.h.cur))) {
      this.player.movePlayer(ke);
      this.checkTargets();
    }
  }
}

//represents the player
class Player {
  // the player's possible spawn locations
  IList<Cell> landcells;
  // the player's current cell
  Cell cur;
  // player image
  WorldImage image;
  // # of steps taken
  int steps;
  // whether or not player has scuba gear on
  boolean hasScuba;
  int scubaTime;

  Player(Cell cur, IList<Cell> landcells) {
    this.landcells = landcells;
    this.cur = cur;
    this.image = new ScaleImage(new FromFileImage("pilot1.png"),
            ForbiddenIslandWorld.CELL_SIZE / 13.);
    this.steps = 0;
    this.hasScuba = false;
    this.scubaTime = 0;
  }

  Player(IList<Cell> landcells) {
    this.landcells = landcells;
    this.cur = null;
    this.image = new ScaleImage(new FromFileImage("pilot1.png"),
            ForbiddenIslandWorld.CELL_SIZE / 13.);
    this.steps = 0;
    this.hasScuba = false;
    this.scubaTime = 0;
  }

  // spawns a player at a random cell
  Player setPlayer() {
    int r = (int) Math.floor(Math.random() * this.landcells.length());
    Cell c = this.landcells.getCellAt(r);
    this.cur = c;
    return new Player(this.cur, this.landcells);
  }

  // responds to keystrokes
  // EFFECT: "moves" the player by setting its next cell to that of another
  // non-flooded cell based on keystrokes
  void movePlayer(String k) {
    if ((k.equals("left") && !this.cur.isFlooded &&
            !this.cur.left.isFlooded) || (k.equals("left")
            && this.hasScuba)) {
      this.cur = this.cur.left;
      this.steps += 1;
    }
    if ((k.equals("up") && !this.cur.isFlooded && !this.cur.top.isFlooded)
            || (k.equals("up") && this.hasScuba)) {
      this.cur = this.cur.top;
      this.steps += 1;
    }
    if ((k.equals("right") && !this.cur.isFlooded &&
            !this.cur.right.isFlooded) || (k.equals("right") &&
            this.hasScuba)) {
      this.cur = this.cur.right;
      this.steps += 1;
    }
    if ((k.equals("down") && !this.cur.isFlooded &&
            !this.cur.bottom.isFlooded) || (k.equals("down") &&
            this.hasScuba)) {
      this.cur = this.cur.bottom;
      this.steps += 1;
    }
  }
}

//represents targets that can/need to be be picked up
class Target {
  // the target's Cell location
  Cell cur;
  // the target's possible spawn locations
  IList<Cell> landcells;
  // indicates whether the target is now underwater
  boolean isFlooded;
  // target image
  WorldImage image;

  Target(Cell cur, IList<Cell> landcells, boolean isFlooded) {
    this.cur = cur;
    this.landcells = landcells;
    this.isFlooded = isFlooded;
    this.image = new ScaleImage(new FromFileImage("wrench.png"),
            ForbiddenIslandWorld.CELL_SIZE / 16.);
  }

  Target(IList<Cell> landcells, boolean isFlooded) {
    this.cur = null;
    this.landcells = landcells;
    this.isFlooded = isFlooded;
    this.image = new ScaleImage(new FromFileImage("wrench.png"),
            ForbiddenIslandWorld.CELL_SIZE / 16.);
  }

  // spawns a target at a random cell
  Target setTarget() {
    int r = (int) Math.floor(Math.random() * this.landcells.length());
    Cell c = this.landcells.getCellAt(r);
    this.cur = c;
    return new Target(this.cur, this.landcells, this.isFlooded);
  }

  // checks if this target is flooded
  // EFFECT: changes isFlooded to true if it is
  void targetFlood() {
    if (this.cur.isFlooded) {
      this.isFlooded = true;
    }
  }
}

//represents the helicopter target that can only be picked up after all other
//targets have been picked up
class HelicopterTarget extends Target {
  // indicates whether this is the last target
  boolean isLast;
  HelicopterTarget(Cell cur, IList<Cell> cells, boolean isFlooded,
                   boolean pickedUp, boolean isLast) {
    super(cur, cells, isFlooded);
    this.cur = cells.getCellAt(cells.length() / 2);
    this.image = new ScaleImage(new FromFileImage("helicopter.png"),
            ForbiddenIslandWorld.CELL_SIZE / 15.);
    this.isLast = isLast;
  }
}
