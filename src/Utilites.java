import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;


public class Utilites {
	public static Random rand = new Random();
	
	//Difference between vectors, [0,1]. 1 = completely equal
	public static double CompareOutputs(List<Boolean> originalOutput, List<Boolean> currentOutput)
	{
		if (originalOutput.size() != currentOutput.size())
			return 0;
		
		int different = 0;
		for (int i = 0; i < originalOutput.size(); i++)
		{
			if (originalOutput.get(i) != currentOutput.get(i))
				different++;
		}
		
		return (double)(originalOutput.size() - different) / (double)originalOutput.size();
		
	}
	public static ArrayList<Boolean> GetRandomBooleanInput(int size)
	{
		ArrayList<Boolean> randomInput = new ArrayList<Boolean>();
		for (int i = 0; i < size; i++)
		{
			randomInput.add(RandomBoolean());
		}
		return randomInput;
	}
	
	public static Boolean RandomBoolean()
	{
		return rand.nextBoolean();
	}

	public static int GetRandom(int max, int min)
	{
		if (max < min)
			return min;
	    int randomNum = rand.nextInt((max - min) + 1) + min;
	    return randomNum;
	}
	
	public static int GetRandom(double max, double min)
	{
	    double randomNum = min + (max - min) * rand.nextDouble();
	    return (int)randomNum;
	}
	
	public static double GetRandomProbability()
	{
		return rand.nextDouble();
	}
	
	
	public static Boolean[][] GenerateInputs()
	{
		Boolean[][] inputs = new Boolean[Defines.NUMBER_OF_TESTING_INPUTS][];
		for (int i = 0; i < Defines.NUMBER_OF_TESTING_INPUTS; i++)
		{
			inputs[i] = new Boolean[Defines.NETWORK_INPUT_SIZE];
			for (int j = 0; j < Defines.NETWORK_INPUT_SIZE; j++)
			{
				inputs[i][j] = rand.nextBoolean();
			}
		}
		return inputs;
	}
	
	public static boolean CompareIntsLists(List<Integer> listOne, List<Integer> listTwo)
	{
		if (listOne.size() != listTwo.size())
			return false;
		
		for (int i = 0; i < listOne.size(); i++)
		{
			if (listOne.get(i) != listTwo.get(i))
				return false;
		}
		return true;
	}
}
