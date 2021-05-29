package hu.leopph.inventoree


import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import hu.leopph.inventoree.database.model.Product
import hu.leopph.inventoree.databinding.ProductListingBinding


class ProductAdapter(
    productList: List<Product>
) : RecyclerView.Adapter<ProductAdapter.ViewHolder>(), Filterable {

    private var mProductList = productList
    private val mAllProductList = productList

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
                if (product.name.contains(filter))
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ProductListingBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mProductList[position])
    }

    override fun getItemCount(): Int {
        return mProductList.size
    }

    class ViewHolder(private val mBinding: ProductListingBinding) : RecyclerView.ViewHolder(mBinding.root) {
        fun bind(product: Product) {
            mBinding.name.text = product.name
            mBinding.status.text = product.status.toString()
            mBinding.orderdate.text = DateFormat.getDateFormat(itemView.context.applicationContext).format(product.orderDate.toDate())
        }
    }

    override fun getFilter(): Filter {
        return mFilter
    }
}