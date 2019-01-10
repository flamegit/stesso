package com.stesso.android.datasource.net

import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

/**
 * Created by peining.li on 2018/3/29.
 */

internal class JSONObjectConverter : Converter.Factory() {

    override fun responseBodyConverter(type: Type, annotations: Array<out Annotation>?, retrofit: Retrofit?): Converter<ResponseBody, *>? {
        return when {
            type is Class<*> && JSONObject::class.java.isAssignableFrom(type) -> Converter<ResponseBody, JSONObject> { JSONObject(it.string()) }
            else -> null
        }
    }

    override fun requestBodyConverter(type: Type, parameterAnnotations: Array<out Annotation>?, methodAnnotations: Array<out Annotation>?, retrofit: Retrofit?): Converter<*, RequestBody>? {
        //为了兼容server, JSONObject的MediaType也写作jsonapi的MediaType
        return when {
            type is Class<*> && JSONObject::class.java.isAssignableFrom(type) -> Converter<JSONObject, RequestBody> { RequestBody.create(MediaType.parse("application/json"), it.toString()) }
            else -> null
        }
    }
}
