package hu.leopph.inventoree


import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import hu.leopph.inventoree.database.model.Product
import hu.leopph.inventoree.databinding.ActivityAddEditProductBinding


class AddEditProductActivity : AppCompatActivity() {
    private val mAuth: FirebaseAuth by lazy { Firebase.auth }
    private lateinit var mBinding: ActivityAddEditProductBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityAddEditProductBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        if (intent.getStringExtra("UID") != mAuth.currentUser?.uid)
            finish()

        println(intent.getParcelableExtra("product"))
    }


    fun onSavePressed(@Suppress("UNUSED_PARAMTER") view: View) {
        val productName = mBinding.prodNameEdittext.text.toString()

        if (productName.isBlank()) {
            Toast.makeText(this, R.string.bad_prod_name_error, Toast.LENGTH_LONG).show()
            return
        }

        val product = intent.getParcelableExtra<Product>("product")!!
        product.name = productName
        product.description = mBinding.prodDescEdittext.text.toString()
        product.isBundle = mBinding.prodBundleToggle.isActivated

        setResult(RESULT_OK,
            Intent(this, ProductListActivity::class.java)
                .putExtra("product", product))
        finish()
    }
}