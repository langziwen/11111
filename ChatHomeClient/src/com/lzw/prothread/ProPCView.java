package com.lzw.prothread;

import java.util.Map;

import com.lzw.window.PCView;

/**
 * 创建私聊窗口的容器，同步线程，取用或删除私聊窗口
 * @author lzw
 *    
 */
public class ProPCView {
	private Map<String,PCView> mapPCView;

	public ProPCView(Map<String, PCView> mapPCView) {
		super();
		this.mapPCView = mapPCView;
	}
	
	public synchronized void put(String key,PCView value){
		mapPCView.put(key, value);
	}
	
	public synchronized PCView get(String key){
		return mapPCView.get(key);
	}
	
	public synchronized void remove(String key){
		mapPCView.remove(key);
	}
	
}
