package com.jmhxy.core;

/**
 * @deprecated
 */
public class JXYEvent {
	public static final int GAME_EXIT = 0;

	public static final int DLG_GAME_CLOSING = 4095;

	public static final int DLG_FIGURE_STATE = 256;

	public static final int DLG_FRIENDS_LIST = 257;

	public static final int DLG_FRIENDS_ATTRIB = 258;

	public static final int DLG_GIVE = 259;

	public static final int DLG_PET = 260;

	public static final int DLG_PROPS_PACKERS = 261;

	public static final int DLG_RECE_MSG = 262;

	public static final int DLG_SEND_MSG = 263;

	public static final int DLG_CALL_ANIMAL = 264;

	public static final int DLG_CALL_ANIMAL_ENDOWMENT = 265;

	public static final int DLG_CALL_ANIMAL_ATTRIB = 272;

	public static final int DLG_SYS_SETTING = 273;

	public static final int DLG_TASK_HINT = 274;

	public int id;

	public JXYEvent(int id) {
		this.id = id;
	}
}

/*
 * Location: D:\Desktop\WasTools2.0-all.jar!\com\jmhxy\core\JXYEvent.class Java
 * compiler version: 6 (50.0) JD-Core Version: 0.7.1
 */