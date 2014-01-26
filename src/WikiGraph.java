import java.util.Scanner;

import javax.swing.SwingUtilities;

import wiki.gui.MainWindow;
import wiki.tests.Tests;


public class WikiGraph {
	private static Scanner input;

	static void makeTests() {
		input = new Scanner(System.in);
		Tests test = new Tests();
		System.out.println("0.Graph\n1.Heap");
		int choice = input.nextInt();
		if(choice == 0)
			test.Graph();
		else if(choice == 1)
			test.Heap();
	}

	public static void main(String[] Args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new MainWindow();
			}
		});
	}
}
