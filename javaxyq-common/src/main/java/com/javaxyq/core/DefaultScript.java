package com.javaxyq.core;

import com.javaxyq.event.PanelListener;
import com.javaxyq.event.SceneListener;
import lombok.extern.slf4j.Slf4j;
import open.xyq.core.script.JaninoScriptEngine;

import java.util.EventListener;

/**
 * 默认脚本语言引擎（java）
 */
@Slf4j
public class DefaultScript implements ScriptEngine {

    public static DefaultScript getInstance() {
        return INSTANCE;
    }

    private static final DefaultScript INSTANCE = new DefaultScript();

    private static final JaninoScriptEngine DELEGATE_SCRIPT_ENGINE = JaninoScriptEngine.INSTANCE;

    @Override
    public EventListener loadNPCScript(String npcId) {
        return DELEGATE_SCRIPT_ENGINE.loadClass("npc.n" + npcId, EventListener.class);
    }

    @Override
    public SceneListener loadSceneScript(String sceneId) {
        return DELEGATE_SCRIPT_ENGINE.loadClass("scene.s" + sceneId, SceneListener.class);
    }

    @Override
    public PanelListener loadUIScript(String id) {
        return DELEGATE_SCRIPT_ENGINE.loadClass("ui." + id, PanelListener.class);
    }
}
