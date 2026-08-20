package jp.haruharutv.adguarddns

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.VpnService
import android.os.Bundle
import android.widget.*
import androidx.core.app.ActivityCompat

class MainActivity : Activity() {
    private lateinit var status: TextView
    private lateinit var ssid: EditText
    private lateinit var toggle: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 48, 32, 32)
        }

        TextView(this).apply {
            text = "AdGuard DNS 自動切替"
            textSize = 24f
        }.also(layout::addView)

        status = TextView(this).apply {
            text = "状態: 停止中"
            textSize = 18f
        }.also(layout::addView)

        ssid = EditText(this).apply {
            hint = "無効化するWi-Fi名"
            setText(Prefs(this@MainActivity).ssid)
        }.also(layout::addView)

        toggle = Switch(this).apply {
            text = "自動切替を有効にする"
            isChecked = Prefs(this@MainActivity).enabled
        }.also(layout::addView)

        val save = Button(this).apply {
            text = "保存して適用"
            setOnClickListener { saveAndApply() }
        }.also(layout::addView)

        layout.addView(save)
        setContentView(layout)
        updateStatus()
    }

    private fun saveAndApply() {
        Prefs(this).ssid = ssid.text.toString().trim()
        Prefs(this).enabled = toggle.isChecked

        if (toggle.isChecked) {
            val intent = VpnService.prepare(this)
            if (intent != null) {
                startActivityForResult(intent, 100)
            } else {
                startService(Intent(this, DnsVpnService::class.java))
            }
        } else {
            stopService(Intent(this, DnsVpnService::class.java))
        }
        updateStatus()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            startService(Intent(this, DnsVpnService::class.java))
            updateStatus()
        }
    }

    private fun updateStatus() {
        status.text = if (toggle.isChecked) {
            "状態: VPN DNSサービスを有効化"
        } else {
            "状態: 停止中"
        }
    }
}
