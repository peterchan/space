import java.text.MessageFormat;
import java.util.Calendar;

/**
 * <pre>
 * desc: 
 * created: Mar 27, 2013 9:30:44 AM
 * author: xiangfeng
 * todo: 
 * history:
 * </pre>
 */
public class Test {

	public static void main(String[] args) {
		String s = "172.23.173.51,172.23.174.107,10.246.146.103,10.246.146.104,10.246.146.133,10.246.146.151,10.246.146.163,10.246.146.94";

		System.out.println(s.contains("10.246.146.103"));

		String sss = "123123%!%12312%!%12312%!%12312%!%12312";

		String[] split = sss.split("%!%");

		for (String ss1 : split) {
			System.out.println(ss1);
		}

		System.out.println(split.length);

		System.out.println(MessageFormat.format(
				"/group/taobao/taobao/hive/s_file/pt={0,date,yyyyMMdd0000}/*",
				Calendar.getInstance().getTime()));

	}
}
