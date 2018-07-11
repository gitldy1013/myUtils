/**
 * @Title: ${file_name}
 * @Package ${package_name}
 * @Description: ${todo}
 * @author ceshi
 * @date 2018/7/617:04
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Author: Liudongyang
 * @Description:
 * @Date: Created in 17:04 2018/7/6
 * @Modified By:
 * @Version: 1.0.0
 */
public class Test {


  public enum Kline {
    T2100("23:30", "21:00", new Integer("2100"), 0),
    T2330("09:00", "23:30", new Integer("2330"), 90 * 3600),//23:30到21:00的绝对时间秒值s 下同（排除休息时间）
    T0900("10:15", "09:00", new Integer("0900"), 90 * 3600),
    T1015("10:30", "10:15", new Integer("1015"), 75 * 3600),
    T1030("11:30", "10:30", new Integer("1030"), 75 * 3600),
    T1130("13:30", "11:30", new Integer("1130"), 60 * 3600),
    T1330("15:00", "13:30", new Integer("1330"), 60 * 3600),
    T1500("21:00", "15:00", new Integer("1500"), 90 * 3600);

    private String preTime;
    private String timeStr;
    private int time;
    private int pick;

    Kline(String preTime, String timeStr, int pink, int time) {
      this.preTime = preTime;
      this.timeStr = timeStr;
      this.pick = pink;
      this.time = time;
    }

    public String getPreTime() {
      return preTime;
    }

    public void setPreTime(String preTime) {
      this.preTime = preTime;
    }

    public String getTimeStr() {
      return timeStr;
    }

    public void setTimeStr(String timeStr) {
      this.timeStr = timeStr;
    }

    public int getTime() {
      return time;
    }

    public void setTime(int time) {
      this.time = time;
    }

    public int getPick() {
      return pick;
    }

    public void setPick(int pick) {
      this.pick = pick;
    }
  }


  public static void main(String[] args) throws ParseException {
    int zhouqi = 1800;//周期 (秒数)
    String time = "10:35";//传入的时间值
    //找到时间值对应区间 并得到时间
    String replace = time.replace(":", "");
    int timeInt = new Integer(replace);
    int finl = 0;
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
    Date dt = sdf.parse(time);
    if (timeInt >= Kline.T2100.getPick() && timeInt <= Kline.T2330.getPick()) {
      getTime(Kline.T2100.timeStr, Kline.T2330.getTimeStr(), Kline.T2100.getTime(), Kline.T2100.getPick(), Kline
          .T2330.getPick(), Kline.T2330.getPreTime(), zhouqi, time, sdf, dt);
    } else if (timeInt >= Kline.T0900.getPick() && timeInt <= Kline.T1015.getPick()) {
      getTime(Kline.T0900.timeStr, Kline.T1015.getTimeStr(), Kline.T0900.getTime(), Kline.T0900.getPick(), Kline
          .T1015.getPick(), Kline.T1015.getPreTime(), zhouqi, time, sdf, dt);
    } else if (timeInt >= Kline.T1030.getPick() && timeInt <= Kline.T1130.getPick()) {
      getTime(Kline.T1030.timeStr, Kline.T1130.getTimeStr(), Kline.T1030.getTime(), Kline.T1030.getPick(), Kline
          .T1130.getPick(), Kline.T1130.getPreTime(), zhouqi, time, sdf, dt);
    } else if (timeInt >= Kline.T1330.getPick() && timeInt <= Kline.T1500.getPick()) {
      getTime(Kline.T1330.timeStr, Kline.T1500.getTimeStr(), Kline.T1330.getTime(), Kline.T1330.getPick(), Kline
          .T1500.getPick(), Kline.T1500.getPreTime(), zhouqi, time, sdf, dt);
    }
  }

  private static void getTime(String timeStrT1, String timeStrT2, int timeKline, int pinkKlineT1, int pinkKlineT2, String
      preTime, int zhouqi, String time,
      SimpleDateFormat
          sdf,
      Date
          dt)
      throws
      ParseException {
    int finl;
    int more = (int) ((sdf.parse(time).getTime() - sdf.parse(timeStrT1).getTime()) / 1000);
    finl = more + timeKline;//距离初始点的秒数
    //TODO...
    int p = zhouqi - finl % zhouqi;
    Calendar timeTmp = Calendar.getInstance();
    timeTmp.setTime(dt);
    timeTmp.add(Calendar.SECOND, p);//时间增加指定秒
    Date dt1 = timeTmp.getTime();//最终时间
    String reStr = sdf.format(dt1);
    String replace2 = reStr.replace(":", "");
    int timeInt2 = new Integer(replace2);
    if (!(timeInt2 >= pinkKlineT1 && timeInt2 <= pinkKlineT2)) {
      int less = (int) ((sdf.parse(reStr).getTime() - sdf.parse(timeStrT2).getTime()));
      Calendar timeTmp2 = Calendar.getInstance();
      timeTmp2.setTime(sdf.parse(preTime));
      timeTmp2.add(Calendar.SECOND, less / 1000);//时间增加指定秒
      Date dt2 = timeTmp2.getTime();
      String reStr2 = sdf.format(dt2);
      System.out.println(reStr2);
    } else {
      System.out.println(reStr);
    }
  }
}
