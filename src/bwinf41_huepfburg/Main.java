package bwinf41_huepfburg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class Main {
	
	static class Node {
	    public int data; //data for storage
	    public List<Node> children;//array will keep children
	    public ArrayList<String> path;
	    public Node parent;

	    public Node(Integer data) {
	        children = new ArrayList<>();
	        this.data = data;
	        ArrayList<String> path = new ArrayList<>();
	        this.path.add(data.toString());
	        
	        //this.parent = parent;
	    }
	    
	    public ArrayList<String> showPath() {
	    	return path;
	    }
	    
	    public Node add(Node node) {
	        children.add(node);
	        node.path.addAll(path);
	        return this;
	    }
	}
	
	
	public static void main(String[] args) {
		
		// Input lesen
		
		Scanner scanner = new Scanner(System.in);
		
		ArrayList<Integer> startPoint = new ArrayList<>();
		ArrayList<Integer> endPoint = new ArrayList<>();
		
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
		
        // Ebene für ebene durchgehen
		
		if (!startPoint.contains(1) || !startPoint.contains(2)) {
			System.out.println("Map doesn't contain a entry point."); 
		} else {
			ArrayList<Integer> index1 = getIndex(startPoint, 1);
			ArrayList<Integer> index2 = getIndex(startPoint, 2);
			
			index1 = getEquivalent(endPoint, index1);
			index2 = getEquivalent(endPoint, index2);

			Map<Integer, Map<Integer, Integer>> parent1 = new HashMap<Integer, Map<Integer, Integer>>();
			Map<Integer, Map<Integer, Integer>> parent2 = new HashMap<Integer, Map<Integer, Integer>>();
			
			Map<Integer, Integer> tmp1 = new HashMap<Integer, Integer>();
			
			for (int i : index1) {
				tmp1.put(i, 1);
				parent1.put(0, tmp1);
			}
			
			Map<Integer, Integer> tmp2 = new HashMap<Integer, Integer>();
			
			for (int i : index2) {
				tmp2.put(i, 2);

				parent2.put(0, tmp2);
			}
			
			calculate(parent1, parent2, startPoint, endPoint, 1);
				
		}
	}
		

	private static boolean calculate(Map<Integer, Map<Integer, Integer>> tree1, Map<Integer, Map<Integer, Integer>> tree2, ArrayList<Integer> startPoint, ArrayList<Integer> endPoint, int index) {

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
			
			if (!return1.isEmpty()) {
				tree1.put(index, return1);
			} else {
				return false;
			}
			
			if (!return2.isEmpty()) {
				tree2.put(index, return2);
			} else {
				return false;
			}
			
			index = index +1;
			
			ArrayList<Integer> l1 = new ArrayList<>(return1.keySet());
			ArrayList<Integer> l2 = new ArrayList<>(return2.keySet());
			
			l1.retainAll(l2);
			
			if(l1.isEmpty()) {
				calculate(tree1, tree2, startPoint, endPoint, index);
			} else {
				System.out.println("Erster Pfad (Sasha):");
				System.out.println(l1.get(0));
				calculatePath(tree1, index, l1.get(0));
				
				System.out.println("\nZweiter Pfad (Mika):");
				System.out.println(l1.get(0));
				calculatePath(tree2, index, l1.get(0));
				
				System.out.printf("\nBeide landen auf gleichzeitig nach %d Sprüngen auf Feld %d.", index, l1.get(0));
			}
					
			return false;
	}

	private static void calculatePath(Map<Integer, Map<Integer, Integer>> tree, int index, int child) {
		
		Map<Integer, Integer> level1 = tree.get(index-1);
							
		child = level1.get(child);
			
		System.out.println(child);
			
		if(child == 1 || child == 2) {
			return;
		} else {
			calculatePath(tree, index-1, child);
		}
		
	}


	private static ArrayList<Integer> getEquivalent(ArrayList<Integer> array, ArrayList<Integer> i) {
		ArrayList<Integer> result = new ArrayList<>();
		for (int x = 0 ; x < i.size() ; x++) {
			result.add(array.get(i.get(x)));
		}
		return result;
	}

	private static ArrayList<Integer> getIndex(ArrayList<Integer> array, int i) {
		ArrayList<Integer> result = new ArrayList<>();
		while (array.indexOf(i) != -1) {
			int x = array.indexOf(i);
			result.add(x);
			array.set(x, -1);
		}
		return result;
	}

}