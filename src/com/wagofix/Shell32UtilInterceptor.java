package com.wagofix;

import java.net.URISyntaxException;

public class Shell32UtilInterceptor {
	public static String getFolderPath(int csidl) throws URISyntaxException {
		return Main.getPathToJar(com.wago.cleverscript.WagoMainApplication.class).resolveSibling("").resolveSibling("").resolveSibling("userappdata").toString();
	}
}