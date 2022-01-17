package id.lemonavy.dalenta.ui.checkout

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.LinearLayoutCompat
import id.lemonavy.dalenta.R
import id.lemonavy.dalenta.base.BaseFragment
import id.lemonavy.dalenta.util.ConstantVal
import kotlinx.android.synthetic.main.fragment_checkout.*
import kotlin.math.ceil

class CheckoutFragment : BaseFragment() {
    private var chargeTotal : String? = ""
    private lateinit var bundle: Bundle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        chargeTotal = getStringSharedPref(ConstantVal.CHARGE_TOTAL)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_checkout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    @SuppressLint("StringFormatMatches")
    private fun initViews() {
        bundle = Bundle()

        val inChargeDouble: Double = chargeTotal!!.toDouble()
        val chargeRoundedIn5 = (ceil(inChargeDouble / 5000) * 5000).toInt()
        val chargeRoundedIn10 = (ceil(inChargeDouble / 10000) * 10000).toInt()
        if (chargeRoundedIn5 == chargeRoundedIn10) {
            radioCash3.visibility = View.GONE
        }

        val noChargeFormat = moneyFormat().format(chargeTotal!!.toLong())
        tvChargeTotal.text = getString(R.string.rp_total, noChargeFormat)
        radioCash1.text = getString(R.string.rp_total, noChargeFormat)

        val chargeRoundedIn5Format = moneyFormat().format(chargeRoundedIn5.toLong())
        radioCash2.text = getString(R.string.rp_total, chargeRoundedIn5Format)

        val chargeRoundedIn10Format = moneyFormat().format(chargeRoundedIn10.toLong())
        radioCash3.text = getString(R.string.rp_total, chargeRoundedIn10Format)

        radioCash1.setOnClickListener {
            bundle.apply {
                putString(ConstantVal.CHARGE_TOTAL, chargeTotal)
                putString(ConstantVal.MUST_TO_PAY, chargeTotal)
            }
            setPaymentTypePref(getString(R.string.payment_type_cash))
            navigateTo(R.id.action_checkoutFragment_to_receiptFragment, bundle)
        }

        radioCash2.setOnClickListener {
            bundle.apply {
                putString(ConstantVal.CHARGE_TOTAL, chargeTotal)
                putString(ConstantVal.MUST_TO_PAY, chargeRoundedIn5.toString())
            }
            setPaymentTypePref(getString(R.string.payment_type_cash))
            navigateTo(R.id.action_checkoutFragment_to_receiptFragment, bundle)
        }

        radioCash3.setOnClickListener {
            bundle.apply {
                putString(ConstantVal.CHARGE_TOTAL, chargeTotal)
                putString(ConstantVal.MUST_TO_PAY, chargeRoundedIn10.toString())
            }
            setPaymentTypePref(getString(R.string.payment_type_cash))
            navigateTo(R.id.action_checkoutFragment_to_receiptFragment, bundle)
        }

        radioCustom.setOnClickListener {
            showCustomPrice(requireContext()) {
                    custom ->

                bundle.apply {
                    putString(ConstantVal.CHARGE_TOTAL, chargeTotal)
                    putString(ConstantVal.MUST_TO_PAY, custom)
                }
                setPaymentTypePref(getString(R.string.payment_type_cash))
                navigateTo(R.id.action_checkoutFragment_to_receiptFragment, bundle)
            }
        }

        paymentFixed(layoutTransfer, R.string.payment_type_transfer)
        paymentFixed(layoutDebit, R.string.payment_type_debit)
        paymentFixed(layoutQris, R.string.payment_type_qris)
    }

    private fun setPaymentTypePref(type: String){
        updateStringSharedPref(ConstantVal.PAYMENT_TYPE, type)
    }

    private fun paymentFixed(layout: LinearLayoutCompat, type: Int){
        layout.setOnClickListener {
            bundle.apply {
                putString(ConstantVal.CHARGE_TOTAL, chargeTotal)
                putString(ConstantVal.MUST_TO_PAY, chargeTotal)
            }
            setPaymentTypePref(getString(type))
            navigateTo(R.id.action_checkoutFragment_to_receiptFragment, bundle)
        }
    }
}