import java.util.List;

public class XorGate  extends LogicGate{
	public XorGate()
	{
		m_numberOfInputs = 2;
	}
	
	public XorGate(LogicGate anotherGate)
	{
		super(anotherGate);
		m_numberOfInputs = 2;
	}
	
	public int GetType()
	{
		return Defines.GATE_XOR;
	}
	
	@Override
	public String toString() {
		return "XOR Gate:" + super.toString();
	}


	@Override
	public Boolean GetOutput(List<Boolean> input) {
		if (!CheckIfOutputPossible(input))
			return GetOutputWhenNotPossible(input);
		return input.get(0) ^ input.get(1);
	}
}
