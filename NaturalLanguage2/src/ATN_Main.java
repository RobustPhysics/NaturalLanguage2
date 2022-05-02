import java.util.ArrayList;
import java.util.List;

/*
This is the ATN for "S"
*/
public class ATN_Main extends ATN
{
	public ATN_Main()
	{
		super();
		
		addArc(0, "NP");
		addArc(1, "VERB");
		addArc(2, "NP");
		addArc(2, "JUMP");
		addArc(3, "NP");
		addArc(3, "JUMP");
		addArc(4, "DONE");
	}
	
	/*
	private void branchToNP(ATN_Output output, List<String> words)
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
	*/
	
	public ATN_Output process(List<String> words)
	{
		ATN_Output output = new ATN_Output("S");
		
		int state = 0;
		boolean done = false;
		LexiconWord verb = null;
		
		while (!done && words.size() > 0)
		{
			String symbol = words.get(0);
			
			System.out.println("S -> Processing word " + symbol);
			LexiconWord word = getWordFromLexicon(symbol);
			
			if (word == null)
			{
				System.out.println("ERROR! Could not find word " + symbol + " in Lexicon!");
				return null;
			}
			assert state <= arcs.size()-1;
			
			//List<ArcData> currentArcs = arcs.get(state);
			List<String> currentArcs = arcs.get(state);
			int arcIndex = 0;
			int lastState = state;
			
			while (!done && state == lastState && arcIndex < currentArcs.size())
			{
				//ArcData arcInfo = currentArcs.get(arcIndex);
				//String category = arcInfo.category;
				//String type = arcInfo.value;
				String arcLabel = currentArcs.get(arcIndex);
				arcIndex++;
				System.out.println("S -> ArcIndex = " + arcIndex);
				
				switch (state)
				{
					////////////////////////////////////////////////////////////////////////////////////////////////////////
					case 0: //NODE IS EQUAL TO S
						
						if (arcLabel.equals("NP"))
						{
							ATN_NP np = new ATN_NP();
							//words.remove(0);
							ATN_Output childOutput = np.process(words);
							
							ATN_Output subj = new ATN_Output("SUBJ");
							subj.children.add(childOutput);
							
							output.addFeature("MOOD", "declarative");
							output.addChild(subj);
							//output.children.add(subj);
							state = 1; //since state changes, we don't need a flag for word changing
						}
						
						break;
					////////////////////////////////////////////////////////////////////////////////////////////////////////
					case 1:  //NODE IS EQUAL TO S1
						if (word.isType(arcLabel)) //arc is a word, not a sub ATN
						{
							if (arcLabel.equals("VERB"))
							{
								ATN_Output subj = output.findChild("SUBJ");
								
								if (subj != null)
								{
									ATN_Element element = subj.findFeatureInOutput("NUM");
									List<String> numWord = word.getValueFromFeature("NUM");
									
									ATN_Element intersect = element.getIntersection(numWord);
									if (intersect.values.size() > 0)
									{
										verb = word;
										output.setFeature("VERB", word.getWord());
										output.setFeature("NUM", intersect.values);
										words.remove(0);
										state = 2;
									}
								}
								else
								{
									System.out.println("ERROR! Tried to find SUBJ, got null!");
									done = true;
								}
							}
						}
						else
						{
							System.out.println("ERROR! No possible arc to travel!");
							done = true;
						}
						
						
						break;
					////////////////////////////////////////////////////////////////////////////////////////////////////////
					case 2:  //NODE IS EQUAL TO S2
						if (arcLabel.equals("NP"))
						{
							if (verb != null && verb.isFeatureValue("TYPE", "transitive"))
							{
								ATN_NP np = new ATN_NP();
								ATN_Output childOutput = np.process(words);
								
								ATN_Output obj = new ATN_Output("OBJ");
								obj.children.add(childOutput);
								
								output.addChild(obj);
								
								state = 3;
							}
						}
						else if (arcLabel.equals("JUMP"))
						{
							if (!word.isFeatureValue("TYPE", "transitive"))
							{
								state = 4;
							}
						}
						break;
					////////////////////////////////////////////////////////////////////////////////////////////////////////
					case 3:
						if (arcLabel.equals("NP"))
						{
							if (verb != null && verb.isFeatureValue("TYPE", "bitransitive"))
							{
								ATN_Output lastObj = output.findChild("OBJ");
								if (lastObj != null)
								{
									ATN_NP np = new ATN_NP();
									ATN_Output childOutput = np.process(words);
									/*
									So we have * (it is NP)
									We have OBJ...
									Rename OBJ to IND-OBJ?
									*/
									lastObj.label = "IND-OBJ";
									
									ATN_Output obj = new ATN_Output("OBJ");
									obj.children.add(childOutput);
									
									output.addChild(obj);
									state = 4;
								}
								else
								{
									System.out.println("ERROR! Failed to find OBJ!");
									done = true;
								}
								
							}
						}
						else if (arcLabel.equals("JUMP"))
						{
							state = 4;
						}
						
					////////////////////////////////////////////////////////////////////////////////////////////////////////
					case 4:
						if (arcLabel.equals("DONE"))
							done = true;
						break;
					default:  //NODE IS UNKNOWN
						System.out.println("ERROR! How did we get here? State = " + state);
						break;
				}
				
				System.out.println("S -> All arcs viewed. State went from " + lastState + " to " + state);
			}
		}
		
		if (words.size() > 0)
			output.failed = true;
		
		return output;
	}
	
	
	
	
	/*
	if (state > arcs.size()-1)
	{
		System.out.println("ERROR! Unable to find arc info for state " + state + " under ATN " + this.getClass());
	}
	else if (arcs.get(state).size() == 0)
	{
		System.out.println("ERROR! There are no arcs for state " + state + " under ATN " + this.getClass());
	}
	*/
}
