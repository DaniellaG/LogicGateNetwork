import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.Semaphore;

import javax.swing.JOptionPane;


public class MainClass {

	/**
	 * @param args
	 * @throws UnsupportedEncodingException 
	 * @throws FileNotFoundException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException, InterruptedException {
		System.out.println("Hello World!");
		Logger.init();

		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		
		PrintWriter writer = new PrintWriter("/home/daniella/workspace/LogicGatesNetwork/output_"+ dateFormat.format(cal.getTime()), "UTF-8");
		WriteDetails(writer);
		ManageThreads(writer);
		
		writer.close();
		Logger.close();
		JOptionPane.showMessageDialog(null,"Work Done");

	}
	
	public static void ManageThreads(PrintWriter writer) throws InterruptedException
	{

		Thread[] threadsArr = new Thread[Defines.NUMBER_OF_NETWORKS_TO_TEST];
		Semaphore writer_mutex = new Semaphore(1);
		Boolean[][] inputsToTest = Utilites.GenerateInputs();
		for (int i = 0; i < Defines.NUMBER_OF_NETWORKS_TO_TEST; i++)
		{
			System.out.println("Network number " +String.valueOf(i));
            Thread t = new Thread(new TestNetwork(writer, writer_mutex, inputsToTest));
            threadsArr[i] = t;
            t.start();
		}
		
		for (int i = 0; i < Defines.NUMBER_OF_NETWORKS_TO_TEST; i++)
		{
			threadsArr[i].join();
		}/*
		Semaphore writer_mutex = new Semaphore(1);
		Boolean[][] inputsToTest = Utilites.GenerateInputs();
        TestNetwork t = new TestNetwork(writer, writer_mutex, inputsToTest);
        t.run();*/
	}
	     
	public static void WriteDetails(PrintWriter writer)
	{
		writer.format(Locale.UK, "Parameters: NUMBER_OF_TESTING_INPUTS:%d,NETWORK_INPUT_SIZE:%d,NETWORK_OUTPUT_SIZE:%d," +
					"NUMBER_OF_RECOVER_TRIES:%d,TRAIN_NUMBER_OF_DAMAGES:%d,TEST_NUMBER_OF_DAMAGES:%d,NUMBER_OF_NETWORKS_TO_TEST:%d," +
					"MAX_PERCENTAGE_LAYER_INPUT_OUTPUT_RATIO:%f,MIN_PERCENTAGE_LAYER_INPUT_OUTPUT_RATIO:%f,"+
					"USE_METROPOLIS:%b,RECOVER_RECENTAGE_WANTED:%f,METROPOLIS_NORMALIZATION_FACTOR:%f," + 
					"ACHIVE_PERCENTAGE_OF_WANTED_OUTPUT:%f," +
					"DAMAGE_CHANGE_GATE_TO_CONSTANCE_PROB:%d,DAMAGE_CHANGE_GATE_TO_ANOTHER_PROB:%d,DAMAGE_CHNAGE_CONNECTIONS_PROB:%d," +
					"RECOVER_CHANGE_GATE_TO_ANOTHER_PROB:%d,RECOVER_CHNAGE_CONNECTIONS_PROB:%d,RECOVER_ADD_NEW_GATE:%d" +
					"Compare_TO_PREVIOUS_GEN:%b\n",
					Defines.NUMBER_OF_TESTING_INPUTS, Defines.NETWORK_INPUT_SIZE, Defines.NETWORK_OUTPUT_SIZE, 
					Defines.NUMBER_OF_RECOVER_TRIES, Defines.TRAIN_NUMBER_OF_DAMAGES, Defines.TEST_NUMBER_OF_DAMAGES, Defines.NUMBER_OF_NETWORKS_TO_TEST,
					Defines.MAX_PERCENTAGE_LAYER_INPUT_OUTPUT_RATIO, Defines.MIN_PERCENTAGE_LAYER_INPUT_OUTPUT_RATIO,
					Defines.USE_METROPOLIS, Defines.RECOVER_RECENTAGE_WANTED, Defines.METROPOLIS_NORMALIZATION_FACTOR,
					Defines.ACHIVE_PERCENTAGE_OF_WANTED_OUTPUT,
					Defines.DAMAGE_CHANGE_GATE_TO_CONSTANCE_PROB,Defines.DAMAGE_CHANGE_GATE_TO_ANOTHER_PROB, Defines.DAMAGE_CHNAGE_CONNECTIONS_PROB,
					Defines.RECOVER_CHANGE_GATE_TO_ANOTHER_PROB,Defines.RECOVER_CHNAGE_CONNECTIONS_PROB, Defines.RECOVER_ADD_NEW_GATE, Defines.Compare_TO_PREVIOUS_GEN); 
		
		System.out.format("Parameters: NUMBER_OF_TESTING_INPUTS:%d,NETWORK_INPUT_SIZE:%d,NETWORK_OUTPUT_SIZE:%d," +
				"NUMBER_OF_RECOVER_TRIES:%d,TRAIN_NUMBER_OF_DAMAGES:%d,TEST_NUMBER_OF_DAMAGES:%d,NUMBER_OF_NETWORKS_TO_TEST:%d," +
				"MAX_PERCENTAGE_LAYER_INPUT_OUTPUT_RATIO:%f,MIN_PERCENTAGE_LAYER_INPUT_OUTPUT_RATIO:%f,"+
				"USE_METROPOLIS:%b,RECOVER_RECENTAGE_WANTED:%f,METROPOLIS_NORMALIZATION_FACTOR:%f," + 
				"ACHIVE_PERCENTAGE_OF_WANTED_OUTPUT:%f," +
				"DAMAGE_CHANGE_GATE_TO_CONSTANCE_PROB:%d,DAMAGE_CHANGE_GATE_TO_ANOTHER_PROB:%d,DAMAGE_CHNAGE_CONNECTIONS_PROB:%d," +
				"RECOVER_CHANGE_GATE_TO_ANOTHER_PROB:%d,RECOVER_CHNAGE_CONNECTIONS_PROB:%d,Compare_TO_PREVIOUS_GEN:%b\n",
				Defines.NUMBER_OF_TESTING_INPUTS, Defines.NETWORK_INPUT_SIZE, Defines.NETWORK_OUTPUT_SIZE, 
				Defines.NUMBER_OF_RECOVER_TRIES, Defines.TRAIN_NUMBER_OF_DAMAGES, Defines.TEST_NUMBER_OF_DAMAGES, Defines.NUMBER_OF_NETWORKS_TO_TEST,
				Defines.MAX_PERCENTAGE_LAYER_INPUT_OUTPUT_RATIO, Defines.MIN_PERCENTAGE_LAYER_INPUT_OUTPUT_RATIO,
				Defines.USE_METROPOLIS, Defines.RECOVER_RECENTAGE_WANTED, Defines.METROPOLIS_NORMALIZATION_FACTOR,
				Defines.ACHIVE_PERCENTAGE_OF_WANTED_OUTPUT,
				Defines.DAMAGE_CHANGE_GATE_TO_CONSTANCE_PROB,Defines.DAMAGE_CHANGE_GATE_TO_ANOTHER_PROB, Defines.DAMAGE_CHNAGE_CONNECTIONS_PROB,
				Defines.RECOVER_CHANGE_GATE_TO_ANOTHER_PROB,Defines.RECOVER_CHNAGE_CONNECTIONS_PROB, Defines.Compare_TO_PREVIOUS_GEN); 
		
		
	}

}
