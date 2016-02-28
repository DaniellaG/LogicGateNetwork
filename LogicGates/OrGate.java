import java.util.List;

public class OrGate extends LogicGate{
	public OrGate()
	{
		m_numberOfInputs = 2;
	}
	
	public OrGate(LogicGate anotherGate)
	{
		super(anotherGate);
		m_numberOfInputs = 2;
	}
	
	public int GetType()
	{
		return Defines.GATE_OR;
	}
	
	@Override
	public String toString() {
		return "OR Gate:" + super.toString();
	}


	@Override
	public Boolean GetOutput(List<Boolean> input) {
		if (!CheckIfOutputPossible(input))
			return GetOutputWhenNotPossible(input);
		return input.get(0) || input.get(1);
	}
}
