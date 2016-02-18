package com.mz800.flappy.backend;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.objectify.ObjectifyService;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.logging.Logger;

/**
 * Flappy:
 * Original game created by dB-SOFT in 1984 for SHARP MZ-800 computer.
 * Java version by Petr Slechta, 2014.
 * Android version by Petr Panuska, 2016.
 */
public class PlayerTest {

    static final Logger logger = Logger.getLogger(PlayerTest.class.getName());

    static{
        ObjectifyService.register(Player.class);
    }

    static SceneService service;

    private static final LocalServiceTestHelper helper =
            new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    @BeforeClass
    public static void setUp() {
        helper.setUp();
        ObjectifyService.begin();

        service = new SceneService();
        service.putPlayer(new Player("id0", "Petrik"));
        service.putPlayer(new Player("id1", "Pavlik"));
        service.putPlayer(new Player("id2", "Honzik"));
        service.putPlayer(new Player("id3", "Tonik"));
        service.putPlayer(new Player("id4", "Vasik"));
    }

    @AfterClass
    public static void tearDown() {
        helper.tearDown();
    }

    @Test
    public void testGet() {


        Player player = service.getPlayer("id0");
        assert player != null;
        assert player.id.equals("id0");
        assert player.name.equals("Petrik");

        player = service.getPlayer("id4");
        assert player != null;
        assert player.id.equals("id4");
        assert player.name.equals("Vasik");
    }

    @Test
    public void testPut() {

        service.putPlayer(new Player("id5", "Vilik"));

        Player player = service.getPlayer("id5");

        assert player != null;
        assert player.id.equals("id5");
        assert player.name.equals("Vilik");

        service.putPlayer(new Player("id2", "Jenik"));

        player = service.getPlayer("id2");
        assert player != null;
        assert player.id.equals("id2");
        assert player.name.equals("Jenik");
    }

    @Test
    public void testOrder() {
        long time = System.currentTimeMillis();
        List<Player> players = service.getPlayersSince(time);
        assert players.size() == 0;

        service.putPlayer(new Player("id0", "Petulka"));
        service.putPlayer(new Player("id1", "Pavlicek"));
        service.putPlayer(new Player("id0", "Peta"));

        players = service.getPlayersSince(time);
        assert players.size() == 2;

        Player petrik, pavlik;
        if (players.get(0).id.equals("id0")) {
            petrik = players.get(0);
            pavlik = players.get(1);
        } else {
            petrik = players.get(1);
            pavlik = players.get(0);
        }

        assert petrik.id.equals("id0");
        assert pavlik.id.equals("id1");
        assert petrik.name.equals("Peta");
        assert pavlik.name.equals("Pavlicek");

        assert petrik.time > pavlik.time;

        players = service.getPlayersSince(petrik.time);
        assert players.size() == 0;
    }
}
