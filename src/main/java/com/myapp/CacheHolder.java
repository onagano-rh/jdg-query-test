package com.myapp;

import org.infinispan.Cache;
import org.infinispan.manager.DefaultCacheManager;

public class CacheHolder {
	private static DefaultCacheManager manager = null;
	private static Cache<String, CacheEntity> ivdPositionCache = null;
	public static DefaultCacheManager getManager() {
		return manager;
	}
	public static void setManager(DefaultCacheManager manager) {
		CacheHolder.manager = manager;
	}
	public static Cache<String, CacheEntity> getIvdPositionCache() {
		return ivdPositionCache;
	}
	public static void setIvdPositionCache(Cache<String, CacheEntity> cache) {
		ivdPositionCache = cache;
	}
	
}
