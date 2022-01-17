package id.lemonavy.dalenta.base

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import id.lemonavy.dalenta.R
import id.lemonavy.dalenta.db.entity.CartData
import id.lemonavy.dalenta.db.entity.ItemData
import id.lemonavy.dalenta.util.DatabaseHelper
import org.koin.android.ext.android.inject
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

open class BaseFragment : Fragment() {
    private val sharedPreferences: SharedPreferences by inject()
    private val sharedPreferencesEditor: SharedPreferences.Editor by inject()
    private var mDb: DatabaseHelper?=null
    private var moneyFormat = DecimalFormat("#,###,###")

    fun validateWhatsAppNumber(stringNumber: String): String {
        return when {
            stringNumber.startsWith("+0") -> stringNumber.replaceFirst("+0", "62")
            stringNumber.startsWith("+8") -> stringNumber.replaceFirst("+8", "628")
            stringNumber.startsWith("+") -> stringNumber.replaceFirst("+", "")
            stringNumber.startsWith("0") -> stringNumber.replaceFirst("0", "62")
            stringNumber.startsWith("8") -> stringNumber.replaceFirst("8", "628")
            else -> stringNumber
        }
    }

    fun navigateTo(target: Int, bundle: Bundle?=null){
        findNavController().navigate(
            target,
            bundle)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDb = DatabaseHelper.getInstance(requireContext())
    }

    fun dbHelper() : DatabaseHelper {
        return mDb!!
    }

    fun moneyFormat() : DecimalFormat {
        return moneyFormat
    }

    fun dateNow(): String {
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
    }

    fun saveData(title: String, price: Int) {

        mDb?.itemDao()?.insertItem(
            ItemData(
                null,
                "",
                "$title",
                price,
                20000
            )
        )
    }

    fun initAllData() {
        saveData(
            "Unagi Rice Bowl",
            45000
        )
        saveData(
            "Oyakodon Rice Bowl",
            55000
        )
        saveData(
            "Black Papper Beef Bowl",
            70000
        )
        saveData(
            "Ichiraku Ramen",
            35000
        )
        saveData(
            "Donburi Rice Bowl",
            60000
        )
    }

    fun getStringSharedPref(key: String): String {
        return sharedPreferences.getString(key, "").toString()
    }

    fun updateStringSharedPref(key: String, value: String?) {
        sharedPreferencesEditor.putString(key, value)
        sharedPreferencesEditor.apply()
    }

    fun clearSharedPref(){
        sharedPreferencesEditor.clear()
        sharedPreferencesEditor.apply()
    }

    fun setBtnCheckoutText(button:MaterialButton, itemPriceFormat:String?){
        button.text = getString(R.string.checkout_total, itemPriceFormat)
    }

    fun setBtnChargeText(button:MaterialButton, itemPriceFormat:String?){
        button.text = getString(R.string.charge_total, itemPriceFormat)
    }

    fun setRpTotalText(textView:TextView, itemPriceFormat:String?){
        textView.text = getString(R.string.rp_total, itemPriceFormat)
    }

    var additionalPrice = 0
    var sizeType = ""
    fun showProductDialog(
        context: Context,
        productName: String,
        uniqueItem: CartData?=null,
        function: (sizeType: String,
                   sizeTypeValue: Int,
                   quantity: String,
                   notes: String,
                   taxService: Boolean,
                   taxGst: Boolean) -> (Unit)
    ) {
        val dialog = AlertDialog.Builder(context)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_detail_product, null)
        dialog.setView(dialogView)
        dialog.setCancelable(true)
        val alertDialog = dialog.create()
        val tvProductName = dialogView.findViewById(R.id.titleProduct) as AppCompatTextView
        val btnAddProduct = dialogView.findViewById(R.id.btnAddProduct) as MaterialButton
        val btnClose = dialogView.findViewById(R.id.btnClose) as AppCompatImageView
        val layoutHalf = dialogView.findViewById(R.id.layoutHalf) as LinearLayoutCompat
        val layoutWhole = dialogView.findViewById(R.id.layoutWhole) as LinearLayoutCompat
        val cbHalf = dialogView.findViewById(R.id.cbHalf) as AppCompatCheckBox
        val cbWhole = dialogView.findViewById(R.id.cbWhole) as AppCompatCheckBox
        val etNotes = dialogView.findViewById(R.id.etNotes) as TextInputEditText
        val etQuantity = dialogView.findViewById(R.id.etQuantity) as TextInputEditText
        val switchService = dialogView.findViewById(R.id.switchService) as SwitchCompat
        val switchGST = dialogView.findViewById(R.id.switchGST) as SwitchCompat
        val qtyAdd = dialogView.findViewById(R.id.qtyAdd) as AppCompatImageView
        val qtyMin = dialogView.findViewById(R.id.qtyMin) as AppCompatImageView

        if(uniqueItem!=null){
            if(uniqueItem.itemSize == "half"){
                cbHalf.isChecked = true
                sizeType = "half"
            }else{
                cbWhole.isChecked = true
                sizeType = "whole"
            }
            switchService.isChecked = uniqueItem.taxService
            switchGST.isChecked = uniqueItem.taxGst
            etQuantity.setText("${uniqueItem.itemQty}")
            etNotes.setText("${uniqueItem.itemNote}")
        }

        tvProductName.text = productName

        layoutHalf.setOnClickListener {
            additionalPrice = 0
            cbHalf.isChecked = true
            cbWhole.isChecked = false
            sizeType = "half"
        }
        layoutWhole.setOnClickListener {
            additionalPrice = 20000
            cbHalf.isChecked = false
            cbWhole.isChecked = true
            sizeType = "whole"
        }


        qtyAdd.setOnClickListener {
            if(!etQuantity.text.isNullOrEmpty()){
                val getCurrentQty = etQuantity.text.toString().toInt()
                if(getCurrentQty >=0) {
                    val currentData = getCurrentQty + 1
                    etQuantity.setText("$currentData")
                }
            }else{
                etQuantity.setText("1")
            }
        }

        qtyMin.setOnClickListener {
            if(!etQuantity.text.isNullOrEmpty()){
                val getCurrentQty = etQuantity.text.toString().toInt()
                if(getCurrentQty >1) {
                    val currentData = getCurrentQty - 1
                    etQuantity.setText("$currentData")
                }
            }else{
                etQuantity.setText("1")
            }
        }

        btnAddProduct.setOnClickListener {
            function(
                sizeType,
                additionalPrice,
                etQuantity.text.toString(),
                etNotes.text.toString(),
                switchService.isChecked,
                switchGST.isChecked
            )
            alertDialog.dismiss()
        }

        btnClose.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()

        setupFullWidthDialog(alertDialog)
    }


    fun showCustomPrice(
        context: Context,
        function: (custom: String) -> (Unit)
    ) {
        val dialog = AlertDialog.Builder(context)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_custom_input, null)
        dialog.setView(dialogView)
        dialog.setCancelable(true)
        val alertDialog = dialog.create()
        val etCustom = dialogView.findViewById(R.id.etCustom) as TextInputEditText
        val btnCustom = dialogView.findViewById(R.id.btnCustom) as MaterialButton

        btnCustom.setOnClickListener {
            function(etCustom.text.toString())
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    private fun setupFullWidthDialog(alertDialog: AlertDialog) {
        //Layout width setup
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(alertDialog.window?.attributes)

        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT

        alertDialog.window?.attributes = layoutParams
    }
}