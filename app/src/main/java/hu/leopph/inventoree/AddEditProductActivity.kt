package hu.leopph.inventoree


import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import hu.leopph.inventoree.database.model.Product
import hu.leopph.inventoree.database.model.ProductStatusType
import hu.leopph.inventoree.databinding.ActivityAddEditProductBinding
import java.lang.IllegalArgumentException
import java.lang.NumberFormatException
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
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, ProductStatusType.values())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mBinding.prodStatusSpinner.adapter = adapter
        mBinding.prodStatusSpinner.setSelection(product.status.ordinal)
        mBinding.dutyFreeAmountEdittext.text.insert(0, product.price.dutyFreeAmount.value.toString())
        mBinding.dutyFreeCurrencyEdittext.text.insert(0, product.price.dutyFreeAmount.unit.currencyCode)
        mBinding.taxIncludedAmountEdittext.text.insert(0, product.price.taxIncludedAmount.value.toString())
        mBinding.taxIncludedCurrencyEdittext.text.insert(0, product.price.taxIncludedAmount.unit.currencyCode)
        mBinding.taxrateEdittext.text.insert(0, product.price.taxRate.toString())
    }


    fun onSavePressed(@Suppress("UNUSED_PARAMETER") view: View) {
        // CHECK FOR EMPTY PRODUCT NAME
        val productName = mBinding.prodNameEdittext.text.toString()
        if (productName.isBlank()) {
            Toast.makeText(this, R.string.bad_prod_name_error, Toast.LENGTH_LONG).show()
            return
        }

        // CHECK FOR EMPTY SERIAL NUMBER
        val serialNumber = mBinding.prodSerialEdittext.text.toString()
        if (serialNumber.isBlank()) {
            Toast.makeText(this, R.string.bad_serial_error, Toast.LENGTH_LONG).show()
            return
        }

        // CHECK FOR VALID NUMBER
        val dutyFreeAmount: Float
        try {
            dutyFreeAmount = mBinding.dutyFreeAmountEdittext.text.toString().toFloat()
        }
        catch (exception: NumberFormatException) {
            // ERROR
            return
        }

        // CHECK FOR VALID CURRENCY
        val dutyFreeCurrency: Currency
        try {
            dutyFreeCurrency = Currency.getInstance(mBinding.dutyFreeCurrencyEdittext.text.toString())
        }
        catch (exception: IllegalArgumentException) {
            // ERROR
            return
        }

        // CHECK FOR VALID NUMBER
        val taxIncludedAmount: Float
        try {
            taxIncludedAmount = mBinding.taxIncludedAmountEdittext.text.toString().toFloat()
        }
        catch (exception: NumberFormatException) {
            // ERROR
            return
        }

        // CHECK FOR VALID CURRENCY
        val taxIncludedCurrency: Currency
        try {
            taxIncludedCurrency = Currency.getInstance(mBinding.taxIncludedCurrencyEdittext.text.toString())
        }
        catch (exception: IllegalArgumentException) {
            // ERROR
            return
        }

        // CHECK FOR VALID NUMBER
        val taxRate: Float
        try {
            taxRate = mBinding.taxrateEdittext.text.toString().toFloat()
        }
        catch (exception: NumberFormatException) {
            // ERROR
            return
        }

        // SET PRODUCT VALUES
        val product = intent.getParcelableExtra<Product>("product")!!
        product.name = productName
        product.description = mBinding.prodDescEdittext.text.toString()
        product.isBundle = mBinding.prodBundleToggle.isChecked
        product.productSerialNumber = serialNumber
        product.price.dutyFreeAmount.unit = dutyFreeCurrency
        product.price.dutyFreeAmount.value = dutyFreeAmount
        product.price.taxIncludedAmount.unit = taxIncludedCurrency
        product.price.taxIncludedAmount.value = taxIncludedAmount
        product.status = ProductStatusType.valueOf(mBinding.prodStatusSpinner.selectedItem.toString())
        product.price.taxRate = taxRate

        product.orderDate = Timestamp(
            java.text.DateFormat.getDateInstance(
                java.text.DateFormat.SHORT, Locale.getDefault())
                .parse(mBinding.orderdateEdittext.text.toString())!!)

        product.startDate = Timestamp(
            java.text.DateFormat.getDateInstance(
                java.text.DateFormat.SHORT, Locale.getDefault())
                .parse(mBinding.startdateEdittext.text.toString())!!)

        product.terminationDate = Timestamp(
            java.text.DateFormat.getDateInstance(
                java.text.DateFormat.SHORT, Locale.getDefault())
                .parse(mBinding.termdateEdittext.text.toString())!!)


        // GO BACK TO LISTING
        setResult(RESULT_OK,
            Intent(this, ProductListActivity::class.java)
                .putExtra("product", product))
        finish()
    }


    private fun onCancel() {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }


    override fun onBackPressed() {
        onCancel()
    }
}