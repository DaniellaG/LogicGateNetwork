import java.util.List;

public class AndGate extends LogicGate{
	
	public AndGate()
	{
		m_numberOfInputs = 2;
	}
	
	public AndGate(LogicGate anotherGate)
	{
		super(anotherGate);
		m_numberOfInputs = 2;
	}
	
	public int GetType()
	{
		return Defines.GATE_AND;
	}


	@Override
	public String toString() {
		return "AND Gate: " + super.toString();
	}


	@Override
	public Boolean GetOutput(List<Boolean> input) {
		if (!CheckIfOutputPossible(input))
			return GetOutputWhenNotPossible(input);
		return input.get(0) & input.get(1);
	}

}
