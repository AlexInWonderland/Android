package com.wistron.myapplication

import android.Manifest
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import android.util.Log
import com.facebook.stetho.Stetho
import android.os.Environment
import java.io.*
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Environment.getExternalStorageDirectory


class MainActivity : AppCompatActivity() {

    val gsonString:String = "[{\"id\":1, \"members\" : [\" 501031 \", \"501032\"] }, {\"id\":2, \"members\" : [\" 501033 \", \"501034\"]}]"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Stetho.initializeWithDefaults(this);
        setContentView(R.layout.activity_main)
        val tv:TextView = findViewById(R.id.tv_gson)

        //val listType = object : TypeToken<ArrayList<Group>>(){}.type
        //val jsonArr = Gson().fromJson<ArrayList<Group>>(gsonString, listType)
        val jsonGroup = parsingJson()
        val sb = StringBuffer()
        for(obj in jsonGroup){
           sb.append(obj.id)
            for(s in obj._members!!)
            sb.append(s)
            sb.append("\n")
        }
        //tv.text = sb.toString()
        //tv.text = gsonString
        //writeToFile(sb.toString(), applicationContext)
        tv.text = Read_SD()
        Write_SD()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permission: Array<String>,
        grantResults: IntArray
    ) {
        //requestCode就是requestPermissions()的第三个参数
        //permission就是requestPermissions()的第二个参数
        //grantResults是结果，0调试通过，-1表示拒绝
    }

    //讀取SD卡資料
    fun Write_SD(){
        var sdCardDir = getExternalStorageDirectory().absolutePath
        sdCardDir += "/wistron"
        val saveFile = File(sdCardDir, "WWSetting.txt")
        try {
            //建立FileReader物件，並設定讀取的檔案為CheckFlie.txt
            val fr = FileWriter(saveFile)
            //將BufferedReader與FileReader做連結
            val bufFile = BufferedWriter(fr)

            bufFile.write("AAAAABBBBBBB") //readLine()讀取一整行
            bufFile.close()

            //					while (temp!=null){
            //						readData+=temp +  "\n";
            //						temp=bufFile.readLine();
            //					}
            //textView.setText(readData);
            //Read_Setting(readData) //字串處理，之後丟到全域去
            //System.out.println("Setting.txt = $readData")
            //return readData

        } catch (e: Exception) {
            e.printStackTrace()
        }
        //return "Alex test "
    }

    //讀取SD卡資料
    fun Read_SD():String {
        var sdCardDir = getExternalStorageDirectory().absolutePath
        sdCardDir += "/wistron"
        val saveFile = File(sdCardDir, "Setting.txt")
        var readData = ""
        try {
            //建立FileReader物件，並設定讀取的檔案為CheckFlie.txt
            val fr = FileReader(saveFile)
            //將BufferedReader與FileReader做連結
            val bufFile = BufferedReader(fr)

            val temp = bufFile.readLine() //readLine()讀取一整行
            readData += temp //讀取一行
            //					while (temp!=null){
            //						readData+=temp +  "\n";
            //						temp=bufFile.readLine();
            //					}
            //textView.setText(readData);
            //Read_Setting(readData) //字串處理，之後丟到全域去
            System.out.println("Setting.txt = $readData")
            bufFile.close()
            return readData

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return "Alex test "
    }
    private fun writeToFile(data: String, context: Context) {
        try {
            val outputStreamWriter =
                OutputStreamWriter(context.openFileOutput("config.txt", Context.MODE_PRIVATE))
            outputStreamWriter.write(data)
            outputStreamWriter.close()
        } catch (e: IOException) {
            Log.e("Exception", "File write failed: " + e.toString())
        }

    }

    fun parsingJson():ArrayList<Group>{
        val MapType = object : TypeToken<ArrayList<Group>>() {}.type
        val jsonArr = Gson().fromJson<ArrayList<Group>>(gsonString, MapType)
        return jsonArr
    }
}
