
public class Defines {
	//Layer Properties
	public static final double MAX_PERCENTAGE_LAYER_INPUT_OUTPUT_RATIO = 0.9;
	public static final double MIN_PERCENTAGE_LAYER_INPUT_OUTPUT_RATIO = 0.6;
	
	//Network Properties
	public static final double ACHIVE_PERCENTAGE_OF_WANTED_OUTPUT = 1.1;

	
	//Logic Gates defines
	public static final int NUMBER_OF_LOGIC_GATES = 7;
	public static final int GATE_AND = 1;
	public static final int GATE_NAND = 2;
	public static final int GATE_NOR = 3;
	public static final int GATE_NOT = 4;
	public static final int GATE_OR = 5;
	public static final int GATE_XNOR = 6;
	public static final int GATE_XOR = 7;
	
	//Probabilities
	//Damage network
	public static final int DAMAGE_CHANGE_GATE_TO_CONSTANCE_PROB = 33;
	public static final int DAMAGE_CHANGE_GATE_TO_ANOTHER_PROB = 33;
	public static final int DAMAGE_CHNAGE_CONNECTIONS_PROB = 34;
	
	//Recover Network
	public static final int RECOVER_CHANGE_GATE_TO_ANOTHER_PROB = 25;
	public static final int RECOVER_CHNAGE_CONNECTIONS_PROB = 50;
	public static final int RECOVER_ADD_NEW_GATE = 25;
	public static final double RECOVER_RECENTAGE_WANTED = 1;
	public static final Boolean USE_METROPOLIS = false;
	public static final double METROPOLIS_NORMALIZATION_FACTOR = 10000;

	
	
	
	//Testing options
	public static final int NUMBER_OF_TESTING_INPUTS = 100;
	public static final int NETWORK_INPUT_SIZE = 100;
	public static final int NETWORK_OUTPUT_SIZE = 20;
	public static final int NUMBER_OF_RECOVER_TRIES = 40000;
	public static final int TRAIN_NUMBER_OF_DAMAGES = 25;
	public static final int TEST_NUMBER_OF_DAMAGES = 10;
	public static final int NUMBER_OF_NETWORKS_TO_TEST = 5;

	
	
	//LOG
	public static final int LOG_LEVEL = 2;
	public static final int DEBUG_LEVEL = 8;
	
	//Logic flows
	public static final boolean Compare_TO_PREVIOUS_GEN = false;



}
