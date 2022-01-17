package id.lemonavy.dalenta.ui.product

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.lemonavy.dalenta.R
import id.lemonavy.dalenta.base.BaseFragment
import id.lemonavy.dalenta.db.entity.CartData
import id.lemonavy.dalenta.db.entity.ItemData
import id.lemonavy.dalenta.util.ConstantVal
import kotlinx.android.synthetic.main.fragment_product.*

class ProductFragment : BaseFragment(), ItemClickListener {
    private lateinit var viewModel: ProductViewModel
    private var adapter: ProductAdapter?= null
    private var chargeTotal: String?=null
    private var subTotalItem = 0
    private var taxService = 0
    private var taxGst = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[ProductViewModel::class.java]
        chargeTotal = getStringSharedPref(ConstantVal.CHARGE_TOTAL)

        viewModel.getItems()
        //Checking if item is not exist
        val itemCount = dbHelper().itemDao().countItem()
        if(itemCount==0) {
            initAllData()
        }

        if(chargeTotal.isNullOrEmpty()){
            chargeTotal = "0"
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initStates()
    }

    override fun onResume() {
        super.onResume()
    }

    private fun initViews() {
        initChargeGrandTotal()
        btnCharge.setOnClickListener {
            navigateTo(R.id.action_productFragment_to_cartFragment)
        }
    }

    private fun initChargeGrandTotal() {
        val itemPriceFormat = moneyFormat().format(chargeTotal!!.toLong())
        setBtnChargeText(btnCharge, itemPriceFormat)
    }

    private fun initStates() {
        viewModel.productState().observe(viewLifecycleOwner, { product ->
            val layoutManager = LinearLayoutManager(requireContext())
            adapter = ProductAdapter(product, this)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = layoutManager
        })
    }

    @SuppressLint("StringFormatMatches")
    override fun onClick(data: ItemData) {

        showProductDialog(requireContext(), data.itemName, null){
                sizeType, additionalPrice, quantity, notes, switchService, switchGst ->

            val priceByQty = (data.itemPrice +additionalPrice) * quantity.toInt()
            subTotalItem += priceByQty

            if(switchService){
                taxService = 5
            }
            if(switchGst){
                taxGst = 10
            }

            val taxServTotal = subTotalItem * taxService / 100
            val taxGstTotal = subTotalItem * taxGst / 100

            val taxesTotal = taxServTotal + taxGstTotal
            val grandTotal = subTotalItem + taxesTotal

            viewModel.addToCart(CartData(
                null,
                data.id.toString(),
                data.itemName,
                sizeType,
                quantity,
                data.itemPrice,
                additionalPrice,
                notes,
                switchService,
                switchGst
            ))

            val itemPriceFormat = moneyFormat().format(grandTotal.toLong())
            updateStringSharedPref(ConstantVal.CHARGE_TOTAL, grandTotal.toString())
            setBtnChargeText(btnCharge, itemPriceFormat)
        }
    }
}