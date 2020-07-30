package com.mxxy.game.xml;

import com.mxxy.game.listener.IPanelListener;
import open.xyq.core.script.JaninoScriptEngine;
import open.xyq.core.script.ScriptEngine;

public class ExtendScript {

    private static final ScriptEngine DELEGATE_SCRIPT_ENGINE = ScriptEngine.DEFAULT;

    public IPanelListener loadUIScript(String panel) {
        return DELEGATE_SCRIPT_ENGINE.loadClass(panel, IPanelListener.class);
    }
}
