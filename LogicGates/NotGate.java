import java.util.List;


public class NotGate extends LogicGate{
	public NotGate()
	{
		m_numberOfInputs = 1;
	}
	
	public NotGate(LogicGate anotherGate)
	{
		super(anotherGate);
		m_numberOfInputs = 1;
	}
	
	public int GetType()
	{
		return Defines.GATE_NOT;
	}
	
	@Override
	public String toString() {
		return "NOT Gate:" + super.toString();
	}


	@Override
	public Boolean GetOutput(List<Boolean> input) {
		if (!CheckIfOutputPossible(input))
			return GetOutputWhenNotPossible(input);
		return !(input.get(0));
	}
}
