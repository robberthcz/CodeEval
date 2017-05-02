package uglyNumbers;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Main {

	private int[] digits;
	private byte[] ops;
	private long uglies;

	public Main(int[] digits) {

		this.digits = digits;

		uglies = 0;

		ops = new byte[digits.length];
		ops[0] = 1;

		// System.out.println(sumDigits(0));

		explore();

		System.out.println(uglies);
	}

	private void explore() {

		if (digits.length == 1) {
			boolean ugly = isUgly(digits[0]);
			if (ugly)
				uglies++;
			return;

		}

		explore(1, (byte) -1);
		explore(1, (byte) 0);
		explore(1, (byte) 1);
	}

	private void explore(int index, byte op) {

		ops[index] = op;

		if (index == digits.length - 1) {
			long n = sumDigits();
			// System.out.println(n);
			if (isUgly(n))
				uglies++;
			return;
		}

		explore(index + 1, (byte) -1);
		explore(index + 1, (byte) 0);
		explore(index + 1, (byte) 1);

	}

	private long sumDigits() {

		StringBuffer sb = new StringBuffer(13);
		sb.append(digits[0]);

		for (int i = 1; i < digits.length; i++) {

			if (ops[i] == -1)
				sb.append('-');
			else if (ops[i] == 1)
				sb.append('+');

			sb.append(digits[i]);

		}

		return strToNum(sb.toString());

	}

	private long strToNum(String n) {

		long result = 0;

		while (n.length() > 0) {

			int plus = n.lastIndexOf('+');
			int minus = n.lastIndexOf('-');

			if (plus == -1 && minus == -1) {
				result += Long.parseLong(n);
				break;
			} else if (plus == -1 || (minus != -1 && minus > plus)) {

				result -= Long.parseLong(n.substring(minus + 1, n.length()));

				n = n.substring(0, minus);

			} else {

				result += Long.parseLong(n.substring(plus + 1, n.length()));
				n = n.substring(0, plus);
			}
		}
		return result;
	}


	private boolean isUgly(long n) {

		return n == 0 || n % 2 == 0 || n % 3 == 0 || n % 5 == 0 || n % 7 == 0;

	}

	public static void main(String[] args) throws FileNotFoundException {

		Scanner textScan = new Scanner(new FileReader(
				"test-cases/uglyNumbers.txt"));

		while (textScan.hasNextLine()) {

			String line = textScan.nextLine();

			// System.out.println(line);

			int[] digits = new int[line.length()];

			for (int i = 0; i < line.length(); i++) {

				char c = line.charAt(i);

				digits[i] = Integer.parseInt(String.valueOf(c));

			}

			Main test = new Main(digits);

		}
	}

}
