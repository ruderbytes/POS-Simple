package id.lemonavy.dalenta.ui.checkout

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import id.lemonavy.dalenta.R
import id.lemonavy.dalenta.base.BaseFragment
import id.lemonavy.dalenta.util.ConstantVal
import kotlinx.android.synthetic.main.fragment_receipt.*
import java.net.URLEncoder

class ReceiptFragment : BaseFragment() {
    private var chargeTotal : String? = ""
    private var mustToPay : String? = ""
    private var diningOption : String? = ""
    private var paymentType : String? = ""
    private var productOrder : String? = ""
    private var productTaxes : String? = ""
    private var changeName : String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        diningOption = getStringSharedPref(ConstantVal.DINING_OPTION)
        paymentType = getStringSharedPref(ConstantVal.PAYMENT_TYPE)
        productOrder = getStringSharedPref(ConstantVal.PRODUCT_ORDER)
        productTaxes = getStringSharedPref(ConstantVal.PRODUCT_TAXES)

        arguments?.let {
            mustToPay = it.getString(ConstantVal.MUST_TO_PAY)
            chargeTotal = it.getString(ConstantVal.CHARGE_TOTAL)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_receipt, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() {

        val moneyCharge: Int = mustToPay!!.toInt() - chargeTotal!!.toInt()
        val changePriceFormat = moneyFormat().format(moneyCharge.toLong())
        val receiptOutFormat = moneyFormat().format(mustToPay!!.toLong())

        receiptOut.text = getString(R.string.rp_total_change, receiptOutFormat)
        changeName = getString(R.string.no_change)
        if (moneyCharge != 0) {
            changeName = getString(R.string.out_of, changePriceFormat)
        }
        receiptChange.text = changeName

        sendReceiptEmail.setOnClickListener {
            sendReceiptToEmail()
        }

        sendReceiptWhatsapp.setOnClickListener {
            sendReceiptToWhatsApp()
        }
    }

    private fun sendReceiptToEmail()
    {
        val intent = Intent(Intent.ACTION_SEND)
        intent.data = Uri.parse("mailto:${getEmail()}")
        intent.putExtra(Intent.EXTRA_SUBJECT, "Dalenta Receipt")
        intent.putExtra(Intent.EXTRA_TEXT, getReceiptContent())

        intent.type = "text/plain"

        startActivity(Intent.createChooser(intent, "Choose an Email App"))
    }

    private fun sendReceiptToWhatsApp(){
        val validateMessage = URLEncoder.encode(getReceiptContent(), "UTF-8")
        val validateWhatsappNumber = validateWhatsAppNumber(getNumber()).trim()
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(
                "https://wa.me/$validateWhatsappNumber?text=$validateMessage"
            )
        ).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
    }

    private fun getProductOrderList(): String? {
        return productOrder
    }

    private fun getNumber() = etNumber.text.toString()
    private fun getEmail() = etEmail.text.toString()

    private fun getReceiptContent() = "Dalenta Order Detail\n\n"+
            "Date : ${dateNow()}\n"+
            "Dining Option : $diningOption\n\n"+
            "=================\n"+
            "Order List :\n${getProductOrderList()}"+
            "=================\n"+
            "Payment Type : $paymentType\n"+
            "Taxes : ${getString(R.string.rp_total, moneyFormat().format(productTaxes!!.toLong()))}\n"+
            "Grand Total : ${getString(R.string.rp_total, moneyFormat().format(chargeTotal!!.toLong()))}\n"+
            "Change : $changeName\n"
}