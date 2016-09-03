package com.mito.mitomod.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class mitoLogger {

	public static Logger logger = LogManager.getLogger("mitomod");

	/*
	 * 以下のメソッドはわざわざ呼び出さなくても、
	 * TutorialLogger.logger.trace(msg);等で呼び出せば事足りるものではある。
	 * 出力するログに、定型文やエラーログなどを含めて出したい場合は、以下のようなメソッドを好きにカスタマイズして、
	 * ログを出したい場所でこのクラスのメソッドを呼ぶようにすると、少し手間を省略できる。
	 */
	public static void trace(String msg) {
		logger.trace(msg);
	}

	public static void info(String msg) {
		if(BAO_main.debug)logger.info(msg);
	}

	public static void warn(String msg) {
		logger.warn(msg);
	}

	public static void info() {
		logger.info("message");
	}

	public static void info(double... list) {
		String s = "";
		for (int n = 0; n < list.length; n++) {
			s = s + " value" + n + " = " + list[n] + " |";
		}
		logger.info(s);
	}

}
