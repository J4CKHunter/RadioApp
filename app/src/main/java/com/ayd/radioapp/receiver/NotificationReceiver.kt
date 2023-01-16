package com.ayd.radioapp.receiver

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.admin.DevicePolicyManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.res.ResourcesCompat
import com.ayd.radioapp.R
import com.ayd.radioapp.view.LockscreenActivity
import com.ayd.radioapp.view.MainActivity
import com.ayd.radioapp.viewmodel.MainViewModel


open class NotificationReceiver(private val mainViewModel: MainViewModel) : BroadcastReceiver() {

    private lateinit var notificationManager: NotificationManagerCompat

    override fun onReceive(context: Context, intent: Intent) {

/*        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val notificationView = layoutInflater.inflate(R.layout.custom_notification_layout, null)
        val button1 = notificationView.findViewById<Button>(R.id.radioOpeningSwitch)*/


        if (intent.action == "notification_clicked"){

/*            val toast = Toast.makeText(context,"You clicked on notification button", Toast.LENGTH_SHORT)
            toast.show()*/

            if(intent.getStringExtra("type").equals("openClose")){

                notificationManager = NotificationManagerCompat.from(context)

                // bu burada olmazsa permanent notification üzerinden tekrardan fullscreen notification gönderilmiyor
                notificationManager.cancel(15);

//                val contentIntent = Intent("com.test.app")
                val contentIntent = Intent(context, LockscreenActivity::class.java)
                val contentPendingIntent = PendingIntent.getActivity(context, 0, contentIntent, 0)

                val fullScreenIntent = Intent(context, LockscreenActivity::class.java)
                val fullScreenPendingIntent = PendingIntent.getActivity(context, 0, fullScreenIntent, 0)

                val builder =  NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.ic_launcher_background)
    //                .setColor(ResourcesCompat.getColor(R.color.purple_200, null))
                    .setContentTitle("Babanne Radio is running in the background.")
                    .setAutoCancel(true)
//                    .setContentIntent(contentPendingIntent)
                    .setFullScreenIntent(fullScreenPendingIntent, true)
    //                .setPriority(NotificationCompat.PRIORITY_HIGH)
    //                .setCategory(NotificationCompat.CATEGORY_ALARM)

                notificationManager.notify(15, builder.build())

                if(MainViewModel.isOpen){
                    mainViewModel.pauseAudio()
                    MainViewModel.isOpen = false
                    //button1.setBackgroundColor(Color.WHITE)
                }else{
                    mainViewModel.playAudio()
                    MainViewModel.isOpen = true
                }
            }

            if(intent.getStringExtra("type").equals("nextChannel")){
                if(MainViewModel.isOpen){
                    mainViewModel.nextAudio()
                }
            }
            if(intent.getStringExtra("type").equals("previousChannel")){
                if(MainViewModel.isOpen){
                    mainViewModel.backAudio()
                }
            }
            if(intent.getStringExtra("type").equals("volumeUp")){
                mainViewModel.volumeUp()
            }
            if(intent.getStringExtra("type").equals("volumeDown")){
                mainViewModel.volumeDown()
            }


        }

    }

}