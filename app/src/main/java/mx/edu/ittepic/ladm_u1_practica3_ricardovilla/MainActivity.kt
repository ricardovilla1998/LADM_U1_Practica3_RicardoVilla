package mx.edu.ittepic.ladm_u1_practica3_ricardovilla

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*

class MainActivity : AppCompatActivity() {

    val vector : Array<Int> = Array(2000,{0})
    //val vectorAux : Array<Int> = Array(9,{0})
    var data = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //TAMBIEN SE PONE EN EL MANIFEST
        if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){

            //PERMISO NO CONCEDIDO, ENTONCES SE SOLICITA
            ActivityCompat.requestPermissions(this,arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.READ_EXTERNAL_STORAGE),0)
        }
        else{
            mensaje("LOS PERMISOS YA FUERON OTORGADOS")
        }

        asignar.setOnClickListener {

            insertarEnVector()
        }

        mostrar.setOnClickListener {

            data = ""
            (0..9).forEach {ren->



                data += vector[ren]
                //data+= "["+matriz[ren][col]+"],"
                data += ","
            }

            valoresMostrar.setText(data)

        }

        guardar.setOnClickListener {

            guardarAchivoSD()

        }

        leer.setOnClickListener {
            leerArchivoSD()
        }

    }


    private fun insertarEnVector() {

        if(valor.text.isEmpty()||posicion.text.isEmpty()||posicion.text.toString().toInt()>9){

            mensaje("ERROR EL VALOR Y EL CAMPO DEBEN ASIGNARSE CORRECTAMENTE")
            return

        }

        vector[posicion.text.toString().toInt()] = valor.text.toString().toInt()

        /*data = ""
        (0..9).forEach {ren->



            data += vector[ren]
            //data+= "["+matriz[ren][col]+"],"
            data += ","
        }*/
        limpiarCampos()
        mensaje("SE INSERTO EL VECTOR")
    }


    fun guardarAchivoSD(){

        if (noSD()){
            mensaje("NO HAY MEMORIA EXTERNA")
            return
        }
        if(nombreA.text.isEmpty()){
            mensaje("DEBES PONERLE UN NOMBRE AL ARCHIVO")
            return
        }
        try {
            var archivo = nombreA.text.toString()+".txt"
            var rutaSD = Environment.getExternalStorageDirectory()
            var datosArchivo = File(rutaSD.absolutePath,archivo)
            var flujoSalida = OutputStreamWriter(FileOutputStream(datosArchivo))
            //data = valoresMostrar.text.toString()

            data = ""
            (0..9).forEach {ren->



                data += vector[ren]
                //data+= "["+matriz[ren][col]+"],"
                data += ","
            }


            flujoSalida.write(data)
            flujoSalida.flush()
            flujoSalida.close()
            mensaje("¡EL ARCHIVO FUE CREADO CON ÉXITO!")


        }catch (error: IOException){
            mensaje(error.message.toString())
        }
    }

    fun leerArchivoSD(){
        if (noSD()){
            mensaje("NO HAY MEMORIA EXTERNA")
            return
        }
        if(nombreEx.text.isEmpty()){
            mensaje("DEBES PONERLE UN NOMBRE AL ARCHIVO")
            return
        }
        try {
            var archivo = nombreEx.text.toString()+".txt"
            var rutaSD = Environment.getExternalStorageDirectory()
            var datosArchivo = File(rutaSD.absolutePath,archivo)
            var flujoEntrada = BufferedReader(InputStreamReader(FileInputStream(datosArchivo)))
            data = flujoEntrada.readLine()

            valoresMostrar.setText(data.toString())

            val vectorAux  = data.split(",")
            (0..9).forEach {ren->


                vector[ren] = vectorAux.get(ren).toInt()

            }









        }catch (error: IOException){
            mensaje(error.message.toString())
        }

    }


    private fun limpiarCampos() {
        valor.setText("")
        posicion.setText("")

    }

    private fun mensaje(s: String) {

        AlertDialog.Builder(this)
            .setTitle("ATENCION")
            .setMessage(s)
            .setPositiveButton("ok"){d,i ->}
            .show()
    }
    fun noSD():Boolean{
        var estado = Environment.getExternalStorageState()

        if (estado != Environment.MEDIA_MOUNTED){
            return true

        }
        return false
    }
}
