package com.mz800.flappy.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.googlecode.objectify.cmd.Query;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Named;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Flappy:
 * Original game created by dB-SOFT in 1984 for SHARP MZ-800 computer.
 * Java version by Petr Slechta, 2014.
 * Android version by Petr Panuska, 2016.
 * An endpoint class we are exposing
 */
@Api(name = "sceneService", version = "v1", namespace = @ApiNamespace(ownerDomain = "backend.flappy.mz800.com", ownerName = "backend.flappy.mz800.com", packagePath = ""))
public class SceneService {

    private Logger log = Logger.getLogger(SceneService.class.getSimpleName());

    /**
     * Adds a new record or replaces the existing one (based on the record ID).
     * @param record
     */
    @ApiMethod(name="addRecord")
    public void addRecord(SceneRecord record) {
        log.info("Adding record "+record);
        record.date = new Date(System.currentTimeMillis());  //todo do not take the time from the player but calculate it on service side
        ofy().save().entity(record).now();
    }

    @ApiMethod(httpMethod = ApiMethod.HttpMethod.GET, name = "top")
    /**
     *
     * @param records how many records to return
     * @return records are being returned in sorted order
     */
    public List<SceneRecord> top(@Named("sceneNo") int sceneNo, @Named("records") int records) {
        log.info("Giving top list of "+records+" records for scene "+sceneNo);
        Query<SceneRecord> q = ofy().load().type(SceneRecord.class).filter("sceneNo", sceneNo).order("- score").order("- lives").order("attempts").order("date").limit(records);
        return q.list();
    }

    @ApiMethod(name="getPlayer")
    public Player getPlayer(@Named("id") String id) {
        log.info("Giving player having id: "+id);
        return ofy().load().type(Player.class).id(id).now();
    }

    @ApiMethod(name="putPlayer")
    public void putPlayer(Player player) {
        log.info("Putting player "+player);
        ofy().save().entity(player).now();
    }
}
