package com.all.eng.dic.kyy.project;

import java.io.File;

// 이미지 저장소
// 자주 사용하는 이미지는 캐쉬로 로컬에 저장한다.
public class ImageRepository extends ImageCacheHandler {
	private static final String FILE_NAME = "/data/data/app.demo/cache/" + "resource_image.cache";
		
	public static final ImageRepository INSTANCE = new ImageRepository();
	
	private ImageRepository() {
		super(new File(FILE_NAME).exists() ? ImageCache.toImageCache(FILE_NAME)
				: new ImageCache());
	}
	
	public void save() {
		File f = new File(FILE_NAME);
		File parent = f.getParentFile();

		if (!parent.isDirectory()) {
			parent.mkdir();
		}
		ImageCache.fromImageCache(FILE_NAME, getImageCache());
	}
}
