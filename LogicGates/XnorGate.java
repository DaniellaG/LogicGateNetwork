import java.util.List;


public class XnorGate  extends LogicGate{
	public XnorGate()
	{
		m_numberOfInputs = 2;
	}
	
	public XnorGate(LogicGate anotherGate)
	{
		super(anotherGate);
		m_numberOfInputs = 2;
	}
	
	public int GetType()
	{
		return Defines.GATE_XNOR;
	}
	
	@Override
	public String toString() {
		return "XNOR Gate:" + super.toString();
	}


	@Override
	public Boolean GetOutput(List<Boolean> input) {
		if (!CheckIfOutputPossible(input))
			return GetOutputWhenNotPossible(input);
		return !(input.get(0) ^ input.get(1));
	}
}
