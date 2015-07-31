package com.all.eng.dic.kyy.project;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import android.graphics.Bitmap;


// 간단한 이미지 리스트를 제공합니다.
// 직렬화를 선언하여서 파일캐쉬로 쓰기를 가능하게 한다. 
public class ImageCache implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3490364931254392574L;
	
	private final Map<String , SynchronizedBitmap> synchronizedMap;
	
	public ImageCache() {
		synchronizedMap = new HashMap<String , SynchronizedBitmap>();
	}
	
	void addBitmapToCache(String url , Bitmap bitmap) {
		// TODO 비트맵을 추가한다. 
		synchronizedMap.put(url, new SynchronizedBitmap(bitmap));	
	}
	
	Bitmap getBitmapFromCache(String url) {
		// TODO 비트맵을 추출한다.
		SynchronizedBitmap bitmap = synchronizedMap.get(url);
		if (bitmap != null)
			return bitmap.get();
		return null;
	}
	
	public void clearCache() {
		// TODO 모든 캐쉬를 지운다.
		synchronizedMap.clear();
	}
		
	public static ImageCache toImageCache (String fileName) {
		ImageCache imageCache = null;
		try {
			imageCache = (ImageCache)ObjectRepository.readObject(fileName);
		} catch (Exception e) {
		}
		return imageCache;  
	}
	
	public static boolean fromImageCache (String fileName , ImageCache cache) {
		// TODO 해당 객체를 파일로 저장한다. 
		try {
			ObjectRepository.saveObject(cache, fileName);
			return true;
		} catch (Exception e) {
		}
		return false;
	}
		
	// 직렬화된 Bitmap 객체를 선언합니다. 
	static final class SynchronizedBitmap implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1859678728937516189L;
		
		private final Bitmap bitmap;
		
		public SynchronizedBitmap(Bitmap bitmap) {
			this.bitmap = bitmap;
		}
		
		public Bitmap get() {
			return bitmap;
		}
	}
}
