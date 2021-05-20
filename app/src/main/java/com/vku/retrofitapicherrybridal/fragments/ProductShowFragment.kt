package com.vku.retrofitapicherrybridal.fragments

import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.Glide
import com.google.gson.JsonObject
import com.vku.retrofitapicherrybridal.AppConfig
import com.vku.retrofitapicherrybridal.MainApplication
import com.vku.retrofitapicherrybridal.R
import com.vku.retrofitapicherrybridal.Tools
import com.vku.retrofitapicherrybridal.client.CartClient
import com.vku.retrofitapicherrybridal.model.Product
import com.vku.retrofitapicherrybridal.model.ProductDetail
import com.vku.retrofitapicherrybridal.viewmodel.ProductViewModel
import kotlinx.android.synthetic.main.product_show.*
import kotlinx.android.synthetic.main.product_show.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProductShowFragment () : Fragment() {
    lateinit var rootView : View
    lateinit var details : ArrayList<ProductDetail>
    var cartClient = AppConfig.retrofit.create(CartClient::class.java)
    var detail_id = 0
    var product = Product()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.product_show, container, false)

        getProduct()

        rootView.btnReduce.setOnClickListener {
            var amount = Integer.parseInt(tvAmount.text.toString())
            if(amount>1) {
                amount--
                tvAmount.text = "${amount}"
            }
        }
        rootView.btnIncrease.setOnClickListener {
            var amount = Integer.parseInt(tvAmount.text.toString())
            val radioButton: View = rgSize.findViewById(rgSize.checkedRadioButtonId)
            val index: Int = rgSize.indexOfChild(radioButton)
            var maximum = details.get(index).amount
            if(amount<maximum) {
                amount++
                tvAmount.text = "${amount}"
            }
        }
        rootView.btnCartAdd.setOnClickListener {
            cartAdd()
        }

        return rootView
    }
    fun cartAdd() {
        var headers = HashMap<String, String>()
        var token = MainApplication.userSharedPreferences().getString("token", null)
        headers.put("Authorization", "Bearer " + token)

        var amount = Integer.parseInt(tvAmount.text.toString())

        val cartService : Call<JsonObject> = cartClient.storeCart(headers, product.id, detail_id, amount)
        var pDialog = SweetAlertDialog(this.context, SweetAlertDialog.PROGRESS_TYPE)
        pDialog.setCancelable(false)
        pDialog.show()
        cartService.enqueue(object : Callback<JsonObject>{
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                pDialog.dismiss()
                pDialog = SweetAlertDialog(this@ProductShowFragment.context, SweetAlertDialog.ERROR_TYPE)
                pDialog.setTitleText("Lỗi...")
                        .setContentText(t.localizedMessage)
                        .show();
                Log.d("ADDCART", t.message.toString())
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                pDialog.dismiss()
                Log.d("ADDCART", response.body().toString())
            }

        })
    }
    fun getProduct() {
        val bundle = this.arguments
        if (bundle != null) {
            val id = bundle.getInt("product_id", 1)
            val productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
            productViewModel.getProduct(id)
            productViewModel.getProductLiveData().observe(this, Observer {
                product = it
                tvName.text = it.name
                Glide.with(this)
                        .load(it.getImageUrl())
                        .centerCrop()
                        .placeholder(R.drawable.loading)
                        .into(imgProduct)
                ratingBar.rating = it.rating
                details = it.productDetails
                it.productDetails.forEach {
                    var rbSize : RadioButton = RadioButton(context)
                    rbSize.id = View.generateViewId()
                    rbSize.text = it.size
                    rbSize.background = ContextCompat.getDrawable(rbSize.context, R.drawable.radio_flat_selector)
                    rbSize.buttonDrawable = ColorDrawable(Color.TRANSPARENT)
                    rbSize.textAlignment = View.TEXT_ALIGNMENT_CENTER
                    rbSize.setPadding(40,30,40,30)
                    rbSize.typeface = Typeface.DEFAULT_BOLD
                    val params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                    params.setMargins(0, 0, 10, 0)
                    rbSize.layoutParams = params

                    rgSize.addView(rbSize)
                }
                rgSize.setOnCheckedChangeListener { group, checkedId ->
                    val radioButton: View = rgSize.findViewById(checkedId)
                    val index: Int = rgSize.indexOfChild(radioButton)
                    val detail = it.productDetails.get(index)

                    detail_id = detail.id

                    tvPrice.text = Tools.format_currency(detail.price)
                    tvDetailAmount.text = "Available: ${detail.amount}"

                    var amount = Integer.parseInt(tvAmount.text.toString())
                    if(amount>detail.amount) {
                        tvAmount.text = "${detail.amount}"
                    }
                }
                rgSize.check(rgSize.getChildAt(0).id)
            })
        }
    }
}