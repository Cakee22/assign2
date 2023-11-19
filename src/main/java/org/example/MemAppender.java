package org.example;


import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.*;



    public class MemAppender extends AppenderSkeleton {

        private int maxSize;
        private long DiscardedLogCount;
        public List<LoggingEvent> loggingEventList;
       private MemAppender(int maxSize){
        this.maxSize = maxSize;
    }
        private static MemAppender instance;

        private MemAppender (List<LoggingEvent> list) {
            super();
            loggingEventList = list;
        }



        public static MemAppender getInstance (List<LoggingEvent> list) {
            if (instance == null) {
                if(null == list){
                    instance.loggingEventList = new ArrayList<>();
                }else{
                    instance.loggingEventList = list;
                }
                return instance;
            }
            return instance;
        }





        public static MemAppender getInstance(int maxSize, List<LoggingEvent> list, Layout layout) {
            if(null == instance){
                if(null == layout) {
                    layout = new PatternLayout(PatternLayout.DEFAULT_CONVERSION_PATTERN);
                }
                instance = new MemAppender(maxSize);
                instance.setLayout(layout);
                if(null == list){
                    instance.loggingEventList = new ArrayList<>();
                }else{
                    instance.loggingEventList = list;
                }
                return instance;
            }
            return instance;
        }




    public Layout getLayout(){
            return layout;
        }
        public void setLayout(Layout layout){
            this.layout= layout;
        }

        @Override
        public void append(LoggingEvent event) {
        if(loggingEventList==null){
            loggingEventList=new ArrayList<>(maxSize);
        }
        else if(loggingEventList.size()>=maxSize){
            loggingEventList.remove(0);
            DiscardedLogCount++;
        }
        loggingEventList.add(event);
        }

        @Override
        public void close() {

        }

        @Override
        public boolean requiresLayout() {
            return false;
        }

        public List<LoggingEvent> getCurrentLogs() {

            return Collections.unmodifiableList(loggingEventList);
        }


        public List<String> getEventString() {
            List<String> loggingEventListString = new ArrayList<>();
            for (LoggingEvent loggingEvent : loggingEventList) {
                if (getLayout() != null) {
                    loggingEventListString.add(getLayout().format(loggingEvent));
                } else {
                    loggingEventListString.add(loggingEvent.getRenderedMessage());
                }

            }
            return Collections.unmodifiableList(loggingEventListString);
        }

        public void printLogs() {
            if (loggingEventList != null) {
                for (LoggingEvent loggingEvent : loggingEventList) {
                    if (getLayout() != null) {
                        System.out.println(layout.format(loggingEvent));
                    } else {
                        System.out.println(loggingEvent.getRenderedMessage());
                    }
                }
            }
            loggingEventList.clear();
        }
    public void setMaxSize (int maxSize) {
        this.maxSize = maxSize;
        adjustLogListSize();
    }
        public long getDiscardedLogCount() {
            return DiscardedLogCount;
        }
    private void adjustLogListSize () {
        if (loggingEventList.size() != 0) {
            while (loggingEventList.size() > maxSize) {
                loggingEventList.remove(0);
            }
        }

    }



    }
