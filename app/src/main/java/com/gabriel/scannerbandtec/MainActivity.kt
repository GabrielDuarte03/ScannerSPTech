package com.gabriel.scannerbandtec

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.*
import org.apache.poi.hssf.usermodel.HSSFCell
import org.apache.poi.hssf.usermodel.HSSFRow
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception


private const val CAMERA_REQUEST_CODE = 101

class MainActivity : AppCompatActivity() {


    private lateinit var codeScanner: CodeScanner
    var serial = ""
    var modelo = ""
    var hsl = ""
    var patri = ""


    var file: File = File(Environment.getExternalStoragePublicDirectory(
        Environment.DIRECTORY_DOWNLOADS).toString() + "/maquinas.xls")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        permissoes()
        codeScanner()
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ),
            PackageManager.PERMISSION_GRANTED
        )


        escreverDadosLegados()
        val btnInserir = findViewById<Button>(R.id.btnInserir) as Button

        btnInserir.setOnClickListener {

            inserirSemCod()

        }

        val btnExportar = findViewById<Button>(R.id.btnExportar) as Button

        btnExportar.setOnClickListener {

           exportarCSV()


        }



    }


    private fun exportarCSV(){

        var dao = MaquinaDAO(this)
        var maquinas = dao.listar()
        var modeloT = ""
        var hslT = ""
        var snT = ""
        var patriT = ""
        var i = 1
        var hssfWorkbook = HSSFWorkbook()
        var hssfSheet = hssfWorkbook.createSheet()
        var hssfRow:HSSFRow
        var hssfCell:HSSFCell

        hssfRow = hssfSheet.createRow(0)
        hssfCell= hssfRow.createCell(0)

        hssfCell.setCellValue("Modelo")
        hssfCell= hssfRow.createCell(1)
        hssfCell.setCellValue("HSL")
        hssfCell= hssfRow.createCell(2)
        hssfCell.setCellValue("Serial Number")
        hssfCell= hssfRow.createCell(3)
        hssfCell.setCellValue("Patrimônio")
        hssfCell= hssfRow.createCell(4)


        for(maquina in maquinas) {



             hssfRow = hssfSheet.createRow(i)
             hssfCell= hssfRow.createCell(0)

            hssfCell.setCellValue(maquina.modelo)

            hssfCell = hssfRow.createCell(1)
            hssfCell.setCellValue(maquina.hsl)

            hssfCell = hssfRow.createCell(2)
            hssfCell.setCellValue(maquina.serial)

            hssfCell = hssfRow.createCell(3)
            hssfCell.setCellValue(maquina.patrimonio)

            modeloT =  maquina.modelo
            hslT =  maquina.hsl
            snT = maquina.serial
            patriT = maquina.patrimonio

            i++
        }


        try {
            if(!file.exists()){
                file.createNewFile()
            }

            var fileOutputStream = FileOutputStream(file)
            hssfWorkbook.write(fileOutputStream)

            if(fileOutputStream!=null){
                fileOutputStream.flush()
                fileOutputStream.close()
            }

        }catch (e:Exception){

            System.out.println(e)
        }


    }


    private fun escreverDadosLegados(){


        var dao = MaquinaDAO(this)
        var maquinas = dao.listar()


        for(maquina in maquinas) {


            val tabela = findViewById<View>(R.id.tabela) as TableLayout


            val tr: View = LayoutInflater.from(this).inflate(R.layout.linha, tabela, false)

            val modelo = tr.findViewById<View>(R.id.modelo) as TextView


            val hsl = tr.findViewById<View>(R.id.hsl) as TextView


            val sn = tr.findViewById<View>(R.id.sn) as TextView


            val patrimonio = tr.findViewById<View>(R.id.patri) as TextView


            modelo.text = maquina.modelo
            hsl.text = maquina.hsl
            sn.text = maquina.serial
            patrimonio.text = maquina.patrimonio


            tabela.addView(
                tr,
                TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT
                )
            )
        }

        }







    private fun escreverTabela(modeloT: String, hslT: String, serialT: String, patriT: String) {




        var dao = MaquinaDAO(this)
        var maquinaC = Maquina()

        val tabela = findViewById<View>(R.id.tabela) as TableLayout


            val tr: View = LayoutInflater.from(this).inflate(R.layout.linha, tabela, false)

            val modelo = tr.findViewById<View>(R.id.modelo) as TextView
            modelo.text = modeloT

            val hsl = tr.findViewById<View>(R.id.hsl) as TextView
            hsl.text = hslT

            val sn = tr.findViewById<View>(R.id.sn) as TextView
            sn.text = serialT

            val patrimonio = tr.findViewById<View>(R.id.patri) as TextView
            patrimonio.text = patriT


            tabela.addView(
                tr,
                TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT
                )
            )

        maquinaC.hsl = hslT;
        maquinaC.modelo = modeloT;
        maquinaC.patrimonio = patriT;
        maquinaC.serial = serialT;

        dao.inserir(maquinaC)






    }

    private fun inserirSemCod(){

        val hslInput = EditText(this@MainActivity)
        val patriInput = EditText(this@MainActivity)
        val serialInput = EditText(this@MainActivity)
        val spinner = Spinner(this@MainActivity)

       hslInput.inputType = 2
        patriInput.inputType = 2



        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.maquinas, R.layout.spinner
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setTitle("Serial Number")
        builder.setMessage("Insira o Serial Number.")


        if (serialInput.parent != null) (serialInput.parent as ViewGroup).removeView(serialInput) // <- fix

        builder.setView(serialInput)

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->



            serial = serialInput.text.toString();

            val builder = AlertDialog.Builder(this@MainActivity)
            builder.setTitle("Modelo da máquina")
            builder.setMessage("Selecione o modelo da máquina:")

            if (spinner.parent != null) (spinner.parent as ViewGroup).removeView(spinner) // <- fix

            builder.setView(spinner)


            //builder.setView(spinner)
            /*2222222222 -------------*/
            builder.setPositiveButton(android.R.string.yes) { dialog, which ->


                //recuperando modelo da máquina de um spinner
                modelo = spinner.selectedItem.toString()
                spinner.setSelection(0)

                val builder = AlertDialog.Builder(this@MainActivity)
                builder.setTitle("HSL")
                builder.setMessage("Insira o HSL da máquina (somente números)")
                //builder.setView(input)


                if (hslInput.parent != null) (hslInput.parent as ViewGroup).removeView(hslInput) // <- fix

                builder.setView(hslInput)

                /*33333333333 -------------*/
                builder.setPositiveButton(android.R.string.yes) { dialog, which ->

                    hsl = hslInput.text.toString()
                    hslInput.text.clear()

                    val builder = AlertDialog.Builder(this@MainActivity)
                    builder.setTitle("Patrimônio")
                    builder.setMessage("Insira o número de patrimônio da máquina (somente números)")

                    //builder.setView(patriInput)

                    if (patriInput.parent != null) (patriInput.parent as ViewGroup).removeView(patriInput) // <- fix

                    builder.setView(patriInput)

                    // 333333 -------------
                    builder.setPositiveButton(android.R.string.yes) { dialog, which ->

                        patri = patriInput.text.toString()
                        patriInput.text.clear()

                        val builder = AlertDialog.Builder(this@MainActivity)
                        builder.setTitle("Confirmação")
                        builder.setMessage("Os dados estão certos?\n\nModelo: $modelo\nHSL: $hsl\nSerial Number: $serial\nNº Patrimônio: $patri")




                        builder.setPositiveButton(android.R.string.yes) { dialog, which ->

                            escreverTabela(modelo, hsl, serial, patri)

                        }
                        builder.setNegativeButton(android.R.string.no) { dialog, which ->

                        }
                        builder.show()
                    }
                    builder.setNegativeButton(android.R.string.no) { dialog, which ->

                    }
                    builder.show()
                }
                builder.setNegativeButton(android.R.string.no) { dialog, which ->

                }
                builder.show()
            }
            builder.setNegativeButton(android.R.string.no) { dialog, which ->

            }
            builder.show()
        }
        builder.setNegativeButton(android.R.string.no) { dialog, which ->

        }
        builder.show()

    }


    private fun codeScanner() {


        val hslInput = EditText(this@MainActivity)
        val patriInput = EditText(this@MainActivity)
        val spinner = Spinner(this@MainActivity)

        val scannerView = findViewById<CodeScannerView>(R.id.scanner_view)
        val tabela = findViewById<TableLayout>(R.id.tabela)


        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.maquinas, R.layout.spinner
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        //tabela.addView()
        codeScanner = CodeScanner(this, scannerView)

        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS

            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.SINGLE
            isAutoFocusEnabled = true
            isFlashEnabled = false

            decodeCallback = DecodeCallback {
                runOnUiThread {


                    val builder = AlertDialog.Builder(this@MainActivity)
                    builder.setTitle("Confirmar Serial Number")
                    builder.setMessage("O Serial Number " + it + " está correto?")

                    builder.setPositiveButton(android.R.string.yes) { dialog, which ->

                        serial = it.toString()

                        val builder = AlertDialog.Builder(this@MainActivity)
                        builder.setTitle("Modelo da máquina")
                        builder.setMessage("Selecione o modelo da máquina:")

                        if (spinner.parent != null) (spinner.parent as ViewGroup).removeView(spinner) // <- fix

                        builder.setView(spinner)




                        builder.setPositiveButton(android.R.string.yes) { dialog, which ->


                            //recuperando modelo da máquina de um spinner
                            modelo = spinner.selectedItem.toString()
                            spinner.setSelection(0)

                            val builder = AlertDialog.Builder(this@MainActivity)
                            builder.setTitle("HSL")
                            builder.setMessage("Insira o HSL da máquina (somente números)")



                            if (hslInput.parent != null) (hslInput.parent as ViewGroup).removeView(hslInput) // <- fix

                            builder.setView(hslInput)


                            builder.setPositiveButton(android.R.string.yes) { dialog, which ->

                                hsl = hslInput.text.toString()
                                hslInput.text.clear()

                                val builder = AlertDialog.Builder(this@MainActivity)
                                builder.setTitle("Patrimônio")
                                builder.setMessage("Insira o número de patrimônio da máquina (somente números)")



                                if (patriInput.parent != null) (patriInput.parent as ViewGroup).removeView(patriInput) // <- fix

                                builder.setView(patriInput)


                                builder.setPositiveButton(android.R.string.yes) { dialog, which ->

                                    patri = patriInput.text.toString()
                                    patriInput.text.clear()

                                    val builder = AlertDialog.Builder(this@MainActivity)
                                    builder.setTitle("Confirmação")
                                    builder.setMessage("Os dados estão certos?\n\nModelo: $modelo\nHSL: $hsl\nSerial Number: $serial\nNº Patrimônio: $patri")




                                    builder.setPositiveButton(android.R.string.yes) { dialog, which ->

                                        escreverTabela(modelo, hsl, serial, patri)

                                    }
                                    builder.setNegativeButton(android.R.string.no) { dialog, which ->

                                    }
                                    builder.show()
                                }
                                builder.setNegativeButton(android.R.string.no) { dialog, which ->

                                }
                                builder.show()
                            }
                            builder.setNegativeButton(android.R.string.no) { dialog, which ->

                            }
                            builder.show()
                        }
                        builder.setNegativeButton(android.R.string.no) { dialog, which ->

                        }
                        builder.show()
                    }
                    builder.setNegativeButton(android.R.string.no) { dialog, which ->

                    }
                    builder.show()


                }


            }

            errorCallback = ErrorCallback {

                runOnUiThread {
                    Log.e("Main", "Erro de inicialização na câmera: ${it.message}")
                }

            }
        }

        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }

    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    private fun permissoes() {
        val permissao = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)

        if (permissao != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }

    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.CAMERA),
            CAMERA_REQUEST_CODE
        )

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(
                        this,
                        "Preciso da permissão da sua câmera para scannear!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}