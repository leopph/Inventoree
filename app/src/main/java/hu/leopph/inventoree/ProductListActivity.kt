package hu.leopph.inventoree


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import hu.leopph.inventoree.database.model.Money
import hu.leopph.inventoree.database.model.Price
import hu.leopph.inventoree.database.model.Product
import hu.leopph.inventoree.database.model.ProductStatusType
import hu.leopph.inventoree.databinding.ActivityProductListBinding
import java.util.*


class ProductListActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityProductListBinding
    private val mAuth: FirebaseAuth by lazy { Firebase.auth }
    private val mFirestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private val mCollection: CollectionReference by lazy { mFirestore.collection("Products") } // TODO save this literal elsewhere
    private val mProductList: MutableList<Product> = mutableListOf()

    private val newProductCallback = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityProductListBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        if (intent.getStringExtra("UID") != mAuth.currentUser?.uid)
            finish()

        mBinding.unameLabel.text = "Hello, ${mAuth.currentUser?.email}" // DEBUG

        mBinding.signoutButton.setOnClickListener {
            mAuth.signOut()
            startActivity(Intent(this, WelcomeActivity::class.java))
            finish()
        }

        mBinding.fab.setOnClickListener {
            newProductCallback.launch(
                Intent(this, AddEditProductActivity::class.java)
                    .putExtra("product", Product())
                    .putExtra("UID", mAuth.currentUser?.uid))
        }

        query()
    }


    private fun query() {
        mProductList.clear()

        mCollection .get().addOnSuccessListener{
            for (item in it) {
            }
        }
    }
}