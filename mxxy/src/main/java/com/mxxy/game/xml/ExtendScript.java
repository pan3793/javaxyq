package com.mxxy.game.xml;

import com.mxxy.game.listener.IPanelListener;
import open.xyq.core.script.JaninoScriptEngine;

public class ExtendScript {

    public IPanelListener loadUIScript(String panel) {
        return JaninoScriptEngine.INSTANCE.loadClass(panel, IPanelListener.class);
    }
}
