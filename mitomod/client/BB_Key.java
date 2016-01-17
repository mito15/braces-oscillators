package com.mito.mitomod.client;

public class BB_Key {

	public int key = 0;

	public BB_Key(boolean ctrl, boolean shift, boolean alt) {
		int b = 0;

		if (ctrl) {
			b = b | 1;
		}
		if (shift) {
			b = b | 2;
		}
		if (alt) {
			b = b | 4;
		}
		this.key = b;
	}

	public BB_Key(int key) {
		this.key = key;
	}

	public boolean isControlPressed() {
		return (key & 1) == 1;
	}

	public boolean isShiftPressed() {
		return (key & 2) == 2;
	}

	public boolean isAltPressed() {
		return (key & 4) == 4;
	}

}
