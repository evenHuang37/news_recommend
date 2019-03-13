package conn;



import java.text.SimpleDateFormat;

public class CDate extends java.util.Date {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static CDate instance;
	public static int j = 0;
	public static int p = 0;

	public CDate() {

	}

	

	public static synchronized  String fileName() {
		SimpleDateFormat sdf = null;
		if (instance != null) {
			// System.out.println("Getting existing instance of connection pool
			// ");
			sdf = new SimpleDateFormat("yyyyMMddHHmm");
		} else {
			instance = new CDate();
			// System.out
			// .println("Getting not existing instance of connection pool ");
			sdf = new SimpleDateFormat("yyyyMMddHHmm");

		}
		java.util.Date date = new java.util.Date();
		String str = sdf.format(date);
		return str + (CDate.j++);
	}


	public static void main(String[] args) {
		System.out.println(CDate.fileName());
		System.out.println(CDate.fileName());
	}

}
