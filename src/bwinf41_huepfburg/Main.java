package bwinf41_huepfburg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/*
 * BWINF-41 "Huepfburg"
 * Developer: Daniel Orkin
 * Team-ID: 00376
 */


public class Main {
	
	public static void main(String[] args) {
		
		// Input lesen
		
		System.out.println("Geben Sie die Daten des Parcours ein und bestätigen Sie mit der Eingabe-Taste.");
		
		Scanner scanner = new Scanner(System.in);
		
		ArrayList<Integer> startPoint = new ArrayList<>();
		ArrayList<Integer> endPoint = new ArrayList<>();
		
		try {
			
			scanner.nextInt();
			int stop = scanner.nextInt();
			
			for (int i = 0 ; i < stop ; i++) {
				try {
					startPoint.add(scanner.nextInt());
					endPoint.add(scanner.nextInt());
				} catch (Exception e) {
					break;
				}
			}
		} catch (Exception e) {
			System.out.println("Invalid input.");
			return;
		}
        // Ebene für ebene durchgehen
		
		if (!startPoint.contains(1) || !startPoint.contains(2)) {
			System.out.println("Map doesn't contain a entry point."); 
		} else {
			
			// Map mit der ersten Ebene an Werten vorbereiten
			
			ArrayList<Integer> index1 = getIndex(startPoint, 1);
			ArrayList<Integer> index2 = getIndex(startPoint, 2);
			
			index1 = getEquivalent(endPoint, index1);
			index2 = getEquivalent(endPoint, index2);

			Map<Integer, Map<Integer, Integer>> tree1 = new HashMap<Integer, Map<Integer, Integer>>();
			Map<Integer, Map<Integer, Integer>> tree2 = new HashMap<Integer, Map<Integer, Integer>>();
			
			Map<Integer, Integer> tmp1 = new HashMap<Integer, Integer>();
			
			for (int i : index1) {
				tmp1.put(i, 1);
				tree1.put(0, tmp1);
			}
			
			Map<Integer, Integer> tmp2 = new HashMap<Integer, Integer>();
			
			for (int i : index2) {
				tmp2.put(i, 2);

				tree2.put(0, tmp2);
			}
			
			calculate(tree1, tree2, startPoint, endPoint, 1);
				
		}
	}
		

	private static void calculate(Map<Integer, Map<Integer, Integer>> tree1, Map<Integer, Map<Integer, Integer>> tree2, ArrayList<Integer> startPoint, ArrayList<Integer> endPoint, int index) {
			try {
				Map<Integer, Integer> level1 = tree1.get(index-1);
				Map<Integer, Integer> level2 = tree2.get(index-1);
				
				Map<Integer, Integer> return1 = new HashMap<Integer, Integer>();
				Map<Integer, Integer> return2 = new HashMap<Integer, Integer>();
	
				for (var i : level1.entrySet()) {
					ArrayList<Integer> tmp = getIndex(startPoint, i.getKey());
					tmp = getEquivalent(endPoint, tmp);
					
					for (int h : tmp) {
						return1.put(h, i.getKey());
					}
				}
				
				for (var i : level2.entrySet()) {
					ArrayList<Integer> tmp = getIndex(startPoint, i.getKey());
					tmp = getEquivalent(endPoint, tmp);
					
					for (int h : tmp) {
						return2.put(h, i.getKey());
					}
				}
				
				// Neue "Kinder" an kompletten Baum anbinden
				
				if (!return1.isEmpty()) {
					tree1.put(index, return1);
				} 
				
				if (!return2.isEmpty()) {
					tree2.put(index, return2);
				} 
				
				index = index +1;
				
				// Zwei Listen auf einen gleichen Wert überprüfen
				
				ArrayList<Integer> l1 = new ArrayList<>(return1.keySet());
				ArrayList<Integer> l2 = new ArrayList<>(return2.keySet());
				
				l1.retainAll(l2);
				
				if(l1.isEmpty()) {
					calculate(tree1, tree2, startPoint, endPoint, index);
				} else {
					System.out.println("Erster Pfad (Sasha):");
					System.out.print(1 + ", ");
					ArrayList<Integer> path1 = calculatePath(tree1, index, l1.get(0), new ArrayList<Integer>());
					Collections.reverse(path1);
					path1.forEach(i -> {
						System.out.print(i + ", ");
					});
					System.out.print(l1.get(0));
					
					System.out.println("\nZweiter Pfad (Mika):");
					System.out.print(2 + ", ");
					ArrayList<Integer> path2 = calculatePath(tree2, index, l1.get(0), new ArrayList<Integer>());
					Collections.reverse(path2);
					path2.forEach(i -> {
						System.out.print(i + ", ");
					});
					System.out.println(l1.get(0));
					
					System.out.printf("\nBeide landen auf gleichzeitig nach %d Sprüngen auf Feld %d.", index, l1.get(0));
				}
						
			} catch (StackOverflowError e) {
				System.out.println("Mika und Sasha treffen sich niemals.");
			}
	}

	private static ArrayList<Integer> calculatePath(Map<Integer, Map<Integer, Integer>> tree, int index, int child, ArrayList<Integer> process) {
		
		if (index != 1) {
			Map<Integer, Integer> level1 = tree.get(index-1);
			
			child = level1.get(child);
				
			process.add(child);

			calculatePath(tree, index-1, child, process);	
		}
		return process;
	}


	private static ArrayList<Integer> getEquivalent(ArrayList<Integer> array, ArrayList<Integer> i) {
		
		ArrayList<Integer> result = new ArrayList<>();
		
		for (int x = 0 ; x < i.size() ; x++) {
			result.add(array.get(i.get(x)));
		}
		
		return result;
	}

	private static ArrayList<Integer> getIndex(ArrayList<Integer> array, int i) {
		
		ArrayList<Integer> copy = new ArrayList<>(array);
		ArrayList<Integer> result = new ArrayList<>();
		
		while (copy.indexOf(i) != -1) {
			int x = copy.indexOf(i);
			result.add(x);
			copy.set(x, -1);
		}
		
		
		return result;
	}

}