package com.javaxyq.core;

import java.util.EventListener;

import com.javaxyq.event.PanelListener;
import com.javaxyq.event.SceneListener;

public interface ScriptEngine {

    default void clearCache() {
    }

    PanelListener loadUIScript(String id);

    EventListener loadNPCScript(String npcId);

    SceneListener loadSceneScript(String sceneId);
}