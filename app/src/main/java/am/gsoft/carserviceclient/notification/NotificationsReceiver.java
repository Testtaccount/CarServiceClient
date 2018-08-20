/*
 * Copyright 2017 Phillip Hsu
 *
 * This file is part of ClockPlus.
 *
 * ClockPlus is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ClockPlus is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ClockPlus.  If not, see <http://www.gnu.org/licenses/>.
 */

package am.gsoft.carserviceclient.notification;


import am.gsoft.carserviceclient.util.Constant;
import am.gsoft.carserviceclient.util.Constant.Action;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class NotificationsReceiver extends BroadcastReceiver {

  public static final String TAG = "NotificationsReceiver";

  @Override
  public void onReceive(final Context context, final Intent intent) {
    Bundle extras = intent.getExtras();
    if(extras != null){
      if(extras.containsKey(Constant.Extra.EXTRA_APP_NOTIFICATION_ID)){
        // extract the extra-data in the Notification
        int id = intent.getIntExtra(Constant.Extra.EXTRA_APP_NOTIFICATION_ID,-1);

        switch (intent.getAction()){
          case Action.ACTION_SEND_MONTH_NOTIFICATION:
            NotificationsIntentService.startSendMonthNotification(context,id);
            break;
            case Action.ACTION_CANCEL_NOTIFICATION:
            NotificationsIntentService.startCencelMonthNotification(context,id);
            break;
        }
      }
    }


  }

}
