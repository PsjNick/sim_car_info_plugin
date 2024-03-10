package com.psj.sim_car_info_plugin.sim_car_info_plugin

import android.Manifest
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.ContentResolver
import android.content.Context
import android.content.Context.TELEPHONY_SUBSCRIPTION_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.Telephony
import android.telephony.SubscriptionManager
import androidx.core.app.ActivityCompat
import com.google.gson.Gson
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result


/** SimCarInfoPlugin */
class SimCarInfoPlugin : FlutterPlugin, EventChannel.StreamHandler, MethodCallHandler , ActivityAware , BroadcastReceiver(){

    private lateinit var channel: MethodChannel
    private lateinit var channelEvent: EventChannel
    private lateinit var context: Context
    private lateinit var activity: Activity
    private var eventSink: EventChannel.EventSink? = null

    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        context = flutterPluginBinding.applicationContext;
        context.registerReceiver(this, IntentFilter("android.provider.Telephony.SMS_RECEIVED"))
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "sim_car_info_plugin")
        channelEvent = EventChannel(flutterPluginBinding.binaryMessenger,"readsms")
        channel.setMethodCallHandler(this)
        channelEvent.setStreamHandler(this)
    }


    override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
        eventSink = events
    }

    override fun onCancel(arguments: Any?) {
        eventSink = null
    }

    override fun onMethodCall(call: MethodCall, result: Result) {


        if (call.method == "req_persmissions") {

//            ActivityCompat.requestPermissions(
//                context,
//                arrayOf(Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_SMS),
//                321
//            )
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                ActivityCompat.requestPermissions(
//                    context,
//                    arrayOf(Manifest.permission.READ_PHONE_NUMBERS),
//                    13432
//                )
//            }
//
//            result.success(null)

        } else if ((call.method == "sim_car_info")) {

            try {

                println("Manifest.permission.READ_SMS: " + ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_SMS
                ) )


                println("Manifest.permission.READ_PHONE_NUMBERS: " + ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_PHONE_NUMBERS
                ) )

                println("Manifest.permission.READ_PHONE_STATE: " + ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_PHONE_STATE
                ) )

                if (
                    ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.READ_SMS
                    ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.READ_PHONE_STATE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {


                    println("Build.VERSION.SDK_INT: " + Build.VERSION.SDK_INT)

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                        try {
                            val tmSub =
                                context.getSystemService(TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager

                            val activeSubscriptionInfoList = tmSub.activeSubscriptionInfoList

                            println("sim car info size: " + activeSubscriptionInfoList.size)


                            val simArr = mutableListOf<Map<String,Any?>>()

                            for (subscriptionInfo in activeSubscriptionInfoList) {
                                println(subscriptionInfo)

                                val infoMap  = mutableMapOf<String,Any?>()

                                infoMap["CarrierName"] = subscriptionInfo.carrierName
                                infoMap["CountryIso"] = subscriptionInfo.countryIso
                                infoMap["DisplayName"] = subscriptionInfo.displayName
                                infoMap["SimSlotIndex"] = subscriptionInfo.simSlotIndex
                                infoMap["Number"] = subscriptionInfo.number

                                simArr.add(infoMap)

                            }


                            val gson = Gson()

                            val  infoGson = gson.toJson(simArr);

                            // 返回数据
                            result.success(infoGson)

                        } catch (e: Exception) {
                            result.success(null)
                            println("sim_car_info 出现错误")
                            e.printStackTrace()
                        }

                    } else {
                        println("sim_car_info 安卓版本小于23")
                        result.success(null)
                    }

                } else {
                    println("sim_car_info 权限不够")
                    result.success(null)
                }

            } catch (e: Exception) {
                result.success(null)
                println("sim_car_info 出现错误")
                e.printStackTrace()
            }

        }


    }

    override fun onReceive(p0: Context?, p1: Intent?) {
        /**
         * Get the messages through the broadcast receiver
         * using the Telephony.Sms.Intent
         */

        val bundle: Bundle? = p1?.getExtras()

        val slot = bundle?.getInt("slot", -1)

        println("this msg from slot index: $slot")

        println(bundle?.toString())

        var displayMessageBody = ""
        var originatingAddress = ""
        var contactName = ""
        var timestampMillis = ""

        for (sms in Telephony.Sms.Intents.getMessagesFromIntent(p1)) {

            displayMessageBody += sms.displayMessageBody
            if (originatingAddress.isEmpty()){
                originatingAddress = sms.originatingAddress.toString().replace(" ","")
            }

            if(timestampMillis.isEmpty()){
                timestampMillis =  sms.timestampMillis.toString()
            }

//        var data = listOf(
//          sms.displayMessageBody,
//          sms.originatingAddress.toString(),
//          sms.timestampMillis.toString(),
//          slot,
//        )
//        eventSink?.success(data)
        }

        val resolver: ContentResolver = context.contentResolver

        val phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, arrayOf(
            ContactsContract.Contacts.DISPLAY_NAME, // 名字
            ContactsContract.CommonDataKinds.Phone.NUMBER // 号码
        ),null,null,null)


        if(phoneCursor != null){

            while (phoneCursor.moveToNext()){

                // 获得联系人姓名
                val displayNameColumn = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                val displayContactName = phoneCursor.getString(displayNameColumn)
                // 获得联系人号码
                val NumberColumn = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                val contactNumber = phoneCursor.getString(NumberColumn).replace(" ","")

                if(contactNumber == originatingAddress){
                    contactName = displayContactName
                    break
                }

            }

        }


        val data = listOf(
            displayMessageBody,
            originatingAddress,
            timestampMillis,
            slot,
            contactName,
        )

        eventSink?.success(data)

    }



    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }


    override fun onAttachedToActivity(p0: ActivityPluginBinding) {
        activity = p0.activity
    }

    override fun onDetachedFromActivityForConfigChanges() {
        TODO("Not yet implemented")
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        TODO("Not yet implemented")
    }

    override fun onDetachedFromActivity() {
        TODO("Not yet implemented")
    }



}
