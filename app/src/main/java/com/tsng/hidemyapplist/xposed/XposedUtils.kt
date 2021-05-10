package com.tsng.hidemyapplist.xposed

import android.content.Context
import com.tsng.hidemyapplist.BuildConfig
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers

object XposedUtils {
    const val resultNo = "HMA No"
    const val resultYes = "HMA Yes"
    const val resultIllegal = "HMA Illegal"
    const val APPNAME = BuildConfig.APPLICATION_ID
    const val SERVICE_VERSION = 22

    @JvmStatic
    fun stopSystemService(context: Context, cleanEnv: Boolean) {
        try {
            context.packageManager.getInstallerPackageName("stopSystemService#$cleanEnv")
        } catch (e: java.lang.IllegalArgumentException) {
            le("stopSystemService: Service not found")
        }
    }

    @JvmStatic
    fun getServiceVersion(context: Context): Int {
        return try {
            context.packageManager.getInstallerPackageName("checkHMAServiceVersion").toInt()
        } catch (e: IllegalArgumentException) {
            0
        }
    }

    @JvmStatic
    fun getServeTimes(context: Context): Int {
        return try {
            context.packageManager.getInstallerPackageName("getServeTimes").toInt()
        } catch (e: IllegalArgumentException) {
            0
        }
    }

    @JvmStatic
    fun getServicePreference(context: Context): String? {
        return try {
            context.packageManager.getInstallerPackageName("getPreference")
        } catch (e: java.lang.IllegalArgumentException) {
            null
        }
    }

    @JvmStatic
    fun callServiceIsUseHook(context: Context, callerName: String?, hookMethod: String): Boolean {
        try {
            val res = context.packageManager.getInstallerPackageName("callIsUseHook#$callerName#$hookMethod")
            if (res == resultIllegal) {
                le("callServiceIsUseHook: Illegal param callIsUseHook#$callerName#$hookMethod")
                return false
            }
            return res == resultYes
        } catch (e: IllegalArgumentException) {
            le("callServiceIsUseHook: Service not found")
            return false
        }
    }

    @JvmStatic
    fun callServiceIsToHide(context: Context, callerName: String?, pkgstr: String?, fileHook: Boolean): Boolean {
        try {
            val res = if (fileHook) context.packageManager.getInstallerPackageName("callIsHideFile#$callerName#$pkgstr")
            else context.packageManager.getInstallerPackageName("callIsToHide#$callerName#$pkgstr")
            if (res == resultIllegal) {
                le("callServiceIsToHide: Illegal param callIsUseHook#$callerName#$pkgstr")
                return false
            }
            return res == resultYes
        } catch (e: IllegalArgumentException) {
            le("callServiceIsToHide: Service not found")
            return false
        }
    }

    @JvmStatic
    fun getRecursiveField(entry: Any, list: List<String>): Any? {
        var field: Any? = entry
        for (it in list)
            field = XposedHelpers.getObjectField(field, it) ?: return null
        return field
    }

    @JvmStatic
    fun ld(log: String) {
        XposedBridge.log("[HMA Xposed] [DEBUG] $log")
    }

    @JvmStatic
    fun li(log: String) {
        XposedBridge.log("[HMA Xposed] [INFO] $log")
    }

    @JvmStatic
    fun le(log: String) {
        XposedBridge.log("[HMA Xposed] [ERROR] $log")
    }
}