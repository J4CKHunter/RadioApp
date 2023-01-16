package com.ayd.radioapp.receiver
//
//import android.content.BroadcastReceiver
//import android.content.Context
//import android.content.Intent
//import android.content.IntentFilter
//import android.os.IBinder
//import android.service.notification.NotificationListenerService
//import android.service.notification.StatusBarNotification
//
//
//open class NotificationListener() : NotificationListenerService() {
//
////    var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
////        override fun onReceive(context: Context?, intent: Intent?) {
////            this@NotificationListener.cancelNotification("com.test.app")
////        }
////    }
//
//    override fun onNotificationPosted(sbn: StatusBarNotification?) {
//        if (sbn != null) {
//            cancelNotification(sbn.key)
//        }
//    }
//
////    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
////        super.onNotificationRemoved(sbn)
////    }
//
//    override fun onBind(intent: Intent?): IBinder? {
//        return super.onBind(intent)
//    }
//
////    override fun onDestroy() {
////        unregisterReceiver(broadcastReceiver)
////        super.onDestroy()
////    }
////
////    override fun onCreate() {
////        super.onCreate()
////        registerReceiver(broadcastReceiver, IntentFilter("com.test.app"))
////    }
//
//
//}