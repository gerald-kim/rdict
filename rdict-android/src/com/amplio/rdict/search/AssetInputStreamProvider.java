package com.amplio.rdict.search;

import java.io.InputStream;

public interface AssetInputStreamProvider {
	public InputStream getAssetInputStream(String path);
}
