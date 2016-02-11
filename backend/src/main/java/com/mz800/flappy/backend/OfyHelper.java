package com.mz800.flappy.backend;

import com.googlecode.objectify.ObjectifyService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Flappy:
 * Original game created by dB-SOFT in 1984 for SHARP MZ-800 computer.
 * Java version by Petr Slechta, 2014.
 * Android version by Petr Panuska, 2016.
 */
public class OfyHelper implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // This will be invoked as part of a warmup request, or the first user
        // request if no warmup request was invoked.
        ObjectifyService.register(SceneRecord.class);
        ObjectifyService.register(Player.class);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
