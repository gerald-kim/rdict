package com.amplio.vcbl8r.search;

import java.io.InputStream;

public interface AssetInputStreamProvider {
	public InputStream getAssetInputStream(String path);
}
