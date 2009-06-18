package com.amplio.rdict;

import java.io.InputStream;

public interface AssetInputStreamProvider {
	public InputStream getAssetInputStream(String path);
}
