package hu.leopph.inventoree


import android.content.Intent
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import hu.leopph.inventoree.database.model.Product
import hu.leopph.inventoree.databinding.ProductListingBinding


class ProductAdapter(
    productList: List<Product>,
    private val mEditProductCallback: ActivityResultLauncher<Intent>
) : RecyclerView.Adapter<ProductAdapter.ViewHolder>(), Filterable {

    private val mAuth: FirebaseAuth by lazy { Firebase.auth }
    private var mProductList = productList
    private val mAllProductList = productList
    private var mDeleteCallback: ((Product) -> Unit)? = null

    private val mFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val ret = FilterResults()

            if (constraint == null || constraint.isEmpty()) {
                ret.count = mAllProductList.size
                ret.values = mAllProductList
                return ret
            }

            val filter = constraint.toString().lowercase()
            val filtered = mutableListOf<Product>()

            for (product in mAllProductList)
                if (product.name.lowercase().contains(filter))
                    filtered.add(product)

            ret.count = filtered.size
            ret.values = filtered
            return ret
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            @Suppress("UNCHECKED_CAST") // type erasure xd
            mProductList = results?.values as List<Product>
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ProductListingBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mProductList[position])
        holder.itemView.setOnClickListener {
            mEditProductCallback.launch(
                Intent(holder.itemView.context, AddEditProductActivity::class.java)
                    .putExtra("product", mProductList[position])
                    .putExtra("UID", mAuth.currentUser?.uid))
        }
    }

    override fun getItemCount(): Int = mProductList.size

    fun setOnDeleteListener(func: ((Product) -> Unit)?) {
        mDeleteCallback = func
    }

    inner class ViewHolder(private val mBinding: ProductListingBinding) : RecyclerView.ViewHolder(mBinding.root) {
        fun bind(product: Product) {
            mBinding.name.text = product.name
            mBinding.status.text = product.status.toString()
            mBinding.orderdate.text = DateFormat.getDateFormat(itemView.context.applicationContext).format(product.orderDate.toDate())
            mBinding.button2.setOnClickListener {
                mDeleteCallback?.invoke(product)
            }
        }
    }

    override fun getFilter(): Filter = mFilter
}