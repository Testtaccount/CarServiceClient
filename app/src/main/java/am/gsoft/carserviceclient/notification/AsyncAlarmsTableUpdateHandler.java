///*
// * Copyright 2017 Phillip Hsu
// *
// * This file is part of ClockPlus.
// *
// * ClockPlus is free software: you can redistribute it and/or modify
// * it under the terms of the GNU General Public License as published by
// * the Free Software Foundation, either version 3 of the License, or
// * (at your option) any later version.
// *
// * ClockPlus is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// * GNU General Public License for more details.
// *
// * You should have received a copy of the GNU General Public License
// * along with ClockPlus.  If not, see <http://www.gnu.org/licenses/>.
// */
//
//package am.gsoft.carserviceclient.alarms;
//
//import android.content.Context;
//
///**
// * Created by Phillip Hsu on 7/1/2016.
// * TODO: Consider making an AsyncDatabaseChangeHandlerWithSnackbar abstract class
// */
//public final class AsyncAlarmsTableUpdateHandler extends AsyncDatabaseTableUpdateHandler<Alarm, AlarmsTableManager> {
//
//    private static final String TAG = "AsyncAlarmsTableUpdateHandler";
//
//    private final AlarmController mAlarmController;
//
//    /**
//     * @param context the Context from which we getAppNotification the application context
//     * @param alarmController
//     */
//    public AsyncAlarmsTableUpdateHandler(Context context,  AlarmController alarmController) {
//        super(context);
//        mAlarmController = alarmController;
//    }
//
//    @Override
//    protected AlarmsTableManager onCreateTableManager(Context context) {
//        return new AlarmsTableManager(context);
//    }
//
//    @Override
//    protected void onPostAsyncDelete(Integer result, final Alarm alarm) {
//        mAlarmController.cancelNotification(alarm);
//    }
//
//    @Override
//    protected void onPostAsyncInsert(Long result, Alarm alarm) {
//        mAlarmController.scheduleNotification(alarm, true);
//    }
//
//    @Override
//    protected void onPostAsyncUpdate(Long result, Alarm alarm) {
//        mAlarmController.scheduleNotification(alarm, true);
//    }
//}
