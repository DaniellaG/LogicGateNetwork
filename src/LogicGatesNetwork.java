import java.util.ArrayList;
import java.util.List;

public class LogicGatesNetwork {
	
	//AND, OR, 	NAND, NOR, XOR, XNOR
	//NOT
	private int m_inputSize;
	private int m_outputSize;
	private int m_numberLayers;
	private List<NetworkLayer> m_layers;
	private List<Integer> m_layersSizes;
	private int m_backupLayerIndex;
	private int m_secondBackupLayerIndex;
	
	public LogicGatesNetwork(int input, int output) //initialize the network randomly
	{
		m_inputSize = input;
		m_outputSize = output;
		m_backupLayerIndex = -1;
		m_secondBackupLayerIndex = -1;
		
	     initialize();

	     int currentOutput = m_inputSize;	     
	     m_layersSizes.add(m_inputSize);
	     
	     while(currentOutput > m_outputSize * Defines.ACHIVE_PERCENTAGE_OF_WANTED_OUTPUT)
	     {	    	 
	    	 currentOutput = CreateNewLayer(currentOutput);
	    	 m_layersSizes.add(currentOutput);
	     }
	}
	
	private void initialize()
	{
	     m_layers = new ArrayList<>();
	     m_layersSizes = new ArrayList<>();
	     m_numberLayers = 0;
	}
	
	public LogicGatesNetwork(LogicGatesNetwork anotherNetwork)
	{
		m_inputSize = anotherNetwork.m_inputSize;
		m_outputSize = anotherNetwork.m_outputSize;
		m_backupLayerIndex = -1;
		m_secondBackupLayerIndex = -1;
		
	     initialize();
	     m_layersSizes.addAll(anotherNetwork.m_layersSizes);

	     for (NetworkLayer layer: anotherNetwork.m_layers)
	     {
	    	 CopyNewLayer(layer);
	     }
	}
	
	
	public boolean CompareNetworks(LogicGatesNetwork anotherNetwork)
	{
		if (m_inputSize != anotherNetwork.m_inputSize)
		{
			return false;
		}
		if (m_outputSize != anotherNetwork.m_outputSize)
		{
			return false;
		}
		if (m_numberLayers != anotherNetwork.m_numberLayers)
		{
			return false;
		}
		
		if (!Utilites.CompareIntsLists(m_layersSizes, anotherNetwork.m_layersSizes))
		{
			return false;
		}
		
		for (int i = 0; i < m_numberLayers; i++)
		{
			if (!m_layers.get(i).compareLayer(anotherNetwork.m_layers.get(i)))
				return false;
		}
		return true;
	}
	
	private void CopyNewLayer(NetworkLayer anotherLayer)
	{
		NetworkLayer layer = new NetworkLayer(anotherLayer);
		m_layers.add(layer);
		m_numberLayers++;
	}
	
	private int CreateNewLayer(int input)
	{
		NetworkLayer layer = new NetworkLayer(input, m_numberLayers);
		m_layers.add(layer);
		m_numberLayers++;
		return layer.GetOutputSize();
	}
	
	public void printNetwork()
	{
		System.out.println("Number of inputs: " + String.valueOf(m_inputSize));
		System.out.println("Number of outputs: " + String.valueOf(m_outputSize));
		System.out.println("Number of layers: " + String.valueOf(m_numberLayers));
		
		for (NetworkLayer layer: m_layers)
			layer.PrintLayer();
		
	}
		
	public ArrayList<Boolean> runNetwork(Boolean[] input)
	{
		List<ArrayList<Boolean>> currentInput = new ArrayList<>();
		ArrayList<Boolean> originalInput = new ArrayList<Boolean>();
		
		for(Boolean in: input)
			originalInput.add(in);
		
		currentInput.add(originalInput);
		
		for (int i = 0; i < m_numberLayers; i++)
		{
			ArrayList<Boolean> layerOutput = new ArrayList<>();
			layerOutput = m_layers.get(i).RunLayer(currentInput);
			currentInput.add(layerOutput);
		}
		
		return currentInput.get(currentInput.size() - 1);
	}
	
	public void damageNetwork()
	{
		int rand = Utilites.GetRandom(100, 0);
		NetworkLayer layer = m_layers.get(Utilites.GetRandom(m_layers.size() - 1, 0));
		
		//change a gate's output to constant 
		if (rand <= Defines.DAMAGE_CHANGE_GATE_TO_CONSTANCE_PROB)
		{
			layer.ChangeGateToConstant();
		}
		//change a gate
		else if (rand <= Defines.DAMAGE_CHANGE_GATE_TO_CONSTANCE_PROB + Defines.DAMAGE_CHANGE_GATE_TO_ANOTHER_PROB)
		{
			layer.ChangeGateToAnother(m_layersSizes);
		}
		//disconnect some input
		else 
		{
			layer.DisconnectInput();
		}		
		System.out.println("Damage Done");
	}
	
	void recoverNetwork()
	{
		int rand = Utilites.GetRandom(100, 0);
		int layerIndex = Utilites.GetRandom(m_layers.size() - 1, 0);
		NetworkLayer layer = m_layers.get(layerIndex);
		
		layer.DeletePrevRecover();
		
		m_backupLayerIndex = layerIndex;
		m_secondBackupLayerIndex = -1;
		
		//change a gate
		if (rand <= Defines.RECOVER_CHANGE_GATE_TO_ANOTHER_PROB)
		{
			Logger.write("Recover:Change gate to another: layer number" + String.valueOf(m_backupLayerIndex));
			if (Defines.LOG_LEVEL >= Defines.DEBUG_LEVEL)
				System.out.println("Recover:Change gate to another: layer number" + String.valueOf(m_backupLayerIndex));
			layer.ChangeGateToAnother(m_layersSizes);
		}		
		//reconnect some random input
		else if (rand <= Defines.RECOVER_CHANGE_GATE_TO_ANOTHER_PROB + Defines.RECOVER_CHNAGE_CONNECTIONS_PROB)
		{
			Logger.write("Recover: Reconnect input: layer number" + String.valueOf(m_backupLayerIndex));
			if (Defines.LOG_LEVEL >= Defines.DEBUG_LEVEL)
				System.out.println("Recover: Reconnect input: layer number" + String.valueOf(m_backupLayerIndex));
			
			layer.ReconnectInput(m_layersSizes);
		}
		//add a new gate
		else 
		{
			//but! we cannot add a new gate to the final layer (cannot change the output size)
			if (layerIndex == m_numberLayers - 1)
			{
				m_backupLayerIndex = -1;
				m_secondBackupLayerIndex = -1;
				return;
			}
			//Add a new gate to the network
			//choose a layer and a gate
			LogicGate newGate = layer.AddNewGateToLayer();
			
			//from the previous layers, get input to this gate
			layer.SetInputToNewGate(newGate, m_layersSizes);
			
			//increase the size of this layer
			m_layersSizes.set(layerIndex+1, m_layersSizes.get(layerIndex+1) + 1);
			
			//randomly choose a gate from the next layers (not including the selected one before)
			int newLayerIndex = Utilites.GetRandom(m_layers.size() - 1, layerIndex + 1);
			int randomGateIndex = Utilites.GetRandom(m_layers.get(newLayerIndex).GetOutputSize() -1 , 0);
			
			//connect the output of the new gate to the chosen gate
			//set the input of the gate to be from <layerIndex, layerIndexSize -1> 
			m_layers.get(newLayerIndex).AddSpecificInput(randomGateIndex, layerIndex+1, layer.GetOutputSize() - 1);
			Logger.write("Added a new gate! It is in layer " + String.valueOf(layerIndex) + ", index " + String.valueOf(m_layersSizes.get(layerIndex+1)) + ", and sends its output to layer " + String.valueOf(newLayerIndex) + ", gate index " + String.valueOf(randomGateIndex));
			//system.out.format("Added a new gate! It is in layer %d, index %d, and sends its output to layer %d, gate index %d\n", layerIndex, m_layersSizes.get(layerIndex+1), newLayerIndex, randomGateIndex);
			
			//set everything needed for backup
			m_backupLayerIndex = layerIndex;
			m_secondBackupLayerIndex = newLayerIndex;
		}
	}
	
	//can only be called once for every recover call!
	public void UndoRecover()
	{
		//System.out.println("Undo!");
		if (m_backupLayerIndex != -1)
		{
			//if the last change was a new gate added to layer,
			//we need to change again the sizes of layers, as we save them
			if (m_layers.get(m_backupLayerIndex).IsLastChangeNewGate())
			{
				//system.out.format("Undoing a new gate: resizing the layer %d to have one less item, now %d\n", m_backupLayerIndex, m_layersSizes.get(m_backupLayerIndex+1)-1);
				Logger.write("Undoing a new gate: resizing the layer " + String.valueOf(m_backupLayerIndex ) + " to have one less item, now " + String.valueOf(m_layersSizes.get(m_backupLayerIndex+1)-1 ));
				m_layersSizes.set(m_backupLayerIndex+1, m_layersSizes.get(m_backupLayerIndex+1) - 1);
			}
			m_layers.get(m_backupLayerIndex).UndoRecoverLayer();
			m_backupLayerIndex = -1;
		}
		if (m_secondBackupLayerIndex != -1)
		{
			m_layers.get(m_secondBackupLayerIndex).UndoRecoverLayer();
			m_secondBackupLayerIndex = -1;
		}
		m_backupLayerIndex = -1;
		m_secondBackupLayerIndex = -1;
	}
	
	public int GetSize()
	{
		int count = 0;
		for (int i = 0; i < m_numberLayers; i++)
		{
			count += m_layers.get(i).GetSize();
		}
		return count;
	}
	
	public int GetNumberOfLayers() {return m_numberLayers;}
}
