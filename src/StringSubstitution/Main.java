package StringSubstitution;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Main {

	private boolean[] marked;
	private String[] string;

	public Main(String[] string) {
		this.string = string;
		marked = new boolean[string.length];

	}

	public void substitute(String toReplace, String replaceWith) {

		for (int i = 0; i < string.length; i++) {

			if (marked[i])
				continue;

			int j = Integer.MIN_VALUE;

			for (j = i; j - i < toReplace.length() && j < string.length; j++) {

				String replacing = String.valueOf(toReplace.charAt(j - i));

				if (marked[j] || !string[j].equals(replacing)) {
					j = i;
					break;
				}

			}

			if (j - i == toReplace.length()) {

				replace(replaceWith, i, j);

			}

		}
	}

	private void replace(String replaceWith, int start, int end) {

		string[start] = replaceWith;
		marked[start] = true;

		for (int i = start + 1; i < end; i++) {
			string[i] = "-";
			marked[i] = true;
		}

	}

	@Override
	public String toString() {

		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < string.length; i++) {

			if (string[i].equals("-"))
				continue;

			sb.append(string[i]);
		}

		return sb.toString();

	}

	public static void main(String[] args) throws FileNotFoundException {

		Scanner textScan = new Scanner(new FileReader(
				"test-cases/stringSubstitution.txt"));

		while (textScan.hasNextLine()) {

			String[] line = textScan.nextLine().split(";");

			Main test = new Main(line[0].split(""));

			String[] substitutions = line[1].split(",");

			for (int i = 0; i < substitutions.length; i += 2) {

				String toReplace = substitutions[i];
				String replaceWith = substitutions[i + 1];

				test.substitute(toReplace, replaceWith);

			}

			System.out.println(test);

		}

	}
}
