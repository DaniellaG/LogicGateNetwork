import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;


public class TestNetwork implements Runnable{
	private Boolean[][] m_allInputs;
	private List<ArrayList<Boolean>> m_originalOutputs;
	private List<ArrayList<Boolean>> m_currentOutputs;
	private LogicGatesNetwork m_mutatedNetwork;
	private LogicGatesNetwork m_originalNetwork;
	public PrintWriter writer;
	public Semaphore m_mutex;
	private double m_trainCompareRate;
	private int m_originalSize;
	
	public TestNetwork (PrintWriter the_writer, Semaphore writer_mutex, Boolean[][] inputsToTest)
	{
		writer = the_writer;
		m_trainCompareRate = 0;
		m_mutex = writer_mutex;

		m_allInputs = inputsToTest;
		m_originalOutputs = new ArrayList<ArrayList<Boolean>>();
		m_currentOutputs = new ArrayList<ArrayList<Boolean>>();

		m_mutatedNetwork = new LogicGatesNetwork(Defines.NETWORK_INPUT_SIZE, Defines.NETWORK_OUTPUT_SIZE);
		m_originalNetwork = new LogicGatesNetwork(m_mutatedNetwork);
		m_originalNetwork.printNetwork();
		
		System.out.printf("Are networks eqal?%b\n", m_mutatedNetwork.CompareNetworks(m_originalNetwork));
		m_originalSize = m_mutatedNetwork.GetSize();
		
		CalculateOriginalOutputs();
	}
	
	public void run()
	{
		try{
			m_mutatedNetwork.printNetwork();
			System.out.format("Number of of layers:%d", m_mutatedNetwork.GetNumberOfLayers());
			for (int i = 0; i < Defines.TRAIN_NUMBER_OF_DAMAGES; i++)
			{
				DamageNetwork();
				RecoverNetwork();
			}
			m_mutatedNetwork.printNetwork();
			RecopyOriginalOutput();
			m_trainCompareRate = GetAvrgCompareRateOfWorkingNetwork();
			System.out.format("Train: The compare rate now is:%f", m_trainCompareRate);
			
			DamageBothNetworks();
		}
		catch (Exception e)
		{
			System.out.println("EXception!");
			System.out.println(e);
			
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			System.out.println( errors.toString());
			
			Logger.write("Exception:" + e.toString());
			Logger.write(errors.toString());
			m_mutatedNetwork.printNetwork();
		}

	}
	
	private void RecopyOriginalOutput()
	{
		m_currentOutputs.clear();
		for (int i = 0; i < Defines.NUMBER_OF_TESTING_INPUTS; i++)
		{
			m_currentOutputs.add(m_originalOutputs.get(i));
		}
		
	}
	
	private void SendToWriter(double mutated, double original) 
	{
		//take mutex!
		try {
			m_mutex.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.out.format("Couldn't write to file!:%f,%f,%f\n", m_trainCompareRate, mutated, original);
			return;
		}
		writer.print(String.valueOf(m_trainCompareRate) + ",");
		writer.print(String.valueOf(mutated) + ",");
		writer.println(String.valueOf(original)+ "," + String.valueOf(m_originalSize) + "," + String.valueOf(m_mutatedNetwork.GetSize()));
		m_mutex.release();
		//release mutex!
	}
	
	private void changeOriginalInput()
	{
		m_allInputs = Utilites.GenerateInputs();
	}
	
	private void printInput()
	{
		for (int i = 0; i < Defines.NUMBER_OF_TESTING_INPUTS; i++)
		{
			for (int j = 0; j < Defines.NETWORK_INPUT_SIZE; j++)
			{
				System.out.printf("%b,", m_allInputs[i][j]);
			}
			System.out.println("\n");
		}
	}
	
	
	private void DamageBothNetworks()
	{
		changeOriginalInput();
		CalculateOriginalOutputs();
		for (int i = 0; i < Defines.TEST_NUMBER_OF_DAMAGES; i++)
		{
			DamageNetwork();
			DamageOriginalNetwork();
		}
		double mutatedCompareRate = GetAvrgCompareRateOfWorkingNetwork();
		double originalCompareRate = GetAvrgCompareRateOfOriginalNetwork();
		System.out.format("Test: The compare rate of the mutation network is:%f\n", mutatedCompareRate);
		System.out.format("Test: The compare rate of the original network is:%f\n", originalCompareRate);
		SendToWriter(mutatedCompareRate, originalCompareRate);

	}
	
	public void DamageOriginalNetwork()
	{
		m_originalNetwork.damageNetwork();
	}
	
	public void DamageNetwork()
	{
		m_mutatedNetwork.damageNetwork();
	}
	
	public Boolean DecideIfKeepChange(double prevCompare, double currentCompare)
	{
		if (currentCompare > prevCompare)
		{
			//Definitely better
			return true;
		}
		
		if (currentCompare == prevCompare)
		{
			//Same compare rate, not saving changes
			return false;
		}
		
		if (Defines.USE_METROPOLIS)
		{
			//we are using  Metropolis–Hastings algorithm, a Monte Carlo method to decide if we want to keep the change or undo it
			double delta = currentCompare - prevCompare;
			delta = delta * Defines.METROPOLIS_NORMALIZATION_FACTOR;
			double rand = Utilites.GetRandomProbability();
			if (rand <= Math.exp(delta))
			{
				//System.out.println("Not better, but great probability:" + String.valueOf(delta) + " " + String.valueOf(rand));
				return true;
			}
		}
		
		return false;
	}
	
	
	public void RecoverNetwork()
	{
		double prevCompare = GetAvrgCompareRateOfWorkingNetwork();
		int savedRecovers = 0;
		int j = 0;
		double currentCompare = prevCompare;
		
		for (j = 0; currentCompare < Defines.RECOVER_RECENTAGE_WANTED && j < Defines.NUMBER_OF_RECOVER_TRIES; j++)
		{
			m_mutatedNetwork.recoverNetwork();
			currentCompare = GetAvrgCompareRateOfWorkingNetwork();
			
			if (DecideIfKeepChange(prevCompare, currentCompare))
			{
				//keep this change
				savedRecovers++;
				prevCompare = currentCompare;
			}
			else
			{
				m_mutatedNetwork.UndoRecover();
			}
		}
		
		System.out.format("Total number of saved recovers in this damage loop:%d, number of recover tries in this damage loop: %d, recover rate:%f\n ",savedRecovers, j, currentCompare);
		
		//Change logic flow! compare each generation to the previous generation only! in train mode!
		if (Defines.Compare_TO_PREVIOUS_GEN)
		{
			m_currentOutputs.clear();
			for (int i = 0; i < Defines.NUMBER_OF_TESTING_INPUTS; i++)
			{
				ArrayList<Boolean> output = m_mutatedNetwork.runNetwork(m_allInputs[i]);
				m_currentOutputs.add(output);
			}
		}
	}
	
	private double GetAvrgCompareRateOfWorkingNetwork()
	{
		return GetAvrgCompareRate(false);
	}
	
	private double GetAvrgCompareRateOfOriginalNetwork()
	{
		return GetAvrgCompareRate(true);
	}
	
	private double GetAvrgCompareRate(Boolean isOriginal)
	{
		LogicGatesNetwork network;
		
		
		if (isOriginal)
		{
			network = m_originalNetwork;
		}
		else
		{
			network = m_mutatedNetwork;
		}
		
		
		double sum = 0;
		for (int i = 0; i < Defines.NUMBER_OF_TESTING_INPUTS; i++)
		{
			List<Boolean> output = network.runNetwork(m_allInputs[i]);
			sum += Utilites.CompareOutputs(m_currentOutputs.get(i), output);
		}
		return sum / Defines.NUMBER_OF_TESTING_INPUTS;
	}
	
	private void CalculateOriginalOutputs()
	{
		m_originalOutputs.clear();
		m_currentOutputs.clear();
		for (int i = 0; i < Defines.NUMBER_OF_TESTING_INPUTS; i++)
		{
			ArrayList<Boolean> output = m_originalNetwork.runNetwork(m_allInputs[i]);
			m_originalOutputs.add(output);
			m_currentOutputs.add(output);
		}
	}
	


}
