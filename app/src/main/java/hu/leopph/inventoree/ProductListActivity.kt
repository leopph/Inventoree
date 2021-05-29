package hu.leopph.inventoree


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
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
            val product = result.data?.getParcelableExtra<Product>("product")!!
            product.id = if (mProductList.isEmpty()) "0" else java.lang.Long.toHexString(java.lang.Long.parseLong(mProductList[0].id) + 1)
            mCollection.add(product).addOnSuccessListener {
                mProductList.add(product)
                mBinding.recyclerView.adapter?.notifyDataSetChanged()
            }
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

        mBinding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding.recyclerView.adapter = ProductAdapter(mProductList)
        query()
    }


    private fun query() {
        mProductList.clear()

        mCollection.orderBy("id", Query.Direction.DESCENDING).get().addOnSuccessListener{
            for (item in it) {
                val dutyFree = Money(Currency.getInstance(item["price.dutyFreeAmount.unit.currencyCode"] as String), (item["price.dutyFreeAmount.value"] as Double).toFloat())
                val taxIncluded = Money(Currency.getInstance(item["price.taxIncludedAmount.unit.currencyCode"] as String), (item["price.taxIncludedAmount.value"] as Double).toFloat())
                val taxRate = (item["price.taxRate"] as Double).toFloat()
                val price = Price(taxRate = taxRate, dutyFreeAmount = dutyFree, taxIncludedAmount = taxIncluded)

                mProductList.add(Product(
                    id = item["id"] as String,
                    description = item["description"] as String,
                    isBundle = item["bundle"] as Boolean,
                    name = item["name"] as String,
                    orderDate = item["orderDate"] as Timestamp,
                    productSerialNumber = item["productSerialNumber"] as String,
                    startDate = item["startDate"] as Timestamp,
                    terminationDate = item["terminationDate"] as Timestamp,
                    status = ProductStatusType.valueOf(item["status"] as String),
                    price = price
                ))
            }

            mBinding.recyclerView.adapter?.notifyDataSetChanged()
        }
    }
}