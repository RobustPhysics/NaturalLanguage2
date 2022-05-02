import java.util.ArrayList;
import java.util.List;

public class ATN
{
	//List<List<ArcData>> arcs;
	List<List<String>> arcs;
	Lexicon lexicon;
	
	public ATN()
	{
		//arcs = new ArrayList<List<ArcData>>();
		arcs = new ArrayList<List<String>>();
		lexicon = Lexicon.getInstance();
	}
	
	/*
	protected void branchToNP(ATN_Output output, List<String> words)
	{
		ATN_NP np = new ATN_NP();
		//words.remove(0);
		ATN_Output childOutput = np.process(words);
		
		ATN_Output subj = new ATN_Output("SUBJ");
		subj.children.add(childOutput);
		
		output.addFeature("MOOD", "declarative");
		output.addChild(subj);
		//output.children.add(subj);
	}
	
	protected boolean processWord(ATN_Output output, String arcLabel, String symbol, LexiconWord word, List<String> words)
	{
		boolean result = false;
		
		
		return result;
	}
	*/
	
	protected void addArc(int state, String label)
	{
		/*
		state = 0, arcs = 0
			add 1 state
		
		state = 1, arcs = 1
			add 2 states
		
		state = 0, arcs = 1
			add no states
		*/
		
		if (state >= arcs.size()-1)
		{
			int diff = state - arcs.size() + 1;
			//System.out.println("Initializing arcs from  " + (arcs.size()) + " to " + state + " in ATN " + this.getClass() + " - +" + (diff) + " arcs");
			
			for (int i = 0; i < diff; i++)
			{
				List<String> arcList = new ArrayList<String>();
				arcs.add(arcList);
			}
		}
		
		System.out.println("Adding arc " + arcs.get(state).size() + " to state " + state + " as " + label);
		
		List<String> arcAtState = arcs.get(state);
		arcAtState.add(label);
	}
	
	public LexiconWord getWordFromLexicon(String symbol)
	{
		return lexicon.getWord(symbol);
	}
	
	public ATN_Output process(List<String> words)
	{
		System.out.println("ERROR! process() is undefined for " + this.getClass() + "!");
		
		ATN_Output output = null;
		
		return output;
	}
	
	public String toString()
	{
		/*
		ATN Arcs
		Arcs		
		Node/Label
		Node/Label
		*/
		
		//NOTE: Use StringBuilder for better performance
		String output = "ATN Arcs (" + this.getClass() + ")\nArcs\n";
		for (int state = 0; state < arcs.size(); state++)
		{
			List<String> arcsAtState = arcs.get(state);
			for (String label : arcsAtState)
			{
				output = output + "S" + state + "/" + label +"\n";
			}
		}
		
		return output;
	}
}
