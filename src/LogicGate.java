import java.util.ArrayList;
import java.util.List;


public abstract class LogicGate {

	protected List<InputLocation<Integer, Integer>> m_inputIndex;
	protected int m_numberOfInputs;
	protected Boolean m_is_constant_output;
	protected Boolean m_output;
	
	private Boolean m_backupConstantValue;
	private List<InputLocation<Integer, Integer>> m_backInputIndex;
	
	public LogicGate()
	{
		m_inputIndex = new ArrayList<InputLocation<Integer, Integer>>();
		m_numberOfInputs = 0;
		m_is_constant_output = false;
		m_output = false;
		
		m_backupConstantValue = false;
		m_backInputIndex = null;
	}
	
	public LogicGate(LogicGate anotherGate)
	{
		m_numberOfInputs = anotherGate.m_numberOfInputs;
		m_is_constant_output = anotherGate.m_is_constant_output;
		m_output = anotherGate.m_output;
		
		m_backupConstantValue = anotherGate.m_backupConstantValue;
		if (anotherGate.m_backInputIndex != null)
		{
			m_backInputIndex = new ArrayList<InputLocation<Integer, Integer>>();
			for (int i = 0; i< anotherGate.m_backInputIndex.size(); i++)
			{
				m_backInputIndex.add(new InputLocation<Integer, Integer> (anotherGate.m_backInputIndex.get(i)));
			}
		}
		else
		{
			m_backInputIndex = null;
		}
		
		m_inputIndex = new ArrayList<InputLocation<Integer, Integer>>();
		for (int i = 0; i < anotherGate.m_inputIndex.size(); i++)
		{
			m_inputIndex.add(new InputLocation<Integer, Integer> (anotherGate.m_inputIndex.get(i)));
		}
	}
	
	abstract public Boolean GetOutput(List<Boolean> input);
	abstract public int GetType();

	public String toString() {return "Input from " + m_inputIndex.toString();}
	
	public boolean compareGate(LogicGate anotherGate)
	{
		if (GetType() != anotherGate.GetType())
			return false;
				
		if (m_numberOfInputs != anotherGate.m_numberOfInputs)
			return false;
		//not checking for constant values, because we compare networks only at the beginning, before damage
		
		for (int i = 0; i < m_numberOfInputs; i++)
		{
			if (!m_inputIndex.get(i).compareLocation(anotherGate.m_inputIndex.get(i)))
				return false;
		}
		
		return true;
	}
	
	
	public void SetInputIndex(List<InputLocation<Integer, Integer>> inputIndex)
	{
		if (inputIndex.size() != m_numberOfInputs)
		{
			//raise an exception
		}
		m_inputIndex.clear();
		m_inputIndex.addAll(inputIndex);
	}
	
	public List<InputLocation<Integer, Integer>> GetInputIndex()	{ return m_inputIndex;	}
	public int GetInputSize() {return m_numberOfInputs;}
	public void ChangeOutputToConstant()
	{
		m_output = Utilites.RandomBoolean();
		m_is_constant_output = true;
		
		m_backupConstantValue = true;
	}
	
	public void AddSpecificInput(int layer, int gate)
	{		
		//if the gate has enough inputs, delete one of them
		if (m_numberOfInputs <= m_inputIndex.size())
		{
			DisconnectInput();
		}
		m_inputIndex.add(new InputLocation<Integer, Integer>(layer, gate));	

	}
	
	public void DisconnectInput()
	{
		if (m_inputIndex.size() == 0)
			return;
		int indexToRemove = Utilites.GetRandom(m_inputIndex.size() -1, 0);
		m_inputIndex.remove(indexToRemove);
	}
	
	public void Undo()
	{
		if (m_backupConstantValue)
		{
			if (Defines.LOG_LEVEL >= Defines.DEBUG_LEVEL)
				System.out.println("Undoing: changing a constant gate to be non-constant");
			m_is_constant_output = false;
			m_backupConstantValue = false;
		}
		
		if (m_backInputIndex != null)
		{
			if (Defines.LOG_LEVEL >= Defines.DEBUG_LEVEL)
				System.out.println("Undoing: Changing the input index to previous version");
			m_inputIndex.clear();
			m_inputIndex.addAll(m_backInputIndex);
			m_backInputIndex = null;
		}
	}
	
	public void ReconnectInput(List<Integer>layersSizes, int currentLayerNumber)
	{
		m_backInputIndex = new ArrayList<InputLocation<Integer, Integer>> ();
		m_backInputIndex.addAll(m_inputIndex);
		
		if (Defines.LOG_LEVEL >= Defines.DEBUG_LEVEL)
			System.out.println("Gate Recover: back up old input index of gate");

		//if the gate needs more inputs than it currently has, just randomly add more
		if (m_numberOfInputs > m_inputIndex.size())
		{
			//int choosenLayer = Utilites.GetRandom(currentLayerNumber -1, 0);
			//int choosenInputWithinLayer = Utilites.GetRandom(layersSizes.get(choosenLayer) -1, 0);
			int choosenLayer = Utilites.GetRandom(currentLayerNumber, 0);
			int choosenInputWithinLayer = Utilites.GetRandom(layersSizes.get(choosenLayer) -1, 0);
			m_inputIndex.add(new InputLocation<Integer, Integer>(choosenLayer, choosenInputWithinLayer));
			
		}
		else //we need to change existing input that gate has (change layer index and input index from the layer)
		{
			int inputToRemove = Utilites.GetRandom(m_numberOfInputs -1, 0);
			
			//int choosenLayer = Utilites.GetRandom(currentLayerNumber-1 , 0);
			int choosenLayer = Utilites.GetRandom(currentLayerNumber , 0);
			int choosenInput = Utilites.GetRandom(layersSizes.get(choosenLayer) - 1, 0);
			
			m_inputIndex.set(inputToRemove, new InputLocation<Integer, Integer>(choosenLayer, choosenInput));
		}
	}
	
	public void DeletePrevRecover()
	{
		m_backupConstantValue = false;
		m_backInputIndex = null;
	}
	
	protected Boolean CheckIfOutputPossible(List<Boolean> input)
	{
		if (m_is_constant_output)
			return false;
		
		if (input.size() < m_numberOfInputs)
			return false;
		return true;
	}
	protected Boolean GetOutputWhenNotPossible(List<Boolean> input)
	{
		if (m_is_constant_output)
			return m_output;
		
		if (input.size() < m_numberOfInputs)
		{
			if (input.size() == 0)
				return Utilites.RandomBoolean();
			
			return input.get(Utilites.GetRandom(input.size() -1, 0));
		}
		
		return true;
	}
	
 	public void CopyDataFromGate(LogicGate gate,int layerNumber, List<Integer> layersSizes)
	{
		//if the number of inputs of the to-be-copied gate is not the same as wanted number of inputs
		//choose randomly some of the inputs 
		if (gate.GetInputSize() > m_numberOfInputs)
		{
			m_inputIndex.addAll(gate.GetInputIndex());
			while (m_inputIndex.size() > m_numberOfInputs)
			{
				m_inputIndex.remove(Utilites.GetRandom(m_inputIndex.size() -1, 0));
			}
		}
		
		//if we need to connect here more inputs, choose randomly
		else if (gate.GetInputSize() < m_numberOfInputs)
		{
			m_inputIndex.addAll(gate.GetInputIndex());
			while (m_inputIndex.size() < m_numberOfInputs)
			{
				int layerIndex = Utilites.GetRandom(layerNumber - 1, 0);
				int inputWithinLayer = Utilites.GetRandom(layersSizes.get(layerIndex) - 1, 0); //SHOULD BE ADDED +1 HERE?
				m_inputIndex.add(new InputLocation<Integer, Integer>(layerIndex, inputWithinLayer));
			}
		}
		//if both gates have the same number of inputs, just copy the inputs index vector
		else
		{
			m_inputIndex.addAll(gate.GetInputIndex());
		}
	}
}
