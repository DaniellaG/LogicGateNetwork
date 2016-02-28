import java.util.List;


public class NorGate  extends LogicGate{
	public NorGate()
	{
		m_numberOfInputs = 2;
	}
	
	public NorGate(LogicGate anotherGate)
	{
		super(anotherGate);
		m_numberOfInputs = 2;
	}
	
	public int GetType()
	{
		return Defines.GATE_NOR;
	}
	
	@Override
	public String toString() {
		return "NOR Gate:" + super.toString();
	}


	@Override
	public Boolean GetOutput(List<Boolean> input) {
		if (!CheckIfOutputPossible(input))
			return GetOutputWhenNotPossible(input);
		return !(input.get(0) || input.get(1));
	}
}
