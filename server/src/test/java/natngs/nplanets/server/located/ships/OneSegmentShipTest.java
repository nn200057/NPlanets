package natngs.nplanets.server.located.ships;

import natngs.nplanets.common.Location;
import natngs.nplanets.server.ILocated;
import natngs.nplanets.server.Universe;
import natngs.nplanets.server.located.planets.PolygonalPlanet;
import natngs.nplanets.server.located.planets.StaticPlanet;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class OneSegmentShipTest {
	private Universe u;

	@Before
	public void before() {
		u = new Universe();
	}

	@Test
	public void staticOriginStaticDestination() throws Exception {
		StaticPlanet p1 = new StaticPlanet(u, new Location(-100, 42));
		StaticPlanet p2 = new StaticPlanet(u, new Location(100, 42));
		double launch = 69;
		double speed = 50;
		double arrival = launch + 4;

		List<ILocated> steps = new ArrayList<>();
		steps.add(p1);
		steps.add(p2);
		Ship s = new Ship(steps, speed, launch);

		assertEquals(p1.getLocation(launch), s.getLocation(launch));
		assertNotEquals(p1.getLocation(launch + 1), s.getLocation(launch + 1));
		assertNotEquals(p2.getLocation(arrival - 1), s.getLocation(arrival - 1));
		assertEquals(p2.getLocation(arrival), s.getLocation(arrival));

		double time = launch * 0.5 + arrival * 0.5;
		assertEquals(Location.getMid(p1.getLocation(time), p2.getLocation(time), 0.5), s.getLocation(time));

		time = launch * 0.11 + arrival * 0.89;
		assertEquals(Location.getMid(p1.getLocation(time), p2.getLocation(time), 0.89), s.getLocation(time));

		time = launch * 0.91 + arrival * 0.09;
		assertEquals(Location.getMid(p1.getLocation(time), p2.getLocation(time), 0.09), s.getLocation(time));
	}


	@Test
	public void staticOriginMovingDestination() throws Exception {
		StaticPlanet p1 = new StaticPlanet(u, new Location(-100, 0));
		List<Location> l2 = new ArrayList<>();
		l2.add(new Location(100, 100));
		l2.add(new Location(100, -100));
		PolygonalPlanet p2 = new PolygonalPlanet(u, l2, 400); // Moving 1 each second
		double launch = 50;
		double speed = 4;
		double arrival = launch + 50;
		List<ILocated> steps = new ArrayList<>();
		steps.add(p1);
		steps.add(p2);
		Ship s = new Ship(steps, speed, launch);

		assertEquals(p1.getLocation(launch), s.getLocation(launch));
		assertNotEquals(p1.getLocation(launch + 1), s.getLocation(launch + 1));
		assertNotEquals(p2.getLocation(arrival - 1), s.getLocation(arrival - 1));
		assertEquals(p2.getLocation(arrival), s.getLocation(arrival));
		assertEquals(new Location(0, 0), s.getLocation((launch + arrival) / 2));
		assertEquals(new Location(78, 0), s.getLocation(launch * 0.11 + arrival * 0.89));
		assertEquals(new Location(-82, 0), s.getLocation(launch * 0.91 + arrival * 0.09));
	}


	@Test
	public void movingOriginStaticDestination() throws Exception {
		List<Location> l1 = new ArrayList<>();
		l1.add(new Location(100, 100));
		l1.add(new Location(100, -100));
		PolygonalPlanet p1 = new PolygonalPlanet(u, l1, 400); // Moving 1 each second
		StaticPlanet p2 = new StaticPlanet(u, new Location(-100, 0));
		double launch = 50;
		double speed = 4;
		double arrival = launch + 50;
		List<ILocated> steps = new ArrayList<>();
		steps.add(p1);
		steps.add(p2);
		Ship s = new Ship(steps, speed, launch);

		assertEquals(p1.getLocation(launch), s.getLocation(launch));
		assertNotEquals(p1.getLocation(launch + 1), s.getLocation(launch + 1));
		assertNotEquals(p2.getLocation(arrival - 1), s.getLocation(arrival - 1));
		assertEquals(p2.getLocation(arrival), s.getLocation(arrival));
		assertEquals(new Location(0, 25), s.getLocation((launch + arrival) / 2));
		assertEquals(new Location(-78, 5.496), s.getLocation(launch * 0.11 + arrival * 0.89));
		assertEquals(new Location(82, 45.5), s.getLocation(launch * 0.91 + arrival * 0.09));
	}


	@Test
	public void movingOriginMovingDestination() throws Exception {
		List<Location> l1 = new ArrayList<>();
		l1.add(new Location(-100, -200));
		l1.add(new Location(-100, 0));
		PolygonalPlanet p1 = new PolygonalPlanet(u, l1, 80); // Moving 5 each second

		List<Location> l2 = new ArrayList<>();
		l2.add(new Location(100, 130));
		l2.add(new Location(100, -20));
		PolygonalPlanet p2 = new PolygonalPlanet(u, l2, 66.667); // Moving 4.5 each second

		double launch = 20;
		double speed = 5;
		double arrival = launch + 40;
		List<ILocated> steps = new ArrayList<>();
		steps.add(p1);
		steps.add(p2);
		Ship s = new Ship(steps, speed, launch);

		assertEquals(new Location(-100, -100), p1.getLocation(launch));
		assertEquals(new Location(100, 100), p2.getLocation(arrival));

		assertEquals(p1.getLocation(launch), s.getLocation(launch));
		assertNotEquals(p1.getLocation(launch + 1), s.getLocation(launch + 1));
		assertNotEquals(p2.getLocation(arrival - 1), s.getLocation(arrival - 1));
		assertEquals(p2.getLocation(arrival), s.getLocation(arrival));
		assertEquals(new Location(0, 0), s.getLocation((launch + arrival) / 2));
		assertEquals(new Location(78, 78), s.getLocation(launch * 0.11 + arrival * 0.89));
		assertEquals(new Location(-82, -82), s.getLocation(launch * 0.91 + arrival * 0.09));
	}
}
