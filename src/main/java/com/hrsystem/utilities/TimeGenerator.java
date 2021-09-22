package com.hrsystem.utilities;

import java.util.Calendar;

public class TimeGenerator {
    private Calendar calendar;

    public TimeGenerator() {
        this.calendar = Calendar.getInstance();
    }

    public Calendar getCurrentCalender() {
        return this.calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public int getCurrentYear() {
        return getCurrentCalender().get(Calendar.YEAR);
    }
}
/*
public class ScheduleService{
  private Calendar calendar;
  public ScheduleService(){
    calendar = Calendar.getInstance();
  }
  public Schedule getTodaySchedule(){
    int day = calendar.get(Calendar.DAY_OF_WEEK);
    Schedule s = lookupAccordingToDay(day);
  }

  public setCalendar(Calendar c){
    calendar = c;
  }
}

@Test
public void testGetTodaySchedule() {
 Calendar c = Mockito.mock(Calendar.class);
 Mockito.when(c.get(Calendar.DAY_OF_WEEK)).thenReturn(2);

 ScheduleService sService = new SomeStrangeService();
 //there has to be a way to set the current calendar
 sService.setCalendar(c);
 Schedule schedule = sService.getTodaySchedule();
 //Assert your schedule values
}

 */
