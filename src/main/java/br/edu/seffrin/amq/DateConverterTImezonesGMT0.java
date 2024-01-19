package br.edu.seffrin.amq;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


public class DateConverterTImezonesGMT0 {
	
    public static String convertDate(String dateString) {
 
        String inputFormat = "dd/MM/yyyy HH:mm:ss";
        String gmtTimeZoneId = "GMT-00:00"; // GMT-0

        try {
            SimpleDateFormat inputFormatter = new SimpleDateFormat(inputFormat);
            inputFormatter.setTimeZone(TimeZone.getTimeZone(gmtTimeZoneId));
            Date gmtDate = inputFormatter.parse(dateString);

            TimeZone androidTimeZone = TimeZone.getDefault();
            SimpleDateFormat outputFormatter = new SimpleDateFormat(inputFormat);
            outputFormatter.setTimeZone(androidTimeZone);

            String formattedDate = outputFormatter.format(gmtDate);
            return formattedDate;
        } catch (ParseException e) {
            e.printStackTrace();
            return dateString;
        }
    }
    

    public static void main(String[] args) {
        String data = convertDate("22/12/2023 13:56:09");
        System.out.println(data);
    }
}
