package io;

import java.util.ArrayList;
import java.util.Random;

public class PrintTables {
	private final String BOTTOMCROSS = "'";
	private final String BOTTOMHORIZONTAL = "-";
	private final String CROSSING = "|";
	private final String HORIZONTAL = "-";
	private final String LEFTCROSS = "|";
	private final String LEFTVERTICAL = "|";
	private final String RIGHTCROSS = "|";
	private final String RIGHTVERTICAL = "|";
	private final String TOPCROSS = ".";
	private final String TOPHORIZONTAL = "-";
	private final String VERTICAL = "|";

	public static void main(String[] args) {
		new PrintTables().printExample();
	}

	/**
	 * Multiplies a String by a number, just like Ruby does it!
	 * 
	 * @param string
	 * @param number
	 * @return
	 */
	public String mulString(String string, int number) {
		StringBuffer result = new StringBuffer();

		for(int i = 0; i < number; i++) {
			result.append(string);
		}

		return result.toString();
	}

	/**
	 * Formats and prints a table
	 * 
	 * @param table
	 *            2d array of strings
	 */
	public void print(ArrayList<ArrayList<String>> table) {
		int rows = table.size();

		int columns = 0;
		for(ArrayList<String> line : table) {
			columns = Math.max(columns, line.size());
		}

		int[] columnWidth = new int[columns];

		for(ArrayList<String> line : table) {
			for(int i = 0; i < line.size(); i++) {
				columnWidth[i] = Math.max(columnWidth[i], line.get(i).length());
			}
		}

		// print top of table
		System.out.print(".");
		for(int i = 0; i < columns; i++) {
			System.out.print(mulString(HORIZONTAL, columnWidth[i]) + TOPCROSS);
		}
		System.out.println();

		// print first row as special case
		System.out.print(VERTICAL);
		for(int cell = 0; cell < table.get(0).size(); cell++) {
			System.out.print(rjust(table.get(0).get(cell), columnWidth[cell]));
			System.out.print(CROSSING);
		}
		System.out.println();

		// print following rows
		for(int row = 1; row < rows; row++) {
			// first the separator line
			System.out.print(LEFTCROSS);
			for(int cell = 0; cell < table.get(row).size(); cell++) {
				System.out.print(mulString(HORIZONTAL, columnWidth[cell]));
				System.out.print(CROSSING);
			}

			System.out.println();

			// now the content
			System.out.print(LEFTVERTICAL);
			for(int cell = 0; cell < table.get(row).size(); cell++) {
				System.out.print(rjust(table.get(row).get(cell), columnWidth[cell]));
				System.out.print(VERTICAL);
			}
			System.out.println();
		}

		// print bottom of table
		System.out.print(BOTTOMCROSS);
		for(int i = 0; i < columns; i++) {
			System.out.print(mulString(HORIZONTAL, columnWidth[i]) + BOTTOMCROSS);
		}
		System.out.println();
	}

	/**
	 * Little test method that prints a table filled with some random numbers
	 */
	public void printExample() {
		// first, generate example
		Random rand = new Random();

		String[] exampleTableHeader = { "Numbers", "More Numbers", "Numbing Numbers", "Baby Names", "Letters" };

		int rows = 10;
		int columns = exampleTableHeader.length;

		String[][] content = new String[columns][rows];

		content[0] = exampleTableHeader;
		for(int i = 1; i < content.length; i++) {
			String[] line = new String[columns];
			for(int j = 0; j < line.length; j++) {
				line[j] = "" + rand.nextInt(1000);
			}
			content[i] = line;
		}

		// now print it
		// print(content);
	}

	/**
	 * Stolen from Ruby. leftjustifies a string;
	 * 
	 * @param string
	 * @param width
	 * @return
	 */
	public String rjust(String string, int width) {
		return rjust(string, width, " ");
	}

	public String rjust(String string, int width, String padding) {
		int paddingLength = width - string.length();
		if(paddingLength <= 0) {
			return string;
		}
		return mulString(padding, width - string.length()).substring(0, paddingLength) + string;
	}
}
