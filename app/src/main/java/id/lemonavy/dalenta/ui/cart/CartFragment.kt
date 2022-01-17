package id.lemonavy.dalenta.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.lemonavy.dalenta.R
import id.lemonavy.dalenta.base.BaseFragment
import id.lemonavy.dalenta.db.entity.CartData
import id.lemonavy.dalenta.util.ConstantVal
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.fragment_cart.recyclerView

class CartFragment : BaseFragment(), ItemClickListener {
    private lateinit var viewModel: CartViewModel
    private var adapter: CartAdapter?= null
    private var doubleClick = 0
    private var subTotalItem = 0
    private var taxService = 0
    private var taxGst = 0
    private var orderListPref : String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setDiningOption(getString(R.string.option_have_here))
        viewModel = ViewModelProvider(this)[CartViewModel::class.java]

        viewModel.getCart()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initStates()
    }

    private fun initViews() {
        diningOption.setOnClickListener{
            if (doubleClick%2==0) {
                diningOption.text = getString(R.string.option_delivery)
                setDiningOption(getString(R.string.option_delivery))
            }else{
                diningOption.text = getString(R.string.option_have_here)
                setDiningOption(getString(R.string.option_have_here))
            }
            doubleClick++
        }

        btnCheckout.setOnClickListener {
            navigateTo(R.id.action_cartFragment_to_checkoutFragment)
        }
    }

    private fun initStates() {
        viewModel.cartState().observe(viewLifecycleOwner, { cart ->
            initProductListPref(cart)
            val layoutManager = LinearLayoutManager(requireContext())
            adapter = CartAdapter(cart, this)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = layoutManager
        })
    }

    private fun initProductListPref(cart: MutableList<CartData>) {
        cart.forEachIndexed { _, detailData ->
            val finalPrice =  (detailData.itemPrice +  detailData.itemPriceTopping) * detailData.itemQty.toInt()
            orderListPref +="* ${detailData.itemName} x${detailData.itemQty} \n= ${moneyFormat().format(finalPrice)}\n\n"
        }
        updateStringSharedPref(ConstantVal.PRODUCT_ORDER, "$orderListPref")
    }

    override fun onClick(data: CartData) {
        val uniqueItem = data.id?.let {
            viewModel.getUniqueItem(it)
        }

        showProductDialog(requireContext(), uniqueItem!!.itemName, uniqueItem){
                sizeType, additionalPrice, quantity, notes, switchService, switchGst ->

            val priceByQty = (uniqueItem.itemPrice +additionalPrice) * quantity.toInt()
            subTotalItem += priceByQty

            taxService = if(switchService){
                5
            }else{ 0 }

            taxGst = if(switchGst){
                10
            }else{ 0 }

            val taxServTotal = subTotalItem * taxService / 100
            val taxGstTotal = subTotalItem * taxGst / 100

            val taxesTotal = taxServTotal + taxGstTotal
            val grandTotal = subTotalItem + taxesTotal

            //update item in cart
            viewModel.updateCartId(CartData(
                uniqueItem.id,
                uniqueItem.itemId,
                data.itemName,
                sizeType,
                quantity,
                uniqueItem.itemPrice,
                additionalPrice,
                notes,
                switchService,
                switchGst
            ))

            val itemPriceFormat = setMoneyFormat(grandTotal)
            updateStringSharedPref(ConstantVal.CHARGE_TOTAL, grandTotal.toString())
            setBtnCheckoutText(btnCheckout, itemPriceFormat)
        }
    }

    private fun setDiningOption(value: String) {
        updateStringSharedPref(ConstantVal.DINING_OPTION, value)
    }

    private fun setMoneyFormat(finalPrice: Int): String {
        return moneyFormat().format(finalPrice.toLong())
    }

    override fun onInitTotal(subTotal: Int, taxes: Int) {
        val grandTotal = subTotal + taxes
        val itemPriceFormat = setMoneyFormat(subTotal)
        val taxesPriceFormat = setMoneyFormat(taxes)
        val grandTotalFormat = setMoneyFormat(grandTotal)
        setRpTotalText(itemSubPrice, itemPriceFormat)
        setRpTotalText(itemTaxes, taxesPriceFormat)
        updateStringSharedPref(ConstantVal.PRODUCT_TAXES, taxes.toString())
        updateStringSharedPref(ConstantVal.CHARGE_TOTAL, grandTotal.toString())
        setBtnCheckoutText(btnCheckout, grandTotalFormat)
    }
}