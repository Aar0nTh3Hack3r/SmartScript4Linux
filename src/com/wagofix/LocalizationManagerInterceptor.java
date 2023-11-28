package com.wagofix;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Path;

public class LocalizationManagerInterceptor {
	public static Path getInstalledResourceFolderPath() throws URISyntaxException {
		return Main.getPathToJar(com.wago.cleverscript.i18n.LocalizationManager.class).resolveSibling("");
	}
}