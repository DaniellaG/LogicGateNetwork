import java.util.List;

public class NandGate  extends LogicGate{
	public NandGate() 
	{
		m_numberOfInputs = 2;
	}
	
	public NandGate(LogicGate anotherGate)
	{
		super(anotherGate);
		m_numberOfInputs = 2;
	}
	
	public int GetType()
	{
		return Defines.GATE_NAND;
	}
	
	@Override
	public String toString() {
		return "NAND Gate:" + super.toString();
	}


	@Override
	public Boolean GetOutput(List<Boolean> input) {
		if (!CheckIfOutputPossible(input))
			return GetOutputWhenNotPossible(input);
		return !(input.get(0) & input.get(1));
	}
}
