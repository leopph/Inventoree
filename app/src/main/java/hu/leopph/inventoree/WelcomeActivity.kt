package hu.leopph.inventoree


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class WelcomeActivity : AppCompatActivity() {
    private val mAuth: FirebaseAuth by lazy { Firebase.auth }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        if (mAuth.currentUser != null) {
            startActivity(
                Intent(this, ProductListActivity::class.java)
                    .putExtra("UID", mAuth.currentUser?.uid))
            finish()
        }
    }
}