package hu.leopph.inventoree


import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import androidx.core.app.NotificationCompat


// This whole thing is a mess...
class MySimpleImmutableNotificationManagerHelper private constructor(private val mContext: Context) { // XD
    companion object Builder : MySimpleImmutableNotificationManagerHelperBuilder {
        private const val sNotificationChannelID = "Eine Kleine Micky Maus Jaa"
        private const val sNotificationChannelName = "MeGa NoTiFiCaTiOnS"
        private const val sNotifcationDescription = "Mega Awesome Channel for Mega Awesome Notifications"
        private const val sNotificationTitle = "Über Notification"
        private var sNotificationChannel: NotificationChannel? = null
        private var sNotificationManager: NotificationManager? = null
        private var sNextNotificationId = 0
            get() = field++

        override fun buildMySimpleImmutableNotificationManagerHelper(context: Context) : MySimpleImmutableNotificationManagerHelper {
            if (sNotificationManager == null)
                getNotificationManagerForMySimpleImmutableNotificationManagerHelper(context)

            if (sNotificationChannel == null)
                createChannelForMySimpleImmutableNotificationManagerHelper()

            return MySimpleImmutableNotificationManagerHelper(context) // Im sorry...
        }

        private fun createChannelForMySimpleImmutableNotificationManagerHelper() { // Im seriously very sorry
            sNotificationChannel = NotificationChannel(
                sNotificationChannelID,
                sNotificationChannelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )

            sNotificationChannel?.enableLights(true)
            sNotificationChannel?.enableVibration(true)
            sNotificationChannel?.lightColor = Color.MAGENTA
            sNotificationChannel?.description = sNotifcationDescription
            sNotificationManager?.createNotificationChannel(sNotificationChannel!!)

        }

        private fun getNotificationManagerForMySimpleImmutableNotificationManagerHelper(context: Context) {
            sNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }
    }


    fun sendNotification(message: String) {
        sNotificationManager?.notify(
            sNextNotificationId,
            NotificationCompat.Builder(mContext, sNotificationChannelID)
                .setContentTitle(sNotificationTitle)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_baseline_cloud_upload_24)
                .build()
        )
    }
}