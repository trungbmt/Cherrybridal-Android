package com.vku.retrofitapicherrybridal.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.vku.retrofitapicherrybridal.AppConfig
import com.vku.retrofitapicherrybridal.MainApplication
import com.vku.retrofitapicherrybridal.R
import com.vku.retrofitapicherrybridal.Tools
import com.vku.retrofitapicherrybridal.adapter.OrderItemAdapater
import com.vku.retrofitapicherrybridal.client.CartClient
import com.vku.retrofitapicherrybridal.model.Cart
import kotlinx.android.synthetic.main.activity_order.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class OrderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        var jsonCarts = intent.getStringExtra("carts")
        var price = intent.getIntExtra("price", 0)


        val myType = object : TypeToken<ArrayList<Cart>>() {}.type
        var carts = Gson().fromJson<ArrayList<Cart>>(jsonCarts, myType)

        var orderItemAdapater = OrderItemAdapater(carts, this)

        order_item_rv.layoutManager = LinearLayoutManager(this)
        order_item_rv.adapter = orderItemAdapater

        total_product_amount.text = "(${carts.size} items)"
        total_price_num.text = Tools.format_currency(price)
        final_price_num.text = Tools.format_currency(price)
        abort_btn.setOnClickListener {
            finish()
        }

        btn_order.setOnClickListener {

            var pDialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
            pDialog.setCancelable(false)
            pDialog.show()

            val cartClient = AppConfig.retrofit.create(CartClient::class.java)

            var headers = HashMap<String, String>()
            var token = MainApplication.userSharedPreferences().getString("token", null)

            var queries = HashMap<String, String>()
            queries.put("order_full_name", inName.text.toString())
            queries.put("order_phone", inNumber.text.toString())
            queries.put("order_city", inCity.text.toString())
            queries.put("order_province", inProvince.text.toString())
            queries.put("order_address", inAddress.text.toString())
            if(token!=null) {
                headers.put("Authorization", "Bearer " + token)

                val cartService = cartClient.order(headers, queries)
                cartService.enqueue(object : Callback<JsonObject>{
                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                        pDialog.dismiss()
                        Log.d("CHECKOUT", "Failure ${t.message}")
                    }

                    override fun onResponse(
                        call: Call<JsonObject>,
                        response: Response<JsonObject>
                    ) {
                        pDialog.dismiss()
                        if(response.isSuccessful) {
                            pDialog = SweetAlertDialog(this@OrderActivity, SweetAlertDialog.SUCCESS_TYPE)
                            pDialog.titleText = response.body()?.get("message")?.asString
                            pDialog.setConfirmClickListener {
                                val returnIntent = Intent()
                                returnIntent.putExtra("success", true)
                                setResult(Activity.RESULT_OK, returnIntent)
                                this@OrderActivity.finish()
                            }
                            pDialog.show()
                        } else {
                            val jObjError = JSONObject(response.errorBody()?.string())
                            val message = jObjError.getString("message")
                            pDialog = SweetAlertDialog(this@OrderActivity, SweetAlertDialog.ERROR_TYPE)
                            pDialog.titleText = message
                            pDialog.show()
                        }
                    }

                })
            }
        }
    }
}