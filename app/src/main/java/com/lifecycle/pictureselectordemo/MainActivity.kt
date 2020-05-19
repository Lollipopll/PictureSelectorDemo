package com.lifecycle.pictureselectordemo

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.lifecycle.pictureselectordemo.utils.PhotoUtils
import com.lifecycle.pictureselectordemo.utils.load
import com.lifecycle.pictureselectordemo.utils.toFile
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onBtnCameraClick(v: View) {

        //调用相机拍照
        PhotoUtils.camera(this) { uri, success, msg ->
            //uri返回的图片 success是否成功 msg错误信息
            if (success) crop(uri)
        }

    }

    fun onBtnSelectorClick(v: View) {

        //调用相册
        PhotoUtils.select(this) { uri, success, msg ->
            if (success) iv.load(uri?.toFile(this))
        }
    }

    private fun crop(uri: Uri?) {
        if (null == uri) return
        PhotoUtils.crop(this)
            .setAspect(1, 1)//设置裁剪比例，不设置则不限制
            .setOutput(333, 666)//设置输出宽高，不设置以裁剪后大小为准
            .build(uri) { uri, success, msg ->
                if (success) iv.load(uri)
            }
    }
}
