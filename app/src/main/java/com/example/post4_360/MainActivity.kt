package com.example.post4_360

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.post4_360.R
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var layoutData: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = AppDatabase.getDatabase(this)
        layoutData = findViewById(R.id.layoutData)

        val nama = findViewById<EditText>(R.id.etNamaLengkap)
        val nik = findViewById<EditText>(R.id.etNIK)
        val kabupaten = findViewById<EditText>(R.id.etKabupaten)
        val kecamatan = findViewById<EditText>(R.id.etKecamatan)
        val desa = findViewById<EditText>(R.id.etDesa)
        val rt = findViewById<EditText>(R.id.etRT)
        val rw = findViewById<EditText>(R.id.etRW)
        val rbMale = findViewById<RadioButton>(R.id.rbMale)
        val rbFemale = findViewById<RadioButton>(R.id.rbFemale)
        val spinner = findViewById<Spinner>(R.id.spStatus)
        val btnSimpan = findViewById<Button>(R.id.btnSimpan)
        val btnReset = findViewById<Button>(R.id.btnReset)

        val statusList = arrayOf("Belum Menikah", "Menikah", "Cerai Hidup", "Cerai Mati")
        spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, statusList)

        tampilkanData()

        btnSimpan.setOnClickListener {
            if (nama.text.isEmpty() || nik.text.isEmpty() || kabupaten.text.isEmpty() ||
                kecamatan.text.isEmpty() || desa.text.isEmpty() ||
                rt.text.isEmpty() || rw.text.isEmpty() || (!rbMale.isChecked && !rbFemale.isChecked)
            ) {
                AlertDialog.Builder(this)
                    .setTitle("Peringatan")
                    .setMessage("Semua field wajib diisi!")
                    .setPositiveButton("OK", null)
                    .show()
            } else {
                val gender = if (rbMale.isChecked) "Laki-laki" else "Perempuan"
                val status = spinner.selectedItem.toString()

                val data = DataEntity(
                    nama = nama.text.toString(),
                    nik = nik.text.toString(),
                    kabupaten = kabupaten.text.toString(),
                    kecamatan = kecamatan.text.toString(),
                    desa = desa.text.toString(),
                    rt = rt.text.toString(),
                    rw = rw.text.toString(),
                    gender = gender,
                    status = status
                )

                lifecycleScope.launch {
                    db.dataDao().insert(data)
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "Data berhasil disimpan!", Toast.LENGTH_SHORT).show()
                        tampilkanData()
                    }
                }
            }
        }

        btnReset.setOnClickListener {
            lifecycleScope.launch {
                db.dataDao().deleteAll()
                runOnUiThread {
                    nama.text.clear()
                    nik.text.clear()
                    kabupaten.text.clear()
                    kecamatan.text.clear()
                    desa.text.clear()
                    rt.text.clear()
                    rw.text.clear()
                    rbMale.isChecked = false
                    rbFemale.isChecked = false
                    spinner.setSelection(0)
                    layoutData.removeAllViews()
                    Toast.makeText(this@MainActivity, "Semua data dihapus!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun tampilkanData() {
        lifecycleScope.launch {
            val list = db.dataDao().getAll()
            runOnUiThread {
                layoutData.removeAllViews()
                for (d in list) {
                    val item = layoutInflater.inflate(R.layout.item_citizen, null)
                    item.findViewById<TextView>(R.id.tvCitizenName).text = d.nama
                    item.findViewById<TextView>(R.id.tvCitizenInfo).text =
                        "NIK: ${d.nik}\n${d.kabupaten}, ${d.kecamatan}, ${d.desa}\nRT/RW: ${d.rt}/${d.rw}\n${d.gender} - ${d.status}"
                    layoutData.addView(item)
                }
            }
        }
    }
}
