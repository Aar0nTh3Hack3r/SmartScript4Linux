package com.wagofix;

import com.wago.cleverscript.print.EZioLib;

public class EZioLibInterceptor {
	private static EZioLib.API instance;

	public static EZioLib.API getInstance() {
		if (instance == null) {
            instance = new ImplementedAPI();
        }
        return instance;
	}
}