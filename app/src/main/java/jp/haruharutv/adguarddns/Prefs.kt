package jp.haruharutv.adguarddns

import android.content.Context

class Prefs(context: Context) {
    private val p = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    var ssid: String
        get() = p.getString("ssid", "NETGG-BYOD") ?: "NETGG-BYOD"
        set(v) { p.edit().putString("ssid", v).apply() }

    var enabled: Boolean
        get() = p.getBoolean("enabled", true)
        set(v) { p.edit().putBoolean("enabled", v).apply() }
}
