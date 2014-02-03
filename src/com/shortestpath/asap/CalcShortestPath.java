package com.shortestpath.asap;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import main.Dijkstra;



public class CalcShortestPath {

	ArrayList<Integer> al = new ArrayList<Integer>();
	HashMap<Integer, ArrayList<Integer>> vertices = new HashMap<Integer, ArrayList<Integer>>();
	int s ;
	int t;
	HashMap<Integer, Integer> psptS = new HashMap<Integer, Integer>();
	HashMap<Integer, Integer> psptT = new HashMap<Integer, Integer>();
	long startTime ;
	int size_num = 0;
	


	public CalcShortestPath() {
		// TODO Auto-generated constructor stub
	}


	public static void main(String[] args)
	{
		CalcShortestPath csp = new CalcShortestPath();
		csp.calculate(args);
	}
	public void calculate(String[] args)
	{
		BufferedReader br = null;
		Scanner stdin = null;
		try{
			do{
			System.out.print("Enter source ");
			stdin = new Scanner(System.in); // Keyboard input       
			String source_id = stdin.nextLine();
			System.out.println(source_id);
			if(Integer.parseInt(source_id)==0)
				break;
			//stdin.close();
			System.out.print("Enter Destination ");
			stdin = new Scanner(System.in); // Keyboard input       
			String destination_id = stdin.nextLine();
			//stdin.close();
			System.out.print("Enter Partial tree size (for testing purpose) ");
			stdin = new Scanner(System.in); // Keyboard input       
			String size = stdin.nextLine();
			//stdin.close();
	
		int i=1;
			br = new BufferedReader(new InputStreamReader(new FileInputStream("graph.txt")));

			String line = null;
			int j = 0 ;
			int k = 0;
			while((line = br.readLine()) != null) {
				
				if(!line.equals(""))
				{
					String[] split = line.split("	");
					j = Integer.parseInt(split[0]);
					k = Integer.parseInt(split[1]);
					if(i!=j)
					{
						vertices.put(i, al);
						System.out.println("Key "+i+"    " +vertices.size()+" vertices, "+al.size()+" neighbors ");
						i=j;
						al = new ArrayList<Integer>();
					}
					al.add(k);
				}else{
					i=j;
					vertices.put(j, al);
					System.out.println("Key "+i+"    " +vertices.size()+" vertices, "+al.size()+" neighbors ");
				}
			}

			//calculate the partial shortest path tree

			s = Integer.parseInt(source_id);
			t = Integer.parseInt(destination_id);
			psptS = new HashMap<Integer, Integer>();
			psptT = new HashMap<Integer, Integer>();
			size_num = Integer.parseInt(size);
			//int tempS = s;
			//int tempT = t;
			if (vertices.containsKey(s) && vertices.containsKey(t))
			{
				Thread source = new Thread() {
					public void run() {
						/*try {*/

						System.out.println("===============Partial Shortest Path for Source node is STARTING =====================");
						for (int i=0;i<size_num;i++)
						{
							ArrayList<Integer> alTemp = get(s,i);
							//Thread.sleep(1000);
							for (int j=0;j<alTemp.size();j++)
							{
								if(!psptS.containsKey(alTemp.get(j)) && alTemp.get(j)!=s)
								{
									psptS.put(alTemp.get(j), i+1);
									//System.out.println(psptS.get(alTemp.get(j)));
								}

							}
							//System.out.println("Source "+i+"============================================");
							//psptS.put(vertices.get(s), value)
							//temp = psptS.get(key)
						}
						System.out.println("===============Partial Shortest Path for Source node is CALCULATED =====================");
						/* } catch(InterruptedException v) {
				            System.out.println(v);
				        }*/
					}  
				};

				source.start();

				Thread destination = new Thread() {
					public void run() {
						/*try {*/

						System.out.println("===============Partial Shortest Path for Destination node is STARTING =====================");
						for (int i=0;i<size_num;i++)
						{
							ArrayList<Integer> alTemp = get(t,i);
							//Thread.sleep(1000);
							for (int j=0;j<alTemp.size();j++)
							{
								if(!psptT.containsKey(alTemp.get(j)) && alTemp.get(j)!=t)
								{
									psptT.put(alTemp.get(j), i+1);
									//System.out.println(psptT.get(alTemp.get(j)));
								}

							}
							//System.out.println("destination"+i+"============================================");
							//psptS.put(vertices.get(s), value)
							//temp = psptS.get(key)
						}
						System.out.println("===============Partial Shortest Path for Destination node is CALCULATED =====================");

						/*} catch(InterruptedException v) {
				            System.out.println(v);
				        }*/
					}  
				};

				destination.start();
				source.join();
				destination.join();
				calculateShortestPath(8);
			}else {
				System.out.println("Source or destination does not exist");
			}

		}while(true);
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			System.out.println("=================Program exited====================");
			if (stdin != null)
			stdin.close();
			try {
				if (br != null)
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void calculateShortestPath(Integer temp)
	{
		startTime = System.nanoTime();
		HashMap<Integer,  Integer> shortestPaths = new HashMap<Integer, Integer>();
		int least = -1;
		int leastKey = -1;
		//System.out.println(temp+"========================================");
		/*for (Map.Entry<Integer,Integer> entry : psptS.entrySet()) {
			Integer key = entry.getKey();
			Integer value = entry.getValue();

			System.out.println("# source   key = "+key+"    value = "+value+"         #");
			// do stuff						  Final Shortest Path/paths from "+s+" to "+t+" =====================
		}
		for (Map.Entry<Integer,Integer> entry : psptT.entrySet()) {
			Integer key = entry.getKey();
			Integer value = entry.getValue();

			System.out.println("#  destination  key = "+key+"    value = "+value+"         #");
			// do stuff						  Final Shortest Path/paths from "+s+" to "+t+" =====================
		}*/
		for (Map.Entry<Integer,Integer> entry : psptS.entrySet()) {
			Integer key = entry.getKey();
			Integer value = entry.getValue();
			if(psptT.containsKey(key))
			{
				if (least == -1 || least > value + psptT.get(key))
				{
					least = value + psptT.get(key);
					leastKey = key;
					shortestPaths.clear();
					shortestPaths.put(key, least);
				}else{
					if (least == value + psptT.get(key))
					{

						shortestPaths.put(key, least);
					}					  
				}
			}
			//System.out.println("Distance from source "+ s+" to node "+key+" = "+value);
			// do stuff
		}
		long stopTime = System.nanoTime();
	      long elapsedTime = stopTime - startTime;
	      System.out.println(stopTime+ " "+startTime);
		System.out.println(); 
		System.out.println();
		System.out.println("=============== Final Shortest Path/paths from "+s+" to "+t+" elapsed time "+elapsedTime+" nano seconds=========");
		for (Map.Entry<Integer,Integer> entry : shortestPaths.entrySet()) {
			Integer key = entry.getKey();
			Integer value = entry.getValue();

			System.out.println("#               Path through the node "+((key<10)?"0"+key:key)+", with distance  "+value+"                 #");
			// do stuff						  Final Shortest Path/paths from "+s+" to "+t+" =====================
		}
		System.out.println("=============== Final Shortest Path/paths from "+s+" to "+t+" elapsed time "+elapsedTime+" nano seconds=========");
		
		if (shortestPaths.isEmpty())
		{
			System.out.println("=============== calling Bi directional algorithm ===============");
			try {
				//System.out.println("d");
				Dijkstra.main(new String[] { "weighted_mst.sp", "-s", ""+s, "-t", ""+t, "-h", "-b" });
			} catch(Exception e) {
				e.printStackTrace();
				//fail();
			}
		}

	}

	public ArrayList<Integer> get(int key, int index)
	{
		ArrayList<Integer> al = null;
		if (index == 0){
			al = vertices.get(key);
			return al;
		}
		else
		{
			al = new ArrayList<Integer>();
			for (int i = 0; i < vertices.get(key).size();i++)
			{
				/*System.out.println(key);
				System.out.println(vertices.get(key));
				System.out.println(vertices.get(key).get(i));*/
				ArrayList<Integer> al1 = get(vertices.get(key).get(i), index-1);
				/*System.out.println(al1);*/
				al.addAll(al1);
			}
			return al;
		}
	}

}
