package hu.leopph.inventoree


import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import hu.leopph.inventoree.database.model.Product
import hu.leopph.inventoree.databinding.ActivityAddEditProductBinding
import java.util.*


class AddEditProductActivity : AppCompatActivity() {

    private val mAuth: FirebaseAuth by lazy { Firebase.auth }
    private lateinit var mBinding: ActivityAddEditProductBinding

    private val startDateCallback = DatePickerDialog.OnDateSetListener { _, year, month, day ->
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)

        mBinding.startdateEdittext.text.clear()
        mBinding.startdateEdittext.text.insert(
            0,
            DateFormat.getDateFormat(applicationContext)
                .format(calendar.time)
        )
    }

    private val orderDateCallback = DatePickerDialog.OnDateSetListener { _, year, month, day ->
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)

        mBinding.orderdateEdittext.text.clear()
        mBinding.orderdateEdittext.text.insert(
            0,
            DateFormat.getDateFormat(applicationContext)
                .format(calendar.time)
        )
    }

    private val termDateCallback = DatePickerDialog.OnDateSetListener { _, year, month, day ->
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)

        mBinding.termdateEdittext.text.clear()
        mBinding.termdateEdittext.text.insert(
            0,
            DateFormat.getDateFormat(applicationContext)
                .format(calendar.time)
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityAddEditProductBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        if (intent.getStringExtra("UID") != mAuth.currentUser?.uid)
            finish()

        val product = intent.getParcelableExtra<Product>("product")!!

        mBinding.startdateEdittext.setOnClickListener {
            val calendar = Calendar.getInstance()
            calendar.time = product.startDate.toDate()

            DatePickerDialog(
                this,
                startDateCallback,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH))
                .show()
        }

        mBinding.orderdateEdittext.setOnClickListener {
            val calendar = Calendar.getInstance()
            calendar.time = product.orderDate.toDate()

            DatePickerDialog(
                this,
                orderDateCallback,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH))
                .show()
        }

        mBinding.termdateEdittext.setOnClickListener {
            val calendar = Calendar.getInstance()
            calendar.time = product.terminationDate.toDate()

            DatePickerDialog(
                this,
                termDateCallback,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH))
                .show()
        }

        mBinding.prodNameEdittext.text.insert(0, product.name)
        mBinding.prodDescEdittext.text.insert(0, product.description)
        mBinding.prodSerialEdittext.text.insert(0, product.productSerialNumber)
        mBinding.prodBundleToggle.isChecked = product.isBundle
        mBinding.orderdateEdittext.text.insert(0, DateFormat.getDateFormat(applicationContext).format(product.orderDate.toDate()))
        mBinding.startdateEdittext.text.insert(0, DateFormat.getDateFormat(applicationContext).format(product.startDate.toDate()))
        mBinding.termdateEdittext.text.insert(0, DateFormat.getDateFormat(applicationContext).format(product.terminationDate.toDate()))
    }


    fun onSavePressed(@Suppress("UNUSED_PARAMETER") view: View) {
        val productName = mBinding.prodNameEdittext.text.toString()

        if (productName.isBlank()) {
            Toast.makeText(this, R.string.bad_prod_name_error, Toast.LENGTH_LONG).show()
            return
        }

        val serialNumber = mBinding.prodSerialEdittext.text.toString()

        if (serialNumber.isBlank()) {
            // ERROR
            return
        }

        val orderDate = mBinding.orderdateEdittext.text.toString()

        if (orderDate.isBlank()) {
            // ERROR
            return
        }

        val startDate = mBinding.startdateEdittext.text.toString()

        if (startDate.isBlank()) {
            // ERROR
            return
        }

        val termDate = mBinding.termdateEdittext.text.toString()

        if (termDate.isBlank()) {
            // ERROR
            return
        }

        val product = intent.getParcelableExtra<Product>("product")!!
        product.name = productName
        product.description = mBinding.prodDescEdittext.text.toString()
        product.isBundle = mBinding.prodBundleToggle.isChecked
        product.productSerialNumber = serialNumber

        product.orderDate = Timestamp(
            java.text.DateFormat.getDateInstance(
                java.text.DateFormat.SHORT, Locale.getDefault())
                .parse(orderDate)!!)

        product.startDate = Timestamp(
            java.text.DateFormat.getDateInstance(
                java.text.DateFormat.SHORT, Locale.getDefault())
                .parse(startDate)!!)

        product.terminationDate = Timestamp(
            java.text.DateFormat.getDateInstance(
                java.text.DateFormat.SHORT, Locale.getDefault())
                .parse(termDate)!!)

        setResult(RESULT_OK,
            Intent(this, ProductListActivity::class.java)
                .putExtra("product", product))
        finish()
    }
}