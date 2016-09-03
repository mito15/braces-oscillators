package com.mito.mitomod.BraceBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mito.mitomod.common.mitoLogger;

public class BB_BindHelper {

	public Map<Integer, List> bindedMap = new HashMap<Integer, List>();
	public Map<Integer, List> bindingMap = new HashMap<Integer, List>();
	public BB_DataWorld dataworld;

	public BB_BindHelper(BB_DataWorld world) {
		this.dataworld = world;
	}

	//本体のBraceのID、関連付けられるBraceのID、関連付けする場所のID
	public void register(int base, int id, int location) {
		BraceBase base1 = this.dataworld.getBraceBaseByID(id);
		if (base1 != null) {
			this.register(base1, base, location);
		} else {
			mitoLogger.info("register " + id);
			if (bindedMap.containsKey(new Integer(id))) {
				bindedMap.get(new Integer(id)).add(new BB_BindLocation(base, location));
			} else {
				List list = new ArrayList();
				list.add(new BB_BindLocation(base, location));
				bindedMap.put(new Integer(id), list);
			}
		}
	}

	public void register(BraceBase base, int id, int location) {
		mitoLogger.info("register ing " + id);
		if (bindingMap.containsKey(new Integer(id))) {
			bindingMap.get(new Integer(id)).add(new BB_BindLocation(base, location));
		} else {
			List list = new ArrayList();
			list.add(new BB_BindLocation(base, location));
			bindingMap.put(new Integer(id), list);
		}
	}

	public void call(BraceBase base) {
		Integer id = new Integer(base.BBID);
		mitoLogger.info("call 0 " + id + bindedMap.size());
		if (bindedMap.containsKey(id)) {
			mitoLogger.info("call 1 " + id);
			List list = bindedMap.get(id);
			bindedMap.remove(id);
			mitoLogger.info("call 2 " + list.size());
			for (int n = 0; n < list.size(); n++) {
				BB_BindLocation loc = (BB_BindLocation) list.get(n);
				mitoLogger.info("call 3 " + (loc != null));
				if (loc != null) {
					BraceBase base1 = base.dataworld.getBraceBaseByID(loc.id);
					if (base1 != null) {
						base1.addBrace(base, loc.location);
						mitoLogger.info("call 4");
					} else {
						register(base, loc.id, loc.location);
					}
				}
			}
		}
		if (bindingMap.containsKey(id)) {
			List list = bindingMap.get(id);
			bindingMap.remove(id);
			for (int n = 0; n < list.size(); n++) {
				BB_BindLocation loc = (BB_BindLocation) list.get(n);
				if (loc != null) {
					base.addBrace(loc.base, loc.location);
				}
			}
		}
	}

	public void onUpdate() {

	}

}
