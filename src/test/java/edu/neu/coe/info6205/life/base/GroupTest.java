package edu.neu.coe.info6205.life.base;

import edu.neu.coe.info6205.reduction.Point;
import edu.neu.coe.info6205.util.PrivateMethodTester;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static edu.neu.coe.info6205.life.base.Grid.Origin;
import static edu.neu.coe.info6205.life.library.Library.*;
import static org.junit.Assert.*;

public class GroupTest {

		@Test
		public void testConstructor() {
				Group target = new Group(0L);
				assertNull(target.getExtent1());
				assertFalse(target.overlap(target));
				assertEquals(0, target.getCount());
		}

		@Test
		public void testConstructor1() {
				final List<Point> points = new ArrayList<>();
				final Point point = new Point(1, 1);
				points.add(point);
				Group target = new Group(0L, Origin, points);
				assertEquals(1, target.getCount());
				assertEquals(point.move(-1, -1), target.getExtent1());
				assertEquals(point.move(1, 1), target.getExtent2());
				assertEquals(point, target.pointsAbsolute().get(0));
		}

		@Test
		public void testConstructor2() throws LifeException {
				final List<Point> points = new ArrayList<>();
				points.add(Origin);
				points.add(new Point(1, 1));
				points.add(new Point(2, 0));
				points.add(new Point(2, 2));
				points.add(new Point(3, 1));
				points.add(new Point(4, 1));
				points.add(new Point(4, 2));
				points.add(new Point(5, 0));
				final Point last = new Point(5, 2);
				points.add(last);
				Group target = new Group(0L, Origin, points);
				assertEquals(0L, target.getGeneration());
				assertEquals(9, target.getCount());
				assertEquals(Origin, target.getOrigin());
				assertEquals(Origin.move(-1, -1), target.getExtent1());
				assertEquals(last.move(1, 1), target.getExtent2());
		}

		@Test
		public void testCellsAndNeighbors0() {
				Group target = new Group(0L);
				target.add(Origin);
				final Group.CellsAndNeighbors can = Group.CellsAndNeighbors.create(target);
				assertEquals("*\n", can.toString());
				final PrivateMethodTester targetTester = new PrivateMethodTester(target);
				final Point vector = new Point(1, 1);
				final Group changed = (Group) targetTester.invokePrivate("changeOrigin", 1L, vector);
				assertEquals("*\n", Group.CellsAndNeighbors.create(changed).toString());
		}

		@Test
		public void testCellsAndNeighbors1() {
				Group target = new Group(0L);
				target.add(Origin);
				target.add(new Point(-1, -1));
				final Group.CellsAndNeighbors can = Group.CellsAndNeighbors.create(target);
				assertEquals(".*\n" + "*.\n", can.toString());
				final PrivateMethodTester targetTester = new PrivateMethodTester(target);
				final Point vector = new Point(1, 1);
				final Group changed = (Group) targetTester.invokePrivate("changeOrigin", 1L, vector);
				assertEquals(".*\n" + "*.\n", Group.CellsAndNeighbors.create(changed).toString());
		}

		@Test
		public void testToString() {
				final List<Point> points = new ArrayList<>();
				final Point point = new Point(1, 1);
				points.add(point);
				Group target = new Group(0L, Origin, points);
				assertEquals("generation 0: extents = [Point{x=0, y=0}, Point{x=2, y=2}]\n" +
								"    [Point{x=1, y=1}]", target.toString());
		}

		@Test
		public void testRender() {
				final List<Point> points = new ArrayList<>();
				final Point point = new Point(1, 1);
				points.add(point);
				Group target = new Group(0L, Origin, points);
				assertEquals("*\n", target.render());
		}

		@Test
		public void testChangeOrigin() {
				Group target = new Group(0L);
				final PrivateMethodTester targetTester = new PrivateMethodTester(target);
				final Point point1 = new Point(1, 1);
				target.add(Origin);
				final String targetRendered = target.render();
				assertEquals("*\n", targetRendered);
				final Group changed = (Group) targetTester.invokePrivate("changeOrigin", 1L, point1);
				assertEquals(point1, changed.getOrigin());
				assertEquals(target.getExtent1(), changed.getExtent1());
				assertEquals(target.getExtent2(), changed.getExtent2());
				assertEquals(target.pointsAbsolute(), changed.pointsAbsolute());
				assertEquals(targetRendered, changed.render());
				assertEquals("generation 1: extents = [Point{x=-1, y=-1}, Point{x=1, y=1}]\n" +
								"    [Point{x=0, y=0}]", changed.toString());
		}
		@Test
		public void testAdd() throws LifeException {
				Group target = new Group(0L);
				final Point point = Origin;
				assertTrue(target.add(point));
				assertEquals(1, target.getCount());
				assertEquals(point.move(-1, -1), target.getExtent1());
				assertEquals(point.move(1, 1), target.getExtent2());
				assertTrue(target.overlap(target));
		}

		@Test
		public void testAdd0() {
				final int x = 0;
				final int y = 1;
				final Point point = new Point(x, y);
				Group target = new Group(0L);
				assertTrue(target.add(x, y));
				assertEquals(1, target.getCount());
				assertEquals(point.move(-1, -1), target.getExtent1());
				assertEquals(point.move(1, 1), target.getExtent2());
		}

		@Test
		public void testAdd1() {
				final int x = 1;
				final int y = 3;
				String s = x + " " + y;
				final Point point = new Point(x, y);
				Group target = new Group(0L);
				assertTrue(target.add(s));
				assertEquals(1, target.getCount());
				assertEquals(point.move(-1, -1), target.getExtent1());
				assertEquals(point.move(1, 1), target.getExtent2());
		}

		@Test
		public void testAdd2() {
				final String s = "1 2, 2 3";
				List<Point> points = Point.points(s);
				Group target = new Group(0L);
				assertTrue(target.add(points));
				assertEquals(2, target.getCount());
				assertEquals(points.get(0).move(-1, -1), target.getExtent1());
				assertEquals(points.get(1).move(1, 1), target.getExtent2());
		}

		@SuppressWarnings("unchecked")
		@Test
		public void testGetAbsolute0() throws LifeException {
				Group target = new Group(0L);
				final PrivateMethodTester targetTester = new PrivateMethodTester(target);
				final Point point0 = new Point(1, 1);
				final Point point1 = new Point(2, 2);
				target.add(point0);
				target.add(point1);
				assertEquals(point0.move(-1, -1), targetTester.invokePrivate("getAbsolute", target.getExtent1()));
				assertEquals(point1.move(1, 1), targetTester.invokePrivate("getAbsolute", target.getExtent2()));
				final List<Point> cellsTarget = (List<Point>) targetTester.invokePrivate("getPoints");
				assertEquals(point0, cellsTarget.get(0));
				assertEquals(point1, cellsTarget.get(1));
		}

		@Test
		public void testGetAbsolute1() throws LifeException {
				final Point origin = new Point(10, 10);
				final Point point0 = new Point(1, 1);
				final Point point1 = new Point(2, 2);
				final List<Point> points = new ArrayList<>();
				points.add(point0);
				points.add(point1);
				Group target = new Group(0L, origin, points);
				final PrivateMethodTester targetTester = new PrivateMethodTester(target);
				assertEquals(point0.move(origin).move(-1, -1), target.getExtent1());
				assertEquals(point1.move(origin).move(1, 1), target.getExtent2());
				final List<Point> cellsTarget = target.pointsAbsolute();
				assertEquals(point0.move(origin), cellsTarget.get(0));
				assertEquals(point1.move(origin), cellsTarget.get(1));
		}

		@Test
		public void testOverlap0() {
				Group target = new Group(0L, Origin, null, null, null);
				Group other = new Group(0L, Origin, null, null, null);
				assertFalse(target.overlap(other));
		}

		@Test
		public void testOverlap1() {
				final Point point11 = new Point(1, 1);
				final List<Point> points1 = new ArrayList<>();
				points1.add(Origin);
				final List<Point> points2 = new ArrayList<>();
				points2.add(point11);
				Group target = new Group(0L, Origin, points1);
				Group other = new Group(0L, Origin, points2);
				assertTrue(target.overlap(other));
		}

		@Test
		public void testOverlap2() {
				final Point point11 = new Point(-1, -1);
				final Point point33 = new Point(3, 3);
				final Point point44 = new Point(4, 4);
				final List<Point> cells1 = new ArrayList<>();
				cells1.add(Origin);
				cells1.add(point11);
				final List<Point> cells2 = new ArrayList<>();
				cells2.add(point33);
				cells2.add(point44);
				Group target = new Group(0L, Origin, cells1);
				Group other = new Group(0L, Origin, cells2);
				assertFalse(target.overlap(other));
		}

		@Test
		public void testOverlap3() {
				final Point point22 = new Point(2, 2);
				final Point point33 = new Point(3, 3);
				final Point point44 = new Point(4, 4);
				final List<Point> cells1 = new ArrayList<>();
				cells1.add(Origin);
				cells1.add(point22);
				final List<Point> cells2 = new ArrayList<>();
				cells2.add(point33);
				cells2.add(point44);
				Group target = new Group(0L, Origin, cells1);
				Group other = new Group(0L, Origin, cells2);
				assertTrue(target.overlap(other));
		}

		@Test
		public void testOverlap4() {
				final Point point22 = new Point(2, 2);
				final Point point33 = new Point(3, 3);
				final Point point44 = new Point(4, 4);
				final List<Point> cells1 = new ArrayList<>();
				cells1.add(Origin);
				cells1.add(point22);
				final List<Point> cells2 = new ArrayList<>();
				cells2.add(point33);
				cells2.add(point44);
				Group target = new Group(0L, Origin, cells1);
				Group other = new Group(0L, new Point(10, 10), cells2);
				assertFalse(target.overlap(other));
		}

		@Test(expected = LifeException.class)
		public void testMerge0() throws LifeException {
				Group target = new Group(0L);
				target.merge(target);
		}

		@Test
		public void testMerge1() throws LifeException {
				Group target = new Group(0L);
				final Point point = Origin;
				assertTrue(target.add(point));
				assertEquals(1, target.getCount());
				assertEquals(point.move(-1, -1), target.getExtent1());
				assertEquals(point.move(1, 1), target.getExtent2());
				assertTrue(target.overlap(target));
				Group merged = target.merge(new Group(0L));
				assertEquals(target, merged);
		}

		@Test
		public void testMerge2() throws LifeException {
				final String s = "1 2, 2 3";
				List<Point> points = Point.points(s);
				Group target1 = new Group(0L);
				assertTrue(target1.add(points));
				Group target2 = new Group(0L, new Point(10, 10), new ArrayList<>());
				assertTrue(target2.add(points));
				assertFalse(target1.overlap(target2));
				Group merged = target1.merge(target2);
				assertEquals(4, merged.getCount());
		}

		@Test
		public void testRemove() {
				final List<Point> cells = new ArrayList<>();
				cells.add(Origin);
				Group target = new Group(0L, Origin, Origin, Origin, cells);
				assertEquals(1, target.getCount());
				assertTrue(target.remove(Origin));
				assertEquals(0, target.getCount());
		}

		@Test
		public void testForEach() {
				final List<Point> cells = new ArrayList<>();
				cells.add(Origin);
				Group target = new Group(0L, Origin, Origin, Origin, cells);
				final List<Point> result = new ArrayList<>();
				target.forEach(result::add);
				assertEquals(1, result.size());
				assertEquals(Origin, result.get(0));
		}

		@Test
		public void testGetOrigin() {
				Group target = new Group(0L, Origin, Origin, null, null);
				assertEquals(Origin, target.getExtent1());
		}

		@Test
		public void testGetExtent() {
				Group target = new Group(0L, Origin, null, Origin, null);
				assertEquals(Origin, target.getExtent2());
		}

		@Test
		public void testWithinExtents() {
				final Point point11 = new Point(1, 1);
				final Point point01 = new Point(0, 1);
				final Point point02 = new Point(0, 2);
				final Point point03 = new Point(0, 3);
				Group target = new Group(0L);
				final PrivateMethodTester targetTester = new PrivateMethodTester(target);
				assertTrue(target.add(Origin));
				assertTrue(target.add(point11));
				assertTrue((Boolean) targetTester.invokePrivate("withinExtents", Origin));
				assertTrue((Boolean) targetTester.invokePrivate("withinExtents", point01));
				assertTrue((Boolean) targetTester.invokePrivate("withinExtents", point02));
				assertFalse((Boolean) targetTester.invokePrivate("withinExtents", point03));
				assertTrue((Boolean) targetTester.invokePrivate("withinExtents", point11));
		}

		@Test
		public void testNewGeneration0() throws LifeException {
				final List<Point> cells1 = new ArrayList<>();
				cells1.add(Origin);
				cells1.add(new Point(1, 1));
				cells1.add(new Point(2, 0));
				cells1.add(new Point(2, 2));
				cells1.add(new Point(3, 1));
				cells1.add(new Point(4, 1));
				cells1.add(new Point(4, 2));
				cells1.add(new Point(5, 0));
				cells1.add(new Point(5, 2));
				Group target = new Group(0L, Origin, cells1);
				final Group newGeneration = target.newGeneration(1L);
				assertEquals(".*.**\n" + "*....\n" + "****.\n", newGeneration.render());
				assertEquals(8, newGeneration.getCount());
		}

		@Test
		public void testNewGeneration1() throws LifeException {
				Group target = new Group(0L);
				target.add(Origin);
				target.add(new Point(-1, -1));
				final Group newGeneration = target.newGeneration(1L);
				assertEquals("", newGeneration.render());
				assertEquals(0, newGeneration.getCount());
		}

		@Test
		public void testBlock() throws LifeException {
				Group target = new Group(0L);
				target.add(Block);
				assertEquals(new Point(0, 0), target.getExtent1());
				assertEquals(new Point(3, 3), target.getExtent2());
				final Group newGeneration = target.newGeneration(1L);
				assertEquals(target.getExtent1(), newGeneration.getOrigin());
				final int count = newGeneration.getCount();
				assertEquals(4, count);
				final List<Point> cellsTarget = target.pointsAbsolute();
				final List<Point> cellsNG = newGeneration.pointsAbsolute();
				for (int i = 0; i < count; i++) assertEquals(cellsTarget.get(i), cellsNG.get(i));
				final Group gen2 = newGeneration.newGeneration(1L);
				assertEquals(newGeneration.getExtent1(), gen2.getOrigin());
				assertEquals(4, gen2.getCount());
				final List<Point> cellsGen2 = gen2.pointsAbsolute();
				for (int i = 0; i < count; i++) assertEquals(cellsTarget.get(i), cellsGen2.get(i));
		}

		@SuppressWarnings("unchecked")
		@Test
		public void testBeehive() throws LifeException {
				Group target = new Group(0L);
				final PrivateMethodTester targetTester = new PrivateMethodTester(target);
				target.add(Beehive);
				assertEquals(".**.\n" + "*..*\n" + ".**.\n",target.render());
				assertEquals(6, target.getCount());
				assertEquals(Origin, target.getExtent1());
				assertEquals(new Point(5, 4), target.getExtent2());
				final Group newGeneration = target.newGeneration(1L);
				final PrivateMethodTester ngTester = new PrivateMethodTester(newGeneration);
				assertEquals(target.getExtent1(), newGeneration.getOrigin());
				final int count = newGeneration.getCount();
				assertEquals(6, count);
				final List<Point> cellsTarget = (List<Point>) targetTester.invokePrivate("getPoints");
				final List<Point> cellsNG = (List<Point>) ngTester.invokePrivate("getPoints");
				for (int i = 0; i < count; i++) assertEquals(cellsTarget.get(i), cellsNG.get(i));
				final Group gen2 = newGeneration.newGeneration(1L);
				final PrivateMethodTester gen2Tester = new PrivateMethodTester(gen2);
				assertEquals(newGeneration.getExtent1(), gen2.getOrigin());
				assertEquals(".**.\n" + "*..*\n" + ".**.\n",newGeneration.render());
				assertEquals(6, gen2.getCount());
				final List<Point> cellsGen2 = (List<Point>) gen2Tester.invokePrivate("getPoints");
				for (int i = 0; i < count; i++) assertEquals(cellsTarget.get(i).relative(gen2.getOrigin()), cellsGen2.get(i));
		}

		@SuppressWarnings("unchecked")
		@Test
		public void testLoaf() throws LifeException {
				Group target = new Group(0L);
				final PrivateMethodTester targetTester = new PrivateMethodTester(target);
				target.add(Loaf);
				System.out.println(target.render());
				assertEquals(Origin, target.getExtent1());
				assertEquals(new Point(5, 5), target.getExtent2());
				final Group newGeneration = target.newGeneration(1L);
				final PrivateMethodTester ngTester = new PrivateMethodTester(newGeneration);
				assertEquals(target.getExtent1(), newGeneration.getOrigin());
				final int count = newGeneration.getCount();
				assertEquals(7, count);
				final List<Point> cellsTarget = (List<Point>) targetTester.invokePrivate("getPoints");
				final List<Point> cellsNG = (List<Point>) ngTester.invokePrivate("getPoints");
				for (int i = 0; i < count; i++) assertEquals(cellsTarget.get(i), cellsNG.get(i));
				final Group gen2 = newGeneration.newGeneration(1L);
				final PrivateMethodTester gen2Tester = new PrivateMethodTester(gen2);
				assertEquals(newGeneration.getExtent1(), gen2.getOrigin());
				System.out.println(newGeneration.render());
				assertEquals(7, gen2.getCount());
				final List<Point> cellsGen2 = (List<Point>) gen2Tester.invokePrivate("getPoints");
				for (int i = 0; i < count; i++) assertEquals(cellsTarget.get(i).relative(gen2.getOrigin()), cellsGen2.get(i));
		}

		@Test
		public void testBlinker() throws LifeException {
				Group target = new Group(0L);
				target.add(Blinker);
				assertEquals("*\n" + "*\n" + "*\n", target.render());
				assertEquals(new Point(-1, -2), target.getExtent1());
				assertEquals(new Point(1, 2), target.getExtent2());
				final Group newGeneration = target.newGeneration(1L);
				assertEquals(Origin, newGeneration.getOrigin());
				assertEquals("***\n", newGeneration.render());
				final int count = newGeneration.getCount();
				assertEquals(3, count);
				final List<Point> cellsNG = newGeneration.pointsAbsolute();
				for (int i = 0; i < count; i++) assertEquals(0, cellsNG.get(i).getY());
				for (int i = 0; i < count; i++) assertTrue(Math.abs(cellsNG.get(i).getX()) <= 1);
				final Group gen2 = newGeneration.newGeneration(2L);
				assertEquals("*\n" + "*\n" + "*\n", gen2.render());
				assertEquals(3, gen2.getCount());
				final List<Point> cellsGen2 = gen2.pointsAbsolute();
				for (int i = 0; i < count; i++) assertEquals(0, cellsGen2.get(i).getX());
				for (int i = 0; i < count; i++) assertTrue(Math.abs(cellsNG.get(i).getY()) <= 1);
		}

		@Test
		public void transpose() {
				Group target = new Group(0L);
				target.add(Origin);
				target.add(new Point(0, 1));
				final List<Point> points = target.pointsAbsolute();
				final Group transposed = target.transpose();
				final List<Point> pointsT = transposed.pointsAbsolute();
				assertEquals(Origin, pointsT.get(0));
				assertEquals(new Point(1, 0), pointsT.get(1));
				final Group transposedAgain = transposed.transpose();
				assertEquals(target, transposedAgain);
		}
}