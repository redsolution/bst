package ru.redsolution.bst.data;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * Предоставляет информацию о том, включена ли отладка.
 * 
 * @author alexander.ivanov
 * 
 */
public class Debugger {

	public static final boolean ENABLED;

	private static Method _getApplicationInfo = null;

	static {
		initCompatibility();
		ENABLED = (getApplicationInfo(BST.getInstance()).flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
	};

	private static void initCompatibility() {
		try {
			_getApplicationInfo = Context.class.getMethod("getApplicationInfo",
					new Class[] {});
		} catch (NoSuchMethodException nsme) {
		}
	}

	private static ApplicationInfo getApplicationInfo(Context context) {
		ApplicationInfo applicationInfo;
		if (_getApplicationInfo != null) {
			try {
				applicationInfo = (ApplicationInfo) _getApplicationInfo
						.invoke(context);
			} catch (InvocationTargetException e) {
				Throwable cause = e.getCause();
				if (cause instanceof RuntimeException) {
					throw (RuntimeException) cause;
				} else if (cause instanceof Error) {
					throw (Error) cause;
				} else {
					throw new RuntimeException(e);
				}
			} catch (IllegalAccessException ie) {
				throw new RuntimeException(ie);
			}
		} else {
			try {
				applicationInfo = context.getPackageManager()
						.getApplicationInfo(context.getPackageName(), 0);
			} catch (NameNotFoundException e) {
				applicationInfo = new ApplicationInfo();
				applicationInfo.flags = 0; // Disabled
			}
		}
		return applicationInfo;
	}

	private Debugger() {
	}

}
