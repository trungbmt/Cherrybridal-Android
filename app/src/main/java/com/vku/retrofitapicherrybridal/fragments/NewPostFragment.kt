package com.vku.retrofitapicherrybridal.fragments

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.gson.JsonObject
import com.vku.retrofitapicherrybridal.AppConfig
import com.vku.retrofitapicherrybridal.MainApplication
import com.vku.retrofitapicherrybridal.R
import com.vku.retrofitapicherrybridal.activities.DashboardActivity
import com.vku.retrofitapicherrybridal.client.PostClient
import kotlinx.android.synthetic.main.fragment_new_post.*
import kotlinx.android.synthetic.main.fragment_new_post.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class NewPostFragment : Fragment() {
    lateinit var rootView : View
    val MEDIA_PICKER_SELECT = 0
    var mediaUri : Uri? = null
    val postClient: PostClient = AppConfig.retrofit.create(PostClient::class.java)
    var MEDIA_TYPE : MediaType? = null
    val READ_STORAGE_PERMISSION_REQUEST_CODE = 1


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_new_post, container, false)
        rootView.vvBackground.setOnCompletionListener {
            it.start()
        }

        rootView.btnSelectMedia.setOnClickListener {
            val pickIntent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            pickIntent.setType("*/*")
            pickIntent.putExtra(
                Intent.EXTRA_MIME_TYPES,
                arrayOf("image/*", "video/*")
            )
            startActivityForResult(pickIntent, MEDIA_PICKER_SELECT)
        }


        rootView.btnPost.setOnClickListener {
            if(checkPermissionForReadExtertalStorage()) {
                if(mediaUri!=null && MEDIA_TYPE!=null) {
                    uploadPost()
                } else {
                    Log.d("uploadPost", "Mediaurl $mediaUri, MediaType $MEDIA_TYPE")
                }
            } else {
                requestPermissionForReadExtertalStorage()
            }
        }
        return rootView
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode === RESULT_OK) {
            mediaUri = data?.data
            if (mediaUri.toString().contains("image")) {
                MEDIA_TYPE = MediaType.parse("image/*")
                showImage()
            }
            else if(mediaUri.toString().contains("video")) {
                MEDIA_TYPE = MediaType.parse("video/*")
                showVideo()
            }
        }
    }
    fun showImage() {
        vvBackground.visibility = View.GONE
        ivBackground.visibility = View.VISIBLE
        ivBackground.setImageURI(mediaUri)

    }
    fun showVideo() {
        vvBackground.visibility = View.VISIBLE
        ivBackground.visibility = View.GONE
        vvBackground.setVideoURI(mediaUri)
        vvBackground.start()
    }
    fun getRealPathFromURI(contentUri: Uri): String? {
        var path: String? = null
        val proj = arrayOf(MediaStore.MediaColumns.DATA)
        val cursor: Cursor? =
            this.context?.getContentResolver()?.query(contentUri, proj, null, null, null)
        if (cursor?.moveToFirst()!!) {
            val column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
            path = cursor.getString(column_index)
        }
        cursor.close()
        return path
    }
    fun uploadPost() {
        vvBackground.stopPlayback()
        var file = File(mediaUri?.let { getRealPathFromURI(it) })
        var requestFile = RequestBody.create(MEDIA_TYPE, file)
        var description = inputDescription.text.toString()
        var body: MultipartBody.Part =
            MultipartBody.Part.createFormData("media", file.name, requestFile)
        var headers = HashMap<String, String>()
        var token = MainApplication.userSharedPreferences().getString("token", null)
        headers.put("Authorization", "Bearer " + token)

        Log.d("uploadPost", "token  "+token)

        val postService: Call<JsonObject> = postClient.createPost(headers, body, description)


        Log.d("uploadPost", "File Path  "+file.absolutePath)

        var pDialog = SweetAlertDialog(this.context, SweetAlertDialog.PROGRESS_TYPE)
        pDialog.setCancelable(false)
        pDialog.show()
        postService.enqueue(object : Callback<JsonObject>{
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                pDialog.dismiss()
                Log.d("uploadPost", "Failed "+t.message)
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                pDialog.dismiss()
                Log.d("uploadPost", "Response "+response.body().toString())
                (this@NewPostFragment.activity as DashboardActivity).backToBlogFragment()
            }

        })
    }
    fun checkPermissionForReadExtertalStorage(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val result = context!!.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            return result == PackageManager.PERMISSION_GRANTED
        }
        return false
    }

    @Throws(Exception::class)
    fun requestPermissionForReadExtertalStorage() {
        try {
            ActivityCompat.requestPermissions(
                (context as Activity?)!!,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                READ_STORAGE_PERMISSION_REQUEST_CODE
            )
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }
    companion object {
        @JvmStatic
        fun newInstance() = NewPostFragment().apply {
            arguments = Bundle().apply {}
        }
    }
}