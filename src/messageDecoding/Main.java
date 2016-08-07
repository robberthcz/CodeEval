package messageDecoding;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Scanner;

public class Main {

	private HashMap<String, Character> map;

	private String padZeros(String binary, int len) {

		while (binary.length() < len) {

			binary = "0" + binary;
		}

		return binary;
	}

	public Main(String header, String message) {

		map = new HashMap<String, Character>();

		int id = 0;

		for (int i = 1; i <= 7 && id < header.length(); i++) {

			int max = (1 << i) - 1;

			for (int j = 0; j < max && id < header.length(); j++) {

				// String format = "%0" + Integer.toString(i) + "d";

				// String key = String.format(format,
				// Integer.toBinaryString(j));

				String key = padZeros(Integer.toBinaryString(j), i);
				map.put(key, header.charAt(id++));

				// System.out.println(key);

			}
		}

		// String decoded = read1(Integer.parseInt(message.substring(0, 3), 2),
		// message.substring(3, message.length()));
		//
		// System.out.println(decoded);

		StringBuffer sb = new StringBuffer();
		read2(Integer.parseInt(message.substring(0, 3), 2),
				message.substring(3, message.length()), sb);

		System.out.println(sb);

	}

	public String read1(int nBits, String stream) {

		String num = stream.substring(0, nBits);
		stream = stream.substring(nBits, stream.length());

		if (Integer.parseInt(num, 2) == ((1 << nBits) - 1)) {

			int segmentLen = Integer.parseInt(stream.substring(0, 3), 2);
			stream = stream.substring(3, stream.length());

			if (segmentLen == 0)
				return "";

			else
				return read1(segmentLen, stream);

		} else
			return map.get(num) + read1(nBits, stream);

	}

	public void read2(int nBits, String stream, StringBuffer sb) {

		String num = stream.substring(0, nBits);
		stream = stream.substring(nBits, stream.length());

		if (Integer.parseInt(num, 2) == ((1 << nBits) - 1)) {

			int segmentLen = Integer.parseInt(stream.substring(0, 3), 2);
			stream = stream.substring(3, stream.length());

			if (segmentLen == 0)
				return;

			else
				read2(segmentLen, stream, sb);

		} else {
			sb.append(map.get(num));
			read2(nBits, stream, sb);
		}

	}

	public static void main(String[] args) throws FileNotFoundException {

		Scanner textScan = new Scanner(new FileReader(
				"test-cases/messageDecoding.txt"));

		while (textScan.hasNextLine()) {

			String line = textScan.nextLine();

			int id1 = line.indexOf("0");
			int id2 = line.indexOf("1");
			id1 = Math.min(id1, id2);

			String header = line.substring(0, id1);
			String message = line.substring(id1, line.length());

			Main test = new Main(header, message);

		}

	}
}
