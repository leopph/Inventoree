package hu.leopph.inventoree


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class ProductListActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)

        mAuth = Firebase.auth

        if (intent.getStringExtra("UID") != mAuth.currentUser?.uid)
            finish()
    }


    override fun onBackPressed() {
        super.onBackPressed()
        mAuth.signOut()
    }


    override fun onStop() {
        super.onStop()
        mAuth.signOut()
    }
}