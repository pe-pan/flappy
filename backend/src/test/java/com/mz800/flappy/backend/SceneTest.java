package com.mz800.flappy.backend;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.objectify.ObjectifyService;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * Flappy:
 * Original game created by dB-SOFT in 1984 for SHARP MZ-800 computer.
 * Java version by Petr Slechta, 2014.
 * Android version by Petr Panuska, 2016.
 */
public class SceneTest {

    static final Logger logger = Logger.getLogger(SceneTest.class.getName());

    static{
        ObjectifyService.register(SceneRecord.class);
    }

    static SceneService service;

    private static final LocalServiceTestHelper helper =
            new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    @BeforeClass
    public static void setUp() {
        helper.setUp();
        ObjectifyService.begin();

        service = new SceneService();
        service.addRecord(new SceneRecord(SCENE_NO_1, "Petr", 200, 5, 1, new Date()));
        service.addRecord(new SceneRecord(SCENE_NO_1, "Honza", 200, 5, 1, new Date()));
        service.addRecord(new SceneRecord(SCENE_NO_1, "Karlik", 201, 5, 1, new Date()));
        service.addRecord(new SceneRecord(SCENE_NO_1, "Tonik", 202, 4, 2, new Date()));
        service.addRecord(new SceneRecord(SCENE_NO_1, "Vasik", 202, 3, 2, new Date()));
        service.addRecord(new SceneRecord(SCENE_NO_1, "Vilik", 203, 2, 1, new Date()));
        service.addRecord(new SceneRecord(SCENE_NO_2, "Tobik", 202, 3, 1, new Date()));
        service.addRecord(new SceneRecord(SCENE_NO_1, "Pepik", 202, 3, 1, new Date()));

        for (int i = 0; i < 15; i++) {
            service.addRecord(new SceneRecord(SCENE_NO_3, "Player"+i, 200, 5, 1, new Date()));
        }

        // adds 3 records but as they all concern the same scene and player, only one is actually added (the last one)
        service.addRecord(new SceneRecord(SCENE_NO_4, "Player_scene_"+SCENE_NO_4, 200, 5, 1, new Date()));
        service.addRecord(new SceneRecord(SCENE_NO_4, "Player_scene_"+SCENE_NO_4, 205, 5, 1, new Date()));
        service.addRecord(new SceneRecord(SCENE_NO_4, "Player_scene_"+SCENE_NO_4, 210, 5, 1, new Date()));


        List<SceneRecord> results;
        do {
            results = service.top(SCENE_NO_1, 10);
            logger.info("Results: " + results.size());
        } while (results.size() < 7);

        do {
            results = service.top(SCENE_NO_2, 10);
            logger.info("Results: " + results.size());
        } while (results.size() < 1);

        do {
            results = service.top(SCENE_NO_3, 15);
            logger.info("Results: " + results.size());
        } while (results.size() < 15);

        do {
            results = service.top(SCENE_NO_4, 10);
            logger.info("Results: " + results.size());
        } while (results.size() < 1);
    }

    @AfterClass
    public static void tearDown() {
        helper.tearDown();
    }

    private static final int SCENE_NO_1 = 1;
    private static final int SCENE_NO_2 = 2;
    private static final int SCENE_NO_3 = 3;
    private static final int SCENE_NO_4 = 4;

    @Test
    public void testOrder() {
        List<SceneRecord> results = service.top(SCENE_NO_1, 10);

        assert results.size() == 7;
        assert results.get(0).playerId.equals("Vilik"); // top score
        assert results.get(1).playerId.equals("Tonik"); // top lives
        assert results.get(2).playerId.equals("Pepik"); // less attempts
        assert results.get(3).playerId.equals("Vasik");
        assert results.get(4).playerId.equals("Karlik");
        assert results.get(5).playerId.equals("Petr");  // sooner
        assert results.get(6).playerId.equals("Honza");
    }

    @Test
    public void testDifferentScenes() {
        List<SceneRecord> results = service.top(SCENE_NO_2, 10);

        assert results.size() == 1;
        assert results.get(0).playerId.equals("Tobik");
    }

    @Test
    public void testLimit() {
        List<SceneRecord> results;
        results = service.top(SCENE_NO_3, 10);
        assert results.size() == 10;
    }

    @Test
    public void testReplace() {
        List<SceneRecord> results;
        results = service.top(SCENE_NO_4, 10);
        assert results.size() == 1;

        assert results.get(0).score == 210;
    }
}
