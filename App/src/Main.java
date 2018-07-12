import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
	

	/*
	 * large01.txt contains 10000 sets
	 * large02.txt contains 5163 sets
	 * medium.txt contains 100 sets
	 * small.txt contains 40 sets
	 * dataset_20_1000.txt contains 20 sets
	 * p01.txt contains 10 sets
	 * p02.txt contains 5 sets
	 * p03.txt contains 6 sets
	 * p04.txt contains 7 sets
	 * p05.txt contains 8 sets
	 * p08.txt contains 24 sets
	 * set100k.txt contains 100k sets
	 * set200k.txt contains 100k sets
	 * set500k.txt contains 100k sets
	 * set1m.txt contains 1m sets
	 */
	static int [] value=new int[100];
	static int [] weight=new int[100];
	static boolean [] used= new boolean[100];
	static int numUsed=0;
	static ArrayList <Integer>list = new ArrayList<>();
	static String inputFileDir="input sets/";
	static String inputFileName = "medium.txt";
	static String outputFileDir="output result/";
	static String outputFileName="result.txt";

	
	public static void main(String[] args) throws FileNotFoundException
	{
		
	
		int currentWeight=0;
		int totalValue=0;
		double elapsedTime1=0;
		double elapsedTime2=0;
		File in= new File(inputFileDir+inputFileName);
		File out = new File(outputFileDir+outputFileName);
		Scanner s = new Scanner(in);
		PrintWriter p = new PrintWriter(out);
		int maxWeight=s.nextInt();
		
		int n=0;
		while(s.hasNext())//readfrom file
		{		
			value[n]=s.nextInt();
			weight[n]=s.nextInt();
			n++;
		}
		
		//-------------------------------------------------
		// sort smallest.....highest density
		double startTime = System.currentTimeMillis();
		if (n<10000)
			sort1(value,weight);//compelete sort
		else
			sort2(value,weight);//incomplete sort
		double stopTime = System.currentTimeMillis();
		elapsedTime1 = (stopTime - startTime)/1000;
		//------------------------------------------------
		//create initial solution
		for(int i=value.length-1;i>=0;i--)
		{
			
			if(weight[i]+currentWeight<=maxWeight)
			{
				used[i]=true;
				numUsed++;
				list.add(i);
				currentWeight+=weight[i];
				if(currentWeight==maxWeight)
					break;
			}
		}
		//-------------------------------------------------
		//find local best solution 300 iteration
		double startTime2 = System.currentTimeMillis();
		for(int i=0;i<300;i++)
		{
			for(int j=list.size()-1;j>=0;j--)//find better combination of other items to replace for each item
			{
				ArrayList<Integer> sets = new ArrayList<Integer>();
				int weightRange= weight[list.get(j)]+(maxWeight-currentWeight);
				int localWeight=0;
				int localValue=0;
				for(int k=0;k<20;k++)//try to find 20 items not selected randomly
				{
					int random =(int)(Math.random()*value.length);
					
					if(used[random]==true)
						continue;
					if(weight[random]+localWeight<weightRange)
					{
						localWeight+=weight[random];
						localValue+=value[random];
						sets.add(random);
						if(localWeight==weightRange)
							break;
					}
				}
				if(sets.size()!=0&&localValue>=value[list.get(j)])//if items set is better than current item replace it
				{
					int x=0;
					for(;x<sets.size();x++)
					{
						list.add(j+x,sets.get(x));
						used[sets.get(x)]=true;
						numUsed++;
					}
					used[list.get(j+x)]=false;
					numUsed--;
					currentWeight=currentWeight-weight[list.get(j+x)]+localWeight;
					list.remove(j+x);
					
					
				}
			}
			if(numUsed==used.length)//check if we selected all items
				break;
		}
		if(currentWeight!=maxWeight)//extra check 99% of time doesn't even pass the if statement
			for(int i=0;i<weight.length;i++)
				if(used[i]==false&&currentWeight+weight[i]<=maxWeight)
				{
					used[i]=true;
					currentWeight+=weight[i];
					list.add(i);
					if(currentWeight==maxWeight)
						break;
				}
			
		
		double stopTime2 = System.currentTimeMillis();
		elapsedTime2 = (stopTime2 - startTime2)/1000;
		totalValue=0;
		for(int i=0;i<list.size();i++)//print items selected and its value
		{
			System.out.println(weight[list.get(i)]+"		"+value[list.get(i)]);
			totalValue+=value[list.get(i)];
		}
		
		System.out.println("total weight = "+currentWeight);
		p.println("total weight = "+currentWeight);
		System.out.println("total value = "+totalValue);
		p.println("total value = "+totalValue);
		System.out.println("items selected = "+list.size());
		p.println("items selected = "+list.size());
		System.out.println("sort time = "+elapsedTime1);
		p.println("sort time = "+elapsedTime1);
		System.out.println("iterations time = "+elapsedTime2);
		p.println("iterations time = "+elapsedTime2);
		System.out.println("___________________________________");
		p.println("___________________________________");
		s.close();
		p.close();
		
		
	}
	
	public static void sort1(int[] value,int[] weight)//insertion sort pretty bad sort
	{
		int temp1,temp2;
		for (int i = 1; i < value.length; i++) {
            for(int j = i ; j > 0 ; j--){
                if((double)value[j]/weight[j]< (double)value[j-1]/weight[j-1])
                {
                    temp1 = value[j];
                    value[j] = value[j-1];
                    value[j-1] = temp1;
                    temp2 = weight[j];
                    weight[j] = weight[j-1];
                    weight[j-1] = temp2;
                }
            }
        }
		
	}
	public static void sort2(int[] value,int[] weight)
	{/*
	
	*this sort good for arrays with size +2000
	*and won't sort the whole array
	*it contains two parts:
	*first: party put higher density items in the last 1000 index in array
	*second: sort only last 1000 items
	*justification: since we want good initial solution we only need best density
	* so there is no need to sort items with lower density just the highest which we
	* put in last 1000 index
	*/
		int temp1,temp2;
		for(int i=1;i<=1000;i++)//for each item in last 1000 index try to find better density items to replace
		{
			int index=value.length-i;
			for(int j=0;j<500;j++)//try 500 items randomly
			{
				
				int ran = (int)(Math.random()*(value.length-1000));//choosing must exclude last 1000 item
				if(value[ran]/weight[ran]>value[index]/weight[index])
				{
					temp1 = value[ran];
	                value[ran] = value[index];
	                value[index] = temp1;
	                temp2 = weight[ran];
	                weight[ran] = weight[index];
	                weight[index] = temp2;
				}
			}
		}//1000 letter being tested 500 times each --> 500k replacing try
		
		for (int i = value.length-999; i < value.length; i++) {//insertion sort on last 1000 items
            for(int j = i ; j > value.length-1000 ; j--){
                if((double)value[j]/weight[j]< (double)value[j-1]/weight[j-1])
                {
                    temp1 = value[j];
                    value[j] = value[j-1];
                    value[j-1] = temp1;
                    temp2 = weight[j];
                    weight[j] = weight[j-1];
                    weight[j-1] = temp2;
                }
            }
        }
		
	}
	
}
