package org.hermes.core;

import org.hermes.core.entities.position.PositionManager;
import org.hermes.core.exceptions.HermesException;

/**
 * Created by Almaz
 * Date: 15.10.2015
 */
public interface Strategy {

    void onBarOpen() throws HermesException;

    void onBarClose() throws HermesException;

    void onTick() throws HermesException;

    PositionManager getPositionManager();

    String getStrategyName();
}
