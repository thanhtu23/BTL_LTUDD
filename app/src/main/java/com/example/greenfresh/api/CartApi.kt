package com.example.greenfresh.api

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.greenfresh.model.Cart
import com.example.greenfresh.utils.Server
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject


class CartApi {

    fun addToCart(context: Context, userId : Int, idPro : Int, num: Int){
        var requestQueue: RequestQueue = Volley.newRequestQueue(context)
        var link: String = Server.linkAddCart
        var stringRequest =
            object : StringRequest(Request.Method.POST, link,
                Response.Listener {
                    if (it == "Success") {
                        Toast.makeText(context, "Added to Cart", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                        Log.d("BBB", it.toString())
                    }
                }, Response.ErrorListener {
                    Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
                    Log.d("AAA", it.toString())
                }) {
                @Throws(AuthFailureError::class)
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["userId"] = userId.toString()
                    params["idPro"] = idPro.toString()
                    params["num"] = num.toString()
                    Log.d("AAAA", "getParams: $userId , $idPro, $num")
                    return params
                }
            }
        requestQueue.add(stringRequest)
    }

    fun getCartList(context: Context, uid :Int, callback: (ArrayList<Cart>) -> Unit){
            val list: ArrayList<Cart> = ArrayList()
            val requestQueue: RequestQueue = Volley.newRequestQueue(context)
            val link: String = Server.linkGetCart
            val stringRequest =
                object : StringRequest(Method.POST, link, { response ->
                    var id = 0
                    var name = ""
                    var thumb = ""
                    var price = 0.0
                    var num = 0
                    if (response != null && response.length != 2) {
                        Log.d("getCart", "getCart: $response")
                        val jsonArray: JSONArray = JSONArray(response)
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject: JSONObject = jsonArray.getJSONObject(i)
                            id = jsonObject.getInt("id")
                            name = jsonObject.getString("name")
                            thumb = jsonObject.getString("thumb")
                            price = jsonObject.getDouble("price")
                            num = jsonObject.getInt("num")

                            list.add(Cart(id, name, thumb, price, num))
                            Log.d("FFF", "getCart: $name")

                            callback(list)
                        }
                    }
                }, { error ->
                    Log.d("AAA", error.toString())
                }) {
                    @Throws(AuthFailureError::class)
                    override fun getParams(): Map<String, String> {
                        val params = HashMap<String, String>()
                        params["uid"] = uid.toString()
                        return params
                    }
                }
            requestQueue.add(stringRequest)
    }

    fun getTotal(context: Context, uid: Int, callback: (Double) -> Unit) {
        var total = 0.0
        val requestQueue: RequestQueue = Volley.newRequestQueue(context)
        val link: String = Server.linkGetCart

        val stringRequest = object : StringRequest(
            Method.POST,
            link,
            { response ->
                var price = 0.0
                var num = 0
                if (response != null && response.length != 2) {
                    Log.d("getCart", "getCart: $response")
                    val jsonArray: JSONArray = JSONArray(response)
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject: JSONObject = jsonArray.getJSONObject(i)

                        price = jsonObject.getDouble("price")
                        num = jsonObject.getInt("num")

                        total += price * num
                        Log.d("total price", "total: $total")
                    }
                    callback(total) // Call the callback function with the updated total value
                }
            },
            { error ->
                Log.d("AAA", error.toString())
            }
        ) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["uid"] = uid.toString()
                return params
            }
        }
        requestQueue.add(stringRequest)
    }

    fun isEmptyCart(context: Context, callback: (Boolean) -> Unit) {
        val requestQueue: RequestQueue = Volley.newRequestQueue(context)
        val link: String = Server.linkGetCart

        val stringRequest = object : StringRequest(Method.POST, link, { response ->
            if (response.length <10) {
                callback(true) // the cart is empty
            } else {
                callback(false) // the cart is not empty
            }
        }, { error ->
            Log.d("AAA", error.toString())
            callback(false) // assume the cart is not empty if there's an error
        }) {}
        requestQueue.add(stringRequest)
    }

    fun deleteCart(context: Context,uid: Int , idPro: Int, callback: () -> Unit){
        val requestQueue: RequestQueue = Volley.newRequestQueue(context)
        val link: String = Server.linkDeleteCart
        val stringRequest =
            object : StringRequest(Request.Method.POST, link,
                Response.Listener {
                    if (it == "Success") {
                        Toast.makeText(context, "Deleted product from cart!", Toast.LENGTH_SHORT).show()
                        callback()
                    }else{
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                        Log.d("BBB", it.toString())
                    }
                }, Response.ErrorListener {
                    Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
                    Log.d("AAA", it.toString())
                }) {
                @Throws(AuthFailureError::class)
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["uid"] = uid.toString()
                    params["idPro"] = idPro.toString()

                    Log.d("AAAA", "delete cart: $uid , $idPro")
                    return params
                }
            }
        requestQueue.add(stringRequest)
    }

}