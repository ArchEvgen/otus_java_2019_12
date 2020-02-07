package ru.otus.hw03;

import java.lang.management.GarbageCollectorMXBean;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;

import com.sun.management.GarbageCollectionNotificationInfo;

public class GcMonitoring {
    private Map<String, Stats> statsByName = new HashMap<>();

    public void switchOn() {
        List<GarbageCollectorMXBean> gcbeans = java.lang.management.ManagementFactory.getGarbageCollectorMXBeans();
        for ( GarbageCollectorMXBean gcbean : gcbeans ) {
            System.out.println( "GC name:" + gcbean.getName() );
            NotificationEmitter emitter = (NotificationEmitter) gcbean;
            NotificationListener listener = (notification, handback ) -> {
                if ( notification.getType().equals( GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION ) ) {
                    GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo.from( (CompositeData) notification.getUserData() );
                    String gcName = info.getGcName();
                    String gcAction = info.getGcAction();
                    String gcCause = info.getGcCause();

                    long startTime = info.getGcInfo().getStartTime();
                    long duration = info.getGcInfo().getDuration();

                    Stats stats = statsByName.get(gcName);
                    if (stats == null) {
                        stats = new Stats();
                        stats.name = gcName;
                        statsByName.put(gcName, stats);
                    }
                    stats.callCount++;
                    stats.sumDuration += duration;
                    stats.maxDuration = Math.max(stats.maxDuration, duration);

                    System.out.println( "start:" + startTime + " Name:" + gcName + ", action:" + gcAction + ", gcCause:" + gcCause + "(" + duration + " ms)" );
                }
            };
            emitter.addNotificationListener( listener, null, null );
        }
    }

    public static class Stats {
        protected String name;
        protected long callCount;
        protected long sumDuration;
        protected long maxDuration;
    }
}
