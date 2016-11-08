import java.util.ArrayList;
import tester.*;

// Tests for the ForbiddenIslandGame.

class ExamplesIsland {

  static ExamplesIsland island = new ExamplesIsland();

  public static void main(String[] args) {
    island.testOnKeyEvent(new Tester());
    island.testOnTick(new Tester());
    island.testNudge(new Tester());
    island.testGetTargets(new Tester());
    island.testAsCons(new Tester());
    island.testIsCons(new Tester());
    island.testRegular(new Tester());
    island.testRandom(new Tester());
    island.testTerrain(new Tester());
    island.testConvert(new Tester());
    island.testBigBang(new Tester());
  }

    Cell ec1;
    Cell ec2;
    Cell ec3;
    Cell ec4;
    Cell ec5;
    Cell ec6;
    Cell ec7;
    Cell ec8;
    Cell ec9;
    Cell ec10;

    OceanCell oc1;
    OceanCell oc2;
    OceanCell oc3;
    OceanCell oc4;
    OceanCell oc5;
    OceanCell oc6;
    OceanCell oc7;
    OceanCell oc8;

    Player p1;
    Player p2;
    Player p3;
    Player mp1;
    Player mp2;
    Player mp3;

    Target t1;
    Target t2;
    Target t3;
    Target t4;

    ForbiddenIslandWorld i;
    ForbiddenIslandWorld i2;
    ForbiddenIslandWorld i3;

    ArrayList<ArrayList<Double>> c1;
    ArrayList<ArrayList<Double>> c2;
    ArrayList<ArrayList<Double>> c3;
    ArrayList<Target> at;

    IList<Cell> cw1;
    IList<Cell> cw2;
    IList<Cell> cw3;
    IList<Cell> lc1;
    IList<Cell> lc2;

  // sets up initial conditions
  void initData() {
    ec1 = new Cell(1.0, 1, 0, null, null, null, null, false);
    ec2 = new Cell(1.0, 0, 1, null, null, null, null, false);
    ec3 = new Cell(2.0, 1, 1, null, null, null, null, false);
    ec4 = new Cell(1.0, 2, 1, null, null, null, null, false);
    ec5 = new Cell(1.0, 1, 2, null, null, null, null, false);
    oc1 = new OceanCell(0.0, 0, 0, null, null, null, null, true);
    oc2 = new OceanCell(0.0, 2, 0, null, null, null, null, true);
    oc3 = new OceanCell(0.0, 0, 2, null, null, null, null, true);
    oc4 = new OceanCell(0.0, 2, 2, null, null, null, null, true);

    ec6 = new Cell(3.0, 2, 0, null, null, null, null, false);
    ec7 = new Cell(2.0, 0, 1, null, null, null, null, false);
    ec8 = new Cell(1.0, 1, 1, null, null, null, null, false);
    ec9 = new Cell(1.0, 1, 2, null, null, null, null, false);
    ec10 = new Cell(1.0, 2, 2, null, null, null, null, false);
    oc5 = new OceanCell(0.0, 0, 0, null, null, null, null, true);
    oc6 = new OceanCell(0.0, 1, 0, null, null, null, null, true);
    oc7 = new OceanCell(0.0, 2, 1, null, null, null, null, true);
    oc8 = new OceanCell(0.0, 0, 2, null, null, null, null, true);

    ec1.setLeft(oc1);
    ec1.setTop(ec1);
    ec1.setRight(oc2);
    ec1.setBottom(ec3);
    ec2.setLeft(ec2);
    ec2.setTop(oc1);
    ec2.setRight(ec3);
    ec2.setBottom(oc3);
    ec3.setLeft(ec2);
    ec3.setTop(ec1);
    ec3.setRight(ec4);
    ec3.setBottom(ec5);
    ec4.setLeft(ec3);
    ec4.setTop(oc2);
    ec4.setRight(ec4);
    ec4.setBottom(oc4);
    ec5.setLeft(oc3);
    ec5.setTop(ec3);
    ec5.setRight(oc4);
    ec5.setBottom(ec5);
    oc1.setLeft(oc1);
    oc1.setTop(oc1);
    oc1.setRight(ec1);
    oc1.setBottom(ec2);
    oc2.setLeft(ec1);
    oc2.setTop(oc2);
    oc2.setRight(oc2);
    oc2.setBottom(ec4);
    oc3.setLeft(oc3);
    oc3.setTop(ec2);
    oc3.setRight(ec5);
    oc3.setBottom(oc3);
    oc4.setLeft(ec5);
    oc4.setTop(ec4);
    oc4.setRight(oc4);
    oc4.setBottom(oc4);

    ec6.setLeft(oc6);
    ec6.setTop(ec6);
    ec6.setRight(ec6);
    ec6.setBottom(oc7);
    ec7.setLeft(ec7);
    ec7.setTop(oc5);
    ec7.setRight(ec8);
    ec7.setBottom(oc8);
    ec8.setLeft(ec7);
    ec8.setTop(oc6);
    ec8.setRight(oc7);
    ec8.setBottom(ec9);
    ec9.setLeft(oc8);
    ec9.setTop(ec8);
    ec9.setRight(ec10);
    ec9.setBottom(ec9);
    ec10.setLeft(ec9);
    ec10.setTop(oc7);
    ec10.setRight(ec10);
    ec10.setBottom(ec10);
    oc5.setLeft(oc5);
    oc5.setTop(oc5);
    oc5.setRight(oc6);
    oc5.setBottom(ec7);
    oc6.setLeft(oc5);
    oc6.setTop(oc6);
    oc6.setRight(ec6);
    oc6.setBottom(ec8);
    oc7.setLeft(ec8);
    oc7.setTop(ec6);
    oc7.setRight(oc7);
    oc7.setBottom(ec10);
    oc8.setLeft(oc8);
    oc8.setTop(ec7);
    oc8.setRight(ec9);
    oc8.setBottom(oc8);

    lc1 = new Cons<Cell>(ec1, new Cons<Cell>(ec2,
            new Cons<Cell>(ec3, new Cons<Cell>(ec4,
                    new Cons<Cell>(ec5, new MT<Cell>())))));
    lc2 = new Cons<Cell>(ec6, new Cons<Cell>(ec7,
            new Cons<Cell>(ec8, new Cons<Cell>(ec9,
                    new Cons<Cell>(ec10, new MT<Cell>())))));

    p1 = new Player(lc1);
    p2 = new Player(lc2);
    p3 = new Player(lc2);
    mp1 = new Player(ec3, lc1);
    mp2 = new Player(ec10, lc2);
    mp3 = new Player(ec6, lc2);

    t1 = new Target(lc1, false);
    t2 = new Target(lc1, false);
    t3 = new Target(lc2, false);
    t4 = new Target(lc2, false);

    i = new ForbiddenIslandWorld();
    i2 = new ForbiddenIslandWorld(0);
    i3 = new ForbiddenIslandWorld(0, 0);
    c1 = i.regular();
    c2 = i2.random();
    c3 = i3.terrain();
    cw1 = i.convert(c1);
    cw2 = i2.convert(c2);
    cw3 = i3.convert(c3);

    at = new ArrayList<Target>();
  }

  // tests flooding
  void testFlooding(Tester t) {
    initData();
    i.onKeyEvent("m");
    i.board = lc1;
    i.flooding();
    t.checkExpect(ec1.isFlooded, false);
    t.checkExpect(ec2.isFlooded, false);
    t.checkExpect(ec3.isFlooded, false);
    t.checkExpect(ec4.isFlooded, false);
    t.checkExpect(ec5.isFlooded, false);
    i.onTick();
    i.flooding();
    t.checkExpect(ec1.isFlooded, false);
    t.checkExpect(ec2.isFlooded, false);
    t.checkExpect(ec3.isFlooded, false);
    t.checkExpect(ec4.isFlooded, false);
    t.checkExpect(ec5.isFlooded, false);
    i.onTick();
    i.flooding();
    t.checkExpect(ec1.isFlooded, true);
    t.checkExpect(ec2.isFlooded, true);
    t.checkExpect(ec3.isFlooded, false);
    t.checkExpect(ec4.isFlooded, true);
    t.checkExpect(ec5.isFlooded, true);
    i.onTick();
    i.flooding();
    t.checkExpect(ec1.isFlooded, true);
    t.checkExpect(ec2.isFlooded, true);
    t.checkExpect(ec3.isFlooded, true);
    t.checkExpect(ec4.isFlooded, true);
    t.checkExpect(ec5.isFlooded, true);

    i2.onKeyEvent("m");
    i2.board = lc2;
    i2.flooding();
    t.checkExpect(ec6.isFlooded, false);
    t.checkExpect(ec7.isFlooded, false);
    t.checkExpect(ec8.isFlooded, false);
    t.checkExpect(ec9.isFlooded, false);
    t.checkExpect(ec10.isFlooded, false);
    i2.onTick();
    i2.flooding();
    t.checkExpect(ec6.isFlooded, false);
    t.checkExpect(ec7.isFlooded, false);
    t.checkExpect(ec8.isFlooded, false);
    t.checkExpect(ec9.isFlooded, false);
    t.checkExpect(ec10.isFlooded, false);
    i2.onTick();
    i2.flooding();
    t.checkExpect(ec6.isFlooded, false);
    t.checkExpect(ec7.isFlooded, false);
    t.checkExpect(ec8.isFlooded, true);
    t.checkExpect(ec9.isFlooded, true);
    t.checkExpect(ec10.isFlooded, true);
    i2.onTick();
    i2.flooding();
    t.checkExpect(ec6.isFlooded, false);
    t.checkExpect(ec7.isFlooded, true);
    t.checkExpect(ec8.isFlooded, true);
    t.checkExpect(ec9.isFlooded, true);
    t.checkExpect(ec10.isFlooded, true);
    i2.onTick();
    i2.flooding();
    t.checkExpect(ec6.isFlooded, true);
    t.checkExpect(ec7.isFlooded, true);
    t.checkExpect(ec8.isFlooded, true);
    t.checkExpect(ec9.isFlooded, true);
    t.checkExpect(ec10.isFlooded, true);
  }

  // tests onKeyEvent
  void testOnKeyEvent(Tester t) {
    initData();
    i.onKeyEvent("m");
    i2.onKeyEvent("m");
    i3.onKeyEvent("m");
    t.checkExpect(i.waterHeight, i2.waterHeight);
    t.checkExpect(i.waterHeight, i3.waterHeight);
    t.checkExpect(i2.waterHeight, i3.waterHeight);
    t.checkExpect(i.tick, i2.tick);
    t.checkExpect(i.tick, i3.tick);
    t.checkExpect(i2.tick, i3.tick);
    t.checkExpect(i.win, i2.win);
    t.checkExpect(i.win, i3.win);
    t.checkExpect(i2.win, i3.win);
    t.checkExpect(i.lose, i2.lose);
    t.checkExpect(i.lose, i3.lose);
    t.checkExpect(i2.lose, i3.lose);
    t.checkExpect(i.board.length(), i2.board.length());
    t.checkExpect(i.board.length(), i3.board.length());
    t.checkExpect(i2.board.length(), i3.board.length());
    i.onKeyEvent("m");
    t.checkExpect(i.waterHeight, i2.waterHeight);
    t.checkExpect(i.waterHeight, i3.waterHeight);
    t.checkExpect(i.tick, i2.tick);
    t.checkExpect(i.tick, i3.tick);
    t.checkExpect(i.win, i2.win);
    t.checkExpect(i.win, i3.win);
    t.checkExpect(i.lose, i2.lose);
    t.checkExpect(i.lose, i3.lose);
    t.checkExpect(i.board.length(), i2.board.length());
    t.checkExpect(i.board.length(), i3.board.length());

    i.onKeyEvent("r");
    i2.onKeyEvent("r");
    i3.onKeyEvent("r");
    t.checkExpect(i.waterHeight, i2.waterHeight);
    t.checkExpect(i.waterHeight, i3.waterHeight);
    t.checkExpect(i2.waterHeight, i3.waterHeight);
    t.checkExpect(i.tick, i2.tick);
    t.checkExpect(i.tick, i3.tick);
    t.checkExpect(i2.tick, i3.tick);
    t.checkExpect(i.win, i2.win);
    t.checkExpect(i.win, i3.win);
    t.checkExpect(i2.win, i3.win);
    t.checkExpect(i.lose, i2.lose);
    t.checkExpect(i.lose, i3.lose);
    t.checkExpect(i2.lose, i3.lose);
    t.checkExpect(i.board.length(), i2.board.length());
    t.checkExpect(i.board.length(), i3.board.length());
    t.checkExpect(i.board.length(), i3.board.length());

    i.onKeyEvent("t");
    i2.onKeyEvent("t");
    i3.onKeyEvent("t");
    t.checkExpect(i.waterHeight, i2.waterHeight);
    t.checkExpect(i.waterHeight, i3.waterHeight);
    t.checkExpect(i2.waterHeight, i3.waterHeight);
    t.checkExpect(i.tick, i2.tick);
    t.checkExpect(i.tick, i3.tick);
    t.checkExpect(i2.tick, i3.tick);
    t.checkExpect(i.win, i2.win);
    t.checkExpect(i.win, i3.win);
    t.checkExpect(i2.win, i3.win);
    t.checkExpect(i.lose, i2.lose);
    t.checkExpect(i.lose, i3.lose);
    t.checkExpect(i2.lose, i3.lose);

  }

  // tests onTick
  void testOnTick(Tester t) {
    initData();
    i.onKeyEvent("m");
    for (int j = 0; j <= 5; j += 1) {
      i.onTick();
      i2.onTick();
      t.checkExpect(i.waterHeight, j + 1);
      t.checkExpect(i2.waterHeight, 0);
      t.checkExpect(i.tick, j + 1);
      t.checkExpect(i2.tick, 0);
    }
    i2.onKeyEvent("t");
    i2.onTick();
    t.checkExpect(i2.waterHeight, 1);
    t.checkExpect(i2.tick, 1);
    i2.lose = true;
    for (int j = 0; j <= 5; j += 1) {
      i2.onTick();
      t.checkExpect(i2.waterHeight, 1);
      t.checkExpect(i2.tick, 1);
    }
    i2.lose = false;
    i2.onTick();
    t.checkExpect(i2.waterHeight, 2);
    t.checkExpect(i2.tick, 2);

    i3.onKeyEvent("r");
    t.checkExpect(i3.waterHeight, 0);
    t.checkExpect(i3.tick, 0);
    i3.onTick();
    t.checkExpect(i3.waterHeight, 1);
    t.checkExpect(i3.tick, 1);
    i3.win = true;
    for (int j = 0; j <= 5; j += 1) {
      i3.onTick();
      t.checkExpect(i3.waterHeight, 1);
      t.checkExpect(i3.tick, 1);
    }
    i3.win = false;
    i3.onTick();
    t.checkExpect(i3.waterHeight, 2);
    t.checkExpect(i3.tick, 2);
    i3.onKeyEvent(" ");
    for (int j = 0; j <= 5; j += 1) {
      i3.onTick();
      t.checkExpect(i3.waterHeight, 2);
      t.checkExpect(i3.tick, 2);
    }
  }

  // tests nudge
  void testNudge(Tester t) {
    initData();
    t.checkInexact(i2.nudge(0), 0.0, (0.3 * 0.0));
    t.checkInexact(i2.nudge(1), 0.0, (0.3 * 1.0));
    t.checkInexact(i2.nudge(2), 0.0, (0.3 * 2.0));
    t.checkInexact(i2.nudge(3), 0.0, (0.3 * 3.0));
  }

  // tests getTargets
  void testGetTargets(Tester t) {
    initData();
    at.add(new Target(i.board.getCellAt(5), i.board.filter(
            new IsLand()), false).setTarget());
    at.add(new Target(i.board.getCellAt(5), i.board.filter(
            new IsLand()), false).setTarget());
    at.add(new Target(i.board.getCellAt(5), i.board.filter(
            new IsLand()), false).setTarget());
    at.add(new Target(i.board.getCellAt(5), i.board.filter(
            new IsLand()), false).setTarget());
    t.checkExpect(i.targets.size(), 4);
    t.checkExpect(i2.targets.size(), 4);
    t.checkExpect(i3.targets.size(), 4);
    t.checkExpect(i3.targets.size(), at.size());
  }

  // tests asCons
  void testAsCons(Tester t) {
    t.checkException(new RuntimeException("mt cannot be as cons"),
            new MT<Cell>(), "asCons");
    t.checkExpect(new Cons<Cell>(
            new Cell(6.0, 3, 4, null, null, null, null, false),
            new MT<Cell>()), (new Cons<Cell>(
            new Cell(6.0, 3, 4, null, null, null, null, false),
            new MT<Cell>())));
    t.checkExpect(new Cons<String>("first", new Cons<String>("second",
            new MT<String>())), new Cons<String>("first",
            new Cons<String>("second", new MT<String>())));
  }

  // tests isCons
  void testIsCons(Tester t) {
    t.checkExpect(new MT<Cell>().isCons(), false);
    t.checkExpect(new Cons<Cell>(
            new Cell(6.0, 3, 4, null, null, null, null, false),
            new MT<Cell>()).isCons(), true);
    t.checkExpect(new Cons<String>("first",
            new MT<String>()).isCons(), true);
  }

  // tests regular
  void testRegular(Tester t) {
    initData();
    t.checkExpect(i.board.length(), 65 * 65);
    t.checkExpect(cw1.length(), 65 * 65);
    t.checkExpect(c1.size(), 65);
    t.checkExpect(c1.get(2).size(), 65);
    t.checkInexact(c1.get(64).get(32), 0.0, 0.001);
    t.checkInexact(c1.get(63).get(32), 3.0, 0.001);
    t.checkInexact(c1.get(32).get(32), 34.0, 0.001);
    t.checkInexact(c1.get(0).get(32), 0.0, 0.001);
    t.checkInexact(c1.get(1).get(32), 3.0, 0.001);
    t.checkInexact(c1.get(62).get(32), c1.get(62).get(31) + 1.0, 0.001);
    t.checkInexact(c1.get(40).get(32), c1.get(40).get(33) + 1.0, 0.001);
  }

  // tests random
  void testRandom(Tester t) {
    initData();
    t.checkExpect(i2.board.length(), 65 * 65);
    t.checkExpect(cw2.length(), 65 * 65);
    t.checkExpect(c2.size(), 65);
    t.checkExpect(c2.get(43).size(), 65);
    t.checkInexact(c2.get(0).get(32), 0.0, 0.001);
    t.checkInexact(c2.get(0).get(27), 0.0, 0.001);
    t.checkInexact(c2.get(64).get(32), 0.0, 0.001);
    t.checkInexact(c2.get(64).get(55), 0.0, 0.001);
    t.checkInexactFail(c2.get(0).get(32), 2.0, 0.001);
    t.checkInexactFail(c2.get(32).get(32), 66.0, 0.001);
  }

  // tests random
  void testTerrain(Tester t) {
    initData();
    t.checkExpect(i3.board.length(), 65 * 65);
    t.checkExpect(cw3.length(), 65 * 65);
    t.checkExpect(c3.size(), 65);
    t.checkExpect(c3.get(43).size(), 65);
    t.checkInexact(c3.get(0).get(32), 1.0, 0.001);
    t.checkInexact(c3.get(32).get(32), 34.0, 0.001);
    t.checkInexact(c3.get(64).get(32), 1.0, 0.001);
    t.checkInexact(c3.get(64).get(64), 0.0, 0.001);
    t.checkInexact(c3.get(0).get(0), 0.0, 0.001);
    t.checkInexactFail(c3.get(32).get(32), 66.0, 0.001);
  }


  // tests convert
  void testConvert(Tester t) {
    initData();
    t.checkExpect(i.convert(c1).length(), 65 * 65);
    t.checkExpect(i2.convert(c1).length(), 65 * 65);
    t.checkExpect(i.convert(c2).length(), 65 * 65);
    t.checkExpect(i2.convert(c2).length(), 65 * 65);
    t.checkExpect(i.convert(c1).isCons(), true);
    t.checkExpect(i2.convert(c1).isCons(), true);
    t.checkExpect(i.convert(c2).isCons(), true);
    t.checkExpect(i2.convert(c2).isCons(), true);
  }

  // test bigBang
  void testBigBang(Tester t) {
    initData();
    i3.bigBang((ForbiddenIslandWorld.ISLAND_SIZE + 1) *
                    ForbiddenIslandWorld.CELL_SIZE,
            (ForbiddenIslandWorld.ISLAND_SIZE + 1) *
                    ForbiddenIslandWorld.CELL_SIZE, 1);
  }
}


