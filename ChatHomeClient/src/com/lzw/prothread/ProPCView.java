package com.lzw.prothread;

import java.util.Map;

import com.lzw.window.PCView;

/**
 * ����˽�Ĵ��ڵ�������ͬ���̣߳�ȡ�û�ɾ��˽�Ĵ���
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
