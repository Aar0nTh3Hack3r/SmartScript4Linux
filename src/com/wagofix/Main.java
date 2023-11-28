package com.wagofix;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Path;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

public class Main {
	public static void main(String[] args) {
		//System.out.println("It Works!");

		// Fix Shell32Util
		new ByteBuddy()
			.redefine(com.sun.jna.platform.win32.Shell32Util.class)
			.method(ElementMatchers.named("getFolderPath").and(ElementMatchers.takesArguments(int.class)))
			.intercept(MethodDelegation.to(Shell32UtilInterceptor.class))
			.make()
			.load(Thread.currentThread().getContextClassLoader(), ClassReloadingStrategy.fromInstalledAgent());
		
			// Fix LocalizationManager
		new ByteBuddy()
			.redefine(com.wago.cleverscript.i18n.LocalizationManager.class)
			.method(ElementMatchers.named("getInstalledResourceFolderPath"))
			.intercept(MethodDelegation.to(LocalizationManagerInterceptor.class)) //FixedValue.value("Bar")
			.make()
			.load(Thread.currentThread().getContextClassLoader(), ClassReloadingStrategy.fromInstalledAgent());

		// Fix EZioLib
		new ByteBuddy()
			.redefine(com.wago.cleverscript.print.EZioLib.class)
			.method(ElementMatchers.named("getInstance"))
			.intercept(MethodDelegation.to(EZioLibInterceptor.class))
			.make()
			.load(Thread.currentThread().getContextClassLoader(), ClassReloadingStrategy.fromInstalledAgent());

		// Fix EzioNetConnection
		Class<?> ezioNetConnectionClass = null;
		try {
			ezioNetConnectionClass = Class.forName("com.wago.cleverscript.print.godex.EzioNetConnection");
		} catch(Exception e) {}
		if(ezioNetConnectionClass != null) {
			new ByteBuddy()
				.redefine(ezioNetConnectionClass)
				.method(ElementMatchers.named("canPingIp"))
				.intercept(FixedValue.value(true))
				.make()
				.load(Thread.currentThread().getContextClassLoader(), ClassReloadingStrategy.fromInstalledAgent());
		}

		com.wago.cleverscript.WagoMainApplication.main(args);
	}

	public static <T> Path getPathToJar(Class<T> c) throws URISyntaxException {
		return new File(c.getProtectionDomain().getCodeSource().getLocation().toURI()).toPath();
	}

}
