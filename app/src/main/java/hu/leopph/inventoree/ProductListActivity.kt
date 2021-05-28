package hu.leopph.inventoree


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import hu.leopph.inventoree.databinding.ActivityProductListBinding


class ProductListActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityProductListBinding
    private val mAuth: FirebaseAuth by lazy { Firebase.auth }
    private val mFirestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private val mCollection: CollectionReference by lazy { mFirestore.collection("Products") } // TODO save this literal elsewhere


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityProductListBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        if (intent.getStringExtra("UID") != mAuth.currentUser?.uid)
            finish()

        mBinding.unameLabel.text = "Hello, ${mAuth.currentUser?.email}" // DEBUG

        mBinding.signoutButton.setOnClickListener {
            mAuth.signOut()
            startActivity(
                Intent(
                    this,
                    WelcomeActivity::class.java)
            )
            finish()
        }
    }
}