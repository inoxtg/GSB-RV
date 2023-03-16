package fr.gsb.rv.visiteur

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    fun annuler(vue: View){
        var teMatr: EditText = findViewById(R.id.teMatr)
        var teMdp: EditText = findViewById(R.id.teMdp)
        teMatr.setText("")
        teMdp.setText("")
    }

    fun seConnecter(vue: View){
        var teMatr: EditText = findViewById(R.id.teMatr)
        var teMdp: EditText = findViewById(R.id.teMdp)

        var matr: String = teMatr.getText().toString()
        var mdp: String = teMdp.getText().toString()

        var url: String = "http://192.168.1.29:5000/visiteur/$matr/$mdp"

        var test = 0;
    }
}