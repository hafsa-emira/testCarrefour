package lansrod.com.carrefour;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CreateDate {
	private  Date day = new Date();

	public  String[] createDates() {
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		String[] dates = new String[7];
		for (int i = 0; i < 7; i++) {
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DATE, i);
			day = c.getTime();
			dates[i] = dateFormat.format(day);
		}
		return dates;
	}
	
	public String createDateStime() {
		DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		Date date = new Date();
		String result = df1.format(date);
		return result;


	}
}
