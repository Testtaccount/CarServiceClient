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

import am.gsoft.carserviceclient.data.database.AppDatabase;
import am.gsoft.carserviceclient.data.database.entity.AppNotification;
import android.app.IntentService;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.support.annotation.Nullable;
import java.util.List;

public class OnBootUpNotificationSchedulerIntentService extends IntentService {


    public OnBootUpNotificationSchedulerIntentService() {
        super("OnBootUpNotificationSchedulerIntentService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {

            NotificationsController notificationsController=new NotificationsController(getApplicationContext());
            LiveData<List<AppNotification>> liveData = AppDatabase.getInstance(getApplicationContext()).mAppNotificationDao().getAllEnabledNotifications();

            liveData.observeForever(new Observer<List<AppNotification>>() {
                @Override
                public void onChanged(@Nullable List<AppNotification> appNotifications) {
                    liveData.removeObserver(this);
                    for(AppNotification mn: appNotifications){
                        notificationsController.scheduleNotification(mn);
                    }
                }
            });

//            AlarmController controller = new AlarmController(this);
//            // IntentService works in a background thread, so this won't hold us up.
//            AlarmCursor cursor = new AlarmsTableManager(this).queryEnabledAlarms();
//            while (cursor.moveToNext()) {
//                Alarm alarm = cursor.getItem();
//                if (!alarm.isEnabled()) {
//                    throw new IllegalStateException(
//                        "queryEnabledAlarms() returned alarm(s) that aren't enabled");
//                }
//                controller.scheduleNotification(alarm, true);
//            }
//            cursor.close();

        }
    }

}
