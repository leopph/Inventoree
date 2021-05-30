package hu.leopph.inventoree


import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
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

        mBinding.prodStartdateEntry.editText?.text?.clear()
        mBinding.prodStartdateEntry.editText?.text?.insert(
            0,
            DateFormat.getDateFormat(applicationContext)
                .format(calendar.time)
        )
    }

    private val orderDateCallback = DatePickerDialog.OnDateSetListener { _, year, month, day ->
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)

        mBinding.prodOrderdateEntry.editText?.text?.clear()
        mBinding.prodOrderdateEntry.editText?.text?.insert(
            0,
            DateFormat.getDateFormat(applicationContext)
                .format(calendar.time)
        )
    }

    private val termDateCallback = DatePickerDialog.OnDateSetListener { _, year, month, day ->
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)

        mBinding.prodTermdateEntry.editText?.text?.clear()
        mBinding.prodTermdateEntry.editText?.text?.insert(
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

        mBinding.prodStartdateEntry.editText?.setOnClickListener {
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

        mBinding.prodOrderdateEntry.editText?.setOnClickListener {
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

        mBinding.prodTermdateEntry.editText?.setOnClickListener {
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

        mBinding.prodNameEntry.editText?.text?.insert(0, product.name)
        mBinding.prodDescEntry.editText?.text?.insert(0, product.description)
        mBinding.prodSerialEntry.editText?.text?.insert(0, product.productSerialNumber)
        mBinding.prodBundleToggle.isChecked = product.isBundle
        mBinding.prodOrderdateEntry.editText?.text?.insert(0, DateFormat.getDateFormat(applicationContext).format(product.orderDate.toDate()))
        mBinding.prodStartdateEntry.editText?.text?.insert(0, DateFormat.getDateFormat(applicationContext).format(product.startDate.toDate()))
        mBinding.prodTermdateEntry.editText?.text?.insert(0, DateFormat.getDateFormat(applicationContext).format(product.terminationDate.toDate()))
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, ProductStatusType.values())
        (mBinding.prodStatusDropdown.editText as? AutoCompleteTextView)?.setAdapter(adapter) // TODO set default
        mBinding.prodDfreeAmountEntry.editText?.text?.insert(0, product.price.dutyFreeAmount.value.toString())
        mBinding.prodDfreeCurrEntry.editText?.text?.insert(0, product.price.dutyFreeAmount.unit.currencyCode)
        mBinding.prodTaxIncAmountEntry.editText?.text?.insert(0, product.price.taxIncludedAmount.value.toString())
        mBinding.prodTaxincCurrEntry.editText?.text?.insert(0, product.price.taxIncludedAmount.unit.currencyCode)
        mBinding.prodTaxrateEntry.editText?.text?.insert(0, product.price.taxRate.toString())
    }


    fun onSavePressed(@Suppress("UNUSED_PARAMETER") view: View) {
        // CHECK FOR EMPTY PRODUCT NAME
        val productName = mBinding.prodNameEntry.editText?.text.toString()
        if (productName.isBlank()) {
            Toast.makeText(this, R.string.bad_prod_name_error, Toast.LENGTH_LONG).show()
            return
        }

        // CHECK FOR EMPTY SERIAL NUMBER
        val serialNumber = mBinding.prodSerialEntry.editText?.text.toString()
        if (serialNumber.isBlank()) {
            Toast.makeText(this, R.string.bad_serial_error, Toast.LENGTH_LONG).show()
            return
        }

        // CHECK FOR VALID NUMBER
        val dutyFreeAmount: Float
        try {
            dutyFreeAmount = mBinding.prodDfreeAmountEntry.editText?.text.toString().toFloat()
        }
        catch (exception: NumberFormatException) {
            // ERROR
            return
        }

        // CHECK FOR VALID CURRENCY
        val dutyFreeCurrency: Currency
        try {
            dutyFreeCurrency = Currency.getInstance(mBinding.prodDfreeCurrEntry.editText?.text.toString())
        }
        catch (exception: IllegalArgumentException) {
            // ERROR
            return
        }

        // CHECK FOR VALID NUMBER
        val taxIncludedAmount: Float
        try {
            taxIncludedAmount = mBinding.prodTaxIncAmountEntry.editText?.text.toString().toFloat()
        }
        catch (exception: NumberFormatException) {
            // ERROR
            return
        }

        // CHECK FOR VALID CURRENCY
        val taxIncludedCurrency: Currency
        try {
            taxIncludedCurrency = Currency.getInstance(mBinding.prodTaxincCurrEntry.editText?.text.toString())
        }
        catch (exception: IllegalArgumentException) {
            // ERROR
            return
        }

        // CHECK FOR VALID NUMBER
        val taxRate: Float
        try {
            taxRate = mBinding.prodTaxrateEntry.editText?.text.toString().toFloat()
        }
        catch (exception: NumberFormatException) {
            // ERROR
            return
        }

        // SET PRODUCT VALUES
        val product = intent.getParcelableExtra<Product>("product")!!
        product.name = productName
        product.description = mBinding.prodDescEntry.editText?.text.toString()
        product.isBundle = mBinding.prodBundleToggle.isChecked
        product.productSerialNumber = serialNumber
        product.price.dutyFreeAmount.unit = dutyFreeCurrency
        product.price.dutyFreeAmount.value = dutyFreeAmount
        product.price.taxIncludedAmount.unit = taxIncludedCurrency
        product.price.taxIncludedAmount.value = taxIncludedAmount
        product.status = ProductStatusType.valueOf((mBinding.prodStatusDropdown.editText as? AutoCompleteTextView)?.text.toString())
        product.price.taxRate = taxRate

        product.orderDate = Timestamp(
            java.text.DateFormat.getDateInstance(
                java.text.DateFormat.SHORT, Locale.getDefault())
                .parse(mBinding.prodOrderdateEntry.editText?.text.toString())!!)

        product.startDate = Timestamp(
            java.text.DateFormat.getDateInstance(
                java.text.DateFormat.SHORT, Locale.getDefault())
                .parse(mBinding.prodStartdateEntry.editText?.text.toString())!!)

        product.terminationDate = Timestamp(
            java.text.DateFormat.getDateInstance(
                java.text.DateFormat.SHORT, Locale.getDefault())
                .parse(mBinding.prodTermdateEntry.editText?.text.toString())!!)


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