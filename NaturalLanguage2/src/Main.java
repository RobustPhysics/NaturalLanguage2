import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main
{
	
	public static void main(String[] args)
	{
		String inputSentence = "Mary gave the boat"; //"Alice gave Bob a picture of the boat.";
		
		//Lexicon lexicon = Lexicon.getInstance();
		
		System.out.println("START");
		ATN atn = new ATN_Main();
		
		String[] wordsArray = inputSentence.split("\\W+");
		List<String> words = new ArrayList<String>(Arrays.asList(wordsArray));
		
		System.out.println("WORDS:");
		for (String s : words)
		{
			System.out.println(s);
		}
		
		ATN_Output output = atn.process(words);
		
		String status = "Failure";
		if (!output.failed)
			status = "Success";
		System.out.println("RESULT (Status = " + status + "):");
		System.out.println(output);
	}
}
