import java.util.ArrayList;
import java.util.List;

/*
This is the ATN for "S"
*/
public class ATN_NP extends ATN
{
	
	public ATN_NP()
	{
		super();
		addArc(0, "NAME");
		addArc(0, "NOUN");
		addArc(0, "PRONOUN");
		addArc(0, "ARTICLE");
		addArc(1, "ADJECTIVE");
		addArc(1, "NOUN");
		addArc(2, "DONE");
	}
	
	public ATN_Output process(List<String> words)
	{
		ATN_Output output = new ATN_Output("NP");
		
		int state = 0;
		boolean done = false;
		List<String> lastNum = new ArrayList<String>();
		
		while (!done && words.size() > 0)
		{
			String symbol = words.get(0);
			System.out.println("NP -> Processing word " + symbol);
			LexiconWord word = getWordFromLexicon(symbol);
			
			
			assert word != null;
			assert state <= arcs.size()-1;
			
			List<String> currentArcs = arcs.get(state);
			int arcIndex = 0;
			int lastState = state;
			
			while (!done && state == lastState && arcIndex < currentArcs.size())
			{
				String arcLabel = currentArcs.get(arcIndex);
				arcIndex++;
				System.out.println("NP -> ArcIndex = " + arcIndex);
				
				switch (state)
				{
					case 0: //NODE IS EQUAL TO S
						if (word.isType(arcLabel)) //arc is a word, not a sub ATN
						{
							if (arcLabel.equals("NAME"))
							{
								output.addFeature("NAME", word.getWord());
								output.addFeature("NUM", "3s");
								lastNum.clear();
								lastNum.add("3s");
								words.remove(0);
								//output.addFeatureValues("NUM", word.getValueFromFeature("NUM"));
								state = 2;
							}
							else if (arcLabel.equals("NOUN"))
							{
								List<String> numWord = word.getValueFromFeature("NUM");
								if (numWord != null && numWord.size() == 1 && numWord.get(0) == "3p")
								{
									output.addFeature("NOUN", word.getWord());
									output.addFeature("NUM", "3p");
									lastNum.clear();
									lastNum.add("3s");
									words.remove(0);
									state = 2;
								}
								else
								{
									//System.out.println("ERROR in ATN_NP! Failed to find NUM for NOUN " + symbol);
								}
							}
							else if (arcLabel.equals("PRONOUN"))
							{
								List<String> numWord = word.getValueFromFeature("NUM");
								if (numWord != null)
								{
									output.addFeature("PRONOUN", word.getWord());
									output.addFeatureValues("NUM", numWord);
									lastNum.addAll(numWord);
									words.remove(0);
									state = 2;
								}
								else
								{
									System.out.println("ERROR in ATN_NP! Failed to find NUM for PRONOUN " + symbol);
								}
							}
							else if (arcLabel.equals("ARTICLE"))
							{
								List<String> numWord = word.getValueFromFeature("NUM");
								if (numWord != null)
								{
									output.addFeature("ARTICLE", word.getWord());
									output.addFeatureValues("NUM", numWord);
									lastNum.addAll(numWord);
									//System.out.println("lastNum is now: " + lastNum);
									words.remove(0);
									state = 1;
								}
								else
								{
									System.out.println("ERROR in ATN_NP! Failed to find NUM for ARTICLE " + symbol);
								}
							}
						}
						
						break;
					case 1:
						if (word.isType(arcLabel)) //arc is a word, not a sub ATN
						{
							if (arcLabel.equals("ADJECTIVE"))
							{
								output.addFeature("ADJECTIVE", word.getWord());
								words.remove(0);
								//output.addFeatureValues("NUM", word.getValueFromFeature("NUM"));
								state = 1;
							}
							else if (arcLabel.equals("NOUN"))
							{
								List<String> numWord = word.getValueFromFeature("NUM");
								
								if (numWord != null && lastNum.size() > 0)
								{
									List<String> intersectNum = new ArrayList<String>();
									
									for (String v : lastNum)
									{
										if (numWord.contains(v))
											intersectNum.add(v);
									}
									if (intersectNum.size() > 0)
									{
										output.addFeature("NOUN", word.getWord());
										output.addFeatureValues("NUM", intersectNum);
										words.remove(0);
										state = 2;
									}
									else
									{
										System.out.println("NUM for NOUN " + word.getWord() + " does not match NUM for ARTICLE.");
										System.out.println("Num for NOUN: " + numWord);
										System.out.println("NUM for ARTICLE: " + lastNum);
									}
								}
								else
								{
									System.out.println("ERROR in ATN_NP! Failed to find NUM for NOUN " + word.getWord());
									//System.out.println("NUM for ARTICLE: " + lastNum);
									//done = true;
								}
							}
						}
						break;
					case 2:
						if (arcLabel.equals("DONE"))
							done = true;
						break;
					default:
						System.out.println("ERROR! How did we get here? ATN_NP");
						break;
				}
			}
			
			System.out.println("NP -> All arcs viewed. State went from " + lastState + " to " + state);
		}
		
		return output;
	}
}
