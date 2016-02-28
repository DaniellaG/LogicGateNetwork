import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class NetworkLayer {
	
	List<LogicGate> m_allGates;
	int m_inputSize;
	int m_outputSize;
	int m_layerNumber;
	Random m_random;
	LogicGate m_backupGate;
	int m_backupGateIndex;
	int m_backupNewGateIndex;
	int m_changedGateIndex;
	
	
	public NetworkLayer(int inputSize, int layerIndex)
	{
		initialize();
		m_inputSize = inputSize;
		m_layerNumber = layerIndex;
		
		CreateRandomGates();
		ConnectInputToGates();	
	}
	
	public NetworkLayer (NetworkLayer anotherLayer)
	{
		initialize();
		m_inputSize = anotherLayer.m_inputSize;
		m_outputSize = anotherLayer.m_outputSize;
		m_layerNumber = anotherLayer.m_layerNumber;
		
		m_backupGateIndex = -1;
		m_backupGate = null;
		m_backupNewGateIndex = -1;
		m_changedGateIndex = -1;
		
		for (LogicGate gate: anotherLayer.m_allGates)
		{
			CopyNewGate(gate);
		}
	}
	
	public boolean compareLayer(NetworkLayer anotherLayer)
	{		
		if (m_layerNumber != anotherLayer.m_layerNumber || m_inputSize != anotherLayer.m_inputSize || m_outputSize != anotherLayer.m_outputSize)
			return false;
		
		int gatesNumber = m_allGates.size();
		for (int i = 0; i < gatesNumber; i++)
		{
			if (!m_allGates.get(i).compareGate(anotherLayer.m_allGates.get(i)))
				return false;
		}
		return true;
		
	}
	
	private void CopyNewGate(LogicGate anotherGate)
	{
		m_allGates.add(ChooseWhichGateToCopy(anotherGate));
	}
	
	private LogicGate ChooseWhichGateToCopy(LogicGate anotherGate)
	{
		LogicGate gate;
		switch (anotherGate.GetType())
		{
			case Defines.GATE_AND:
				gate = new AndGate(anotherGate);
				break;
			case Defines.GATE_NAND:
				gate = new NandGate(anotherGate);
				break;
			case Defines.GATE_NOR:
				gate = new NorGate(anotherGate);
				break;
			case Defines.GATE_NOT:
				gate = new NotGate(anotherGate);
				break;
			case Defines.GATE_OR:
				gate = new OrGate(anotherGate);
				break;
			case Defines.GATE_XNOR:
				gate = new XnorGate(anotherGate);
				break;
			case Defines.GATE_XOR:
				gate = new XorGate(anotherGate);
				break;
			default: //TO DO: check how to get rid of default statement
				gate = new AndGate(anotherGate);
				break;
		}
		return gate;
	}
		
	private void ConnectInputToGates()
	{
		//randomly connect inputs to the logical gates
		ArrayList<Integer> usedInputs = new ArrayList<Integer>();
		ArrayList<Integer> possibleInputs = new ArrayList<Integer>();
		
		//add possible input indexes to the list
		for (int j = 0; j < m_inputSize; j++)
			possibleInputs.add(j);

		//for every gate
		for (int i = 0; i < m_outputSize; i++)
		{
			//for every input that gate needs
			int numOfWantedInputs = m_allGates.get(i).GetInputSize();
			
			List<InputLocation<Integer, Integer>> wantedInputIndex = new ArrayList<InputLocation<Integer, Integer>>();
			
			//for every input that specific gate needs
			for (int j = 0; j < numOfWantedInputs; j++)
			{
				int randomInputIndex = GetRandomInputIndex(usedInputs, possibleInputs);
				wantedInputIndex.add(new InputLocation<Integer, Integer>(m_layerNumber, randomInputIndex));
			}
			m_allGates.get(i).SetInputIndex(wantedInputIndex);
		}
	}
	private void CreateRandomGates()
	{
		//Randomly create the logical gates
		
		//int numberOfGates = Utilites.GetRandom(Defines.MIN_PERCENTAGE_OF_WANTED_OUTPUT * (m_inputSize / 2), Defines.MAX_PERCENTAGE_OF_WANTED_OUTPUT * (m_inputSize / 2));
		//change Density of the network, don't Converge too fast, every layer should be only a little smaller than the one before it
		int numberOfGates = Utilites.GetRandom(Defines.MAX_PERCENTAGE_LAYER_INPUT_OUTPUT_RATIO * m_inputSize, Defines.MIN_PERCENTAGE_LAYER_INPUT_OUTPUT_RATIO * m_inputSize);
		for (int i = 0; i < numberOfGates; i++)
		{
			//Choose a random gate
			LogicGate newGate = GetRandomLogicGate();
			m_allGates.add(newGate);
		}	
		//notice: the output of layer is the number of logical gates it has
		m_outputSize = numberOfGates;
	}
	private void initialize()
	{
		m_allGates = new ArrayList<>();
		m_random = new Random();
	}
	public void PrintLayer()
	{
		System.out.println("Layer number " + String.valueOf(m_layerNumber) + ". Input size:" + String.valueOf(m_inputSize) + ", output size:" + String.valueOf(m_outputSize) + "\n");
		for (int i = 0; i < m_outputSize; i++)
		{
			System.out.println(String.valueOf(i) + ":" + m_allGates.get(i).toString() + "\n" );
		}
		System.out.println("\n");

	}
	
	private int GetRandomInputIndex (ArrayList<Integer> usedInputs, ArrayList<Integer> possibleInputs)
	{
		//if we used all possible inputs, re-add them to the list and reuse them
		if (possibleInputs.isEmpty())
		{
			possibleInputs.addAll(usedInputs);
			usedInputs.clear();
		}
		int randomInputIndex = possibleInputs.get(m_random.nextInt(possibleInputs.size()));;
		
		
		usedInputs.add(randomInputIndex);
		possibleInputs.remove(new Integer(randomInputIndex));
		
		return randomInputIndex;
	}
	
	private LogicGate GetRandomLogicGate()
	{
		LogicGate gate;
		int randomNumber = m_random.nextInt(Defines.NUMBER_OF_LOGIC_GATES + 1) + 1;
		switch (randomNumber)
		{
			case Defines.GATE_AND:
				gate = new AndGate();
				break;
			case Defines.GATE_NAND:
				gate = new NandGate();
				break;
			case Defines.GATE_NOR:
				gate = new NorGate();
				break;
			case Defines.GATE_NOT:
				gate = new NotGate();
				break;
			case Defines.GATE_OR:
				gate = new OrGate();
				break;
			case Defines.GATE_XNOR:
				gate = new XnorGate();
				break;
			case Defines.GATE_XOR:
				gate = new XorGate();
				break;
			default: //TO DO: check how to get rid of default statement
				gate = new AndGate();
				break;
		}
		return gate;
	}
	
	public ArrayList<Boolean> RunLayer(List<ArrayList<Boolean>> input)
	{
		/*if (input.size() != m_inputSize)
		{
			//throw exception;
		}*/
		
		ArrayList<Boolean> output = new ArrayList<>();
		for (int i = 0; i< m_outputSize; i++)
		{
			List<Boolean> currentInput = new ArrayList<>();
			List<InputLocation<Integer,Integer>> currentInputIndexes = m_allGates.get(i).GetInputIndex();
			
			//currentInputIndexes has pairs of data for each input: Layer index, the index within the layer
			
			for (int j = 0; j < currentInputIndexes.size(); j++)
			{
				try{
					int layerNum = currentInputIndexes.get(j).getLeft();
					int inputNumWithinLayer = currentInputIndexes.get(j).getRight();
					currentInput.add (input.get(layerNum).get(inputNumWithinLayer) );
				}
				catch (Exception e)
				{
					Logger.write("layer number=" +String.valueOf(m_layerNumber) + ",j=" + String.valueOf(j));
					Logger.write("layerNum=" + String.valueOf(currentInputIndexes.get(j).getLeft()) + ", inputNumWithinLayer=" + String.valueOf(currentInputIndexes.get(j).getRight()));
					throw e;
				}
			}
			
			output.add(m_allGates.get(i).GetOutput(currentInput));
		}
		return output;
	}
		
	public int GetOutputSize() {return m_outputSize;}
	public void ChangeGateToConstant()
	{		
		m_changedGateIndex = Utilites.GetRandom(m_allGates.size() - 1, 0);
		m_allGates.get(m_changedGateIndex).ChangeOutputToConstant();
	}
	
	public void SaveBackupGate(int index)
	{
		DeletePrevRecover();
		//System.out.format("layer %d setting the backup index to %d\n", m_layerNumber, index);
		Logger.write("layer " + String.valueOf(m_layerNumber) + "setting the backup index to " + String.valueOf(index));
		m_backupGateIndex = index;
		m_backupGate =  ChooseWhichGateToCopy(m_allGates.get(index));
	}
	
	/*public void SaveSecondBackupGate(int index)
	{
		//DeletePrevRecover();
		//System.out.format("you know, just for checking, layer %d, its backup index is %d, and second backup index is %d\n", m_layerNumber, m_backupGateIndex, index);
		m_backupSecondGate = null;
		m_backupSecondGateIndex = index;
		m_backupSecondGate =  ChooseWhichGateToCopy(m_allGates.get(index));
	}*/
	
	public void ChangeGateToAnother(List<Integer>  layerSizes)
	{
		int gateIndex = Utilites.GetRandom(m_allGates.size() - 1, 0);
		
		SaveBackupGate(gateIndex);
		
		LogicGate newgate = GetRandomLogicGate();
		newgate.CopyDataFromGate(m_allGates.get(gateIndex),m_layerNumber, layerSizes);
		
		m_allGates.set(gateIndex, newgate);
		
		Logger.write("Recover/Damage: changing a gate to another: gate number" + String.valueOf(gateIndex));
		if (Defines.LOG_LEVEL >= Defines.DEBUG_LEVEL)
		{
			System.out.println("Recover/Damage: changing a gate to another: gate number" + String.valueOf(gateIndex));
		}
	}
	
	public LogicGate AddNewGateToLayer()
	{
		LogicGate newgate = GetRandomLogicGate();
		m_allGates.add(newgate);
		
		DeletePrevRecover();
		//System.out.format("layer %d setting the backup index to %d (adding new gate to layer)\n", m_layerNumber, m_outputSize);
		Logger.write("layer " + String.valueOf(m_layerNumber)+ " setting the backup index to " + String.valueOf(m_outputSize)+ " (adding new gate to layer)");
		m_backupNewGateIndex = m_outputSize;
		
		m_outputSize++;
		return newgate;
	}
	public boolean IsLastChangeNewGate()
	{
		if (m_backupNewGateIndex != -1)
			return true;
		return false;
	}
	
	public void SetInputToNewGate(LogicGate newGate, List<Integer> layersSizes)
	{
		/*//save backup of the gate
		SaveBackupGate(m_allGates.indexOf(newGate));*/
		
		//for every input that gate needs
		int numOfWantedInputs = newGate.GetInputSize();
		
		List<InputLocation<Integer, Integer>> wantedInputIndex = new ArrayList<InputLocation<Integer, Integer>>();
		
		//for every input that specific gate needs
		for (int j = 0; j < numOfWantedInputs; j++)
		{
			int randomLayer = Utilites.GetRandom(m_layerNumber, 0);
			int randomIndex = Utilites.GetRandom(layersSizes.get(randomLayer)-1, 0);
			wantedInputIndex.add(new InputLocation<Integer, Integer>(randomLayer, randomIndex));
			Logger.write("New gate in layer " + String.valueOf(m_layerNumber) + " added a new input from layer " + String.valueOf(randomLayer) + " amd index " + String.valueOf(randomIndex) );
		}
		newGate.SetInputIndex(wantedInputIndex);
	}
	
	public void AddSpecificInput(int GateIndex, int inputLayerIndex, int inputGateIndex)
	{
		SaveBackupGate(GateIndex);
		m_allGates.get(GateIndex).AddSpecificInput(inputLayerIndex, inputGateIndex);
	}
	
	public void UndoRecoverLayer()
	{
		if (Defines.LOG_LEVEL >= Defines.DEBUG_LEVEL)
			System.out.println("Undoing: layer number:" + String.valueOf(m_layerNumber));
		Logger.write("Undoing: layer number:" + String.valueOf(m_layerNumber));
		
		if (m_backupNewGateIndex != -1)
		{
			//System.out.format("Undoing recover in layer %d, gate %d, deleting the gate\n", m_layerNumber, m_backupNewGateIndex);
			Logger.write("Undoing recover in layer " + String.valueOf(m_layerNumber)+ ", gate " + String.valueOf(m_backupNewGateIndex)+", deleting the gate");
			m_allGates.remove(m_allGates.size() -1);
			m_outputSize--;
			m_backupGateIndex = -1;
		}
		else if (m_backupGateIndex != -1)
		{
			//System.out.format("Undoing: changing in layer %d, gate number %d to its backup", m_layerNumber, m_backupGateIndex);
			Logger.write("Undoing: changing in layer " + String.valueOf(m_layerNumber)+ ", gate number " + String.valueOf(m_backupGateIndex)+" to its backup");

			m_allGates.set(m_backupGateIndex, m_backupGate);
			m_backupGateIndex = -1;
			m_backupGate = null;
		}
		else if (-1 != m_changedGateIndex)
		{
			Logger.write("Undoing: calling gates undo function: gate number" + String.valueOf(m_changedGateIndex));
			if (Defines.LOG_LEVEL >= Defines.DEBUG_LEVEL)
				System.out.println("Undoing: calling gates undo function: gate number" + String.valueOf(m_changedGateIndex));
			
			m_allGates.get(m_changedGateIndex).Undo();
			m_changedGateIndex = -1;
		}
		else
		{
			//cannot undo
			//System.out.format("Cannot do recover (why?) layer %d\n", m_layerNumber);
			Logger.write("Cannot do recover (why?) layer " + String.valueOf(m_layerNumber));
			if (Defines.LOG_LEVEL >= Defines.DEBUG_LEVEL)
				System.out.println("Undoing: Cannot undo");

		}
		//DeletePrevRecover();
	}
	
	public void DisconnectInput()
	{
		LogicGate gate = m_allGates.get(Utilites.GetRandom(m_allGates.size() - 1, 0));
		gate.DisconnectInput();
	}
	
	public void ReconnectInput(List<Integer> layersSizes)
	{
		m_changedGateIndex = Utilites.GetRandom(m_allGates.size() - 1, 0);
		LogicGate gate = m_allGates.get(m_changedGateIndex);
		gate.DeletePrevRecover();
		gate.ReconnectInput(layersSizes, m_layerNumber);
		
		Logger.write("Recover: Reconnect input: of gate number " + String.valueOf(m_changedGateIndex));
		if (Defines.LOG_LEVEL >= Defines.DEBUG_LEVEL)
			System.out.println("Recover: Reconnect input: of gate number " + String.valueOf(m_changedGateIndex));
	}
	
	public void DeletePrevRecover()
	{
		if (null != m_backupGate)
		{
			m_backupGate.DeletePrevRecover();
		}
		
		m_backupGate = null;
		m_backupGateIndex = -1;
		m_changedGateIndex = -1;
		m_backupNewGateIndex = -1;
	}
	
	public LogicGate GetRandomGate()
	{
		LogicGate gate = m_allGates.get(Utilites.GetRandom(m_allGates.size() - 1, 0));
		return gate;
	}
	
	public int GetSize()
	{
		return m_allGates.size();
	}
}
