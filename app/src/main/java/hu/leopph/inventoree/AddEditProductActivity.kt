package hu.leopph.inventoree


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class AddEditProductActivity : AppCompatActivity() {
    private val mAuth: FirebaseAuth by lazy { Firebase.auth }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_product)

        if (intent.getStringExtra("UID") != mAuth.currentUser?.uid)
            finish()

        println(intent.getParcelableExtra("product"))
    }
}