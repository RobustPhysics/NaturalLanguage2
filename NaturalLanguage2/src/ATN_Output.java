import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


/*
This class is what the ATN returns when it reaches 'done'

So if we have the output:
(S (MOOD declarative) (NUM 3s)
	(SUBJ (NP (NAME Mary)))
	(VERB (V (VERB give) (TENSE past)))
	(IND-OBJ (NP (PRONOUN me) (NUM 1s)))
	(OBJ (NP (DET a) (ADJS (new)) (NOUN picture))))

Then...
ATN_Output.label = S
ATN_Output.list = {
	ATN_Element("MOOD", {"declarative"}),
	ATN_Element("NUM", {"3s"})
}
ATN_Output.children = {
	//SUBJ
	ATN_Output(
		label = "SUBJ",
		list = {},
		children = {
			ATN_Output(
				label = "NP",
				list = {
					ATN_Element("NAME", {"Mary"})
				},
				children = {}
			)
		}
	),
	
	//VERB
	ATN_Output(
		label = "VERB",
		list = {},
		children = {
			ATN_Output(
				label = "VERB",
				list = {
					ATN_Element("VERB", {"give"}),
					ATN_Element("TENSE", {"past"})
				},
				children = {}
			)
		}
	),
	
	
	ATN_Output(
		label = "IND-OBJ",
		list = {},
		children = {
			ATN_Output(
				label = "NP",
				list = {
					ATN_Element("PRONOUN", {"me"}),
					ATN_Element("NUM", {"1s"})
				},
				children = {}
			)
		}
	),
	
	
	ATN_Output(
		label = "OBJ",
		list = {},
		children = {
			ATN_Output(
				label = "NP",
				list = {
					ATN_Element("DET", {"me"}),
					ATN_Element("ADJS", {"1s"}),
					ATN_Element("NOUN", {"picture"})
				},
				children = {}
			)
		}
	)
}



NOTE: When displaying the output of everything...perhaps add a method to disable flags that are equal to
Already output flags?
For example...

S has the list.num = 3s
If the subj and verb also have list.num = 3s, then...
Don't print those flags?
*/
public class ATN_Output
{
	String label;
	List<ATN_Element> list;
	List<ATN_Output> children;
	boolean failed = false;
	
	public ATN_Output(String label)
	{
		this.label = label;
		list = new ArrayList<ATN_Element>();
		children = new ArrayList<ATN_Output>();
		//outputOrder = new ArrayList<String>();
	}
	
	public ATN_Output()
	{
		this(null);
	}
	
	public void addChild(ATN_Output child)
	{
		children.add(child);
	}
	
	private void addFeatureValues(String feature, List<String> values, boolean overwrite)
	{
		ATN_Element newFeature = findFeatureInList(feature);
		if (newFeature == null)
		{
			newFeature = new ATN_Element(feature, values);
			list.add(newFeature);
		}
		else
		{
			if (overwrite)
				newFeature.values.clear();
			newFeature.values.addAll(values);
		}
	}
	
	public void setFeature(String feature, List<String> values)
	{
		addFeatureValues(feature, values, true);
	}
	
	public void setFeature(String feature, String value)
	{
		List<String> values = new ArrayList<String>();
		values.add(value);
		addFeatureValues(feature, values, true);
	}
	
	public void addFeatureValues(String feature, List<String> values)
	{
		addFeatureValues(feature, values, false);
	}
	
	public void addFeature(String feature, String value)
	{
		List<String> values = new ArrayList<String>();
		values.add(value);
		addFeatureValues(feature, values);
	}
	
	protected ATN_Element findFeatureInList(String feature)
	{
		ATN_Element result = null;
		for (ATN_Element element : list)
		{
			if (element.feature.equals(feature))
				result = element;
		}
		
		return result;
	}
	
	protected ATN_Element findFeatureInOutput(String feature, boolean searchChildren)
	{
		ATN_Element result = null;
		
		Queue<ATN_Output> childrenToSearch = new ArrayDeque<ATN_Output>();
		childrenToSearch.add(this);
		//System.out.println("Seaching " + this.label + " for feature " + feature);
		
		while (!childrenToSearch.isEmpty())
		{
			ATN_Output child = childrenToSearch.remove();
			//System.out.println("\tNext child = " + child.label);
			ATN_Element element = child.findFeatureInList(feature);
			//System.out.println("\t\tElement = " + element);
			if (element != null)
			{
				result = element;
				childrenToSearch.clear();
			}
			else if (searchChildren)
			{
				childrenToSearch.addAll(child.children);
			}
		}
		
		return result;
	}
	
	protected ATN_Element findFeatureInOutput(String feature)
	{
		return findFeatureInOutput(feature, true);
	}
	
	public ATN_Output findChild(String label, boolean recursiveSearch)
	{
		ATN_Output result = null;
		
		Queue<ATN_Output> childrenToSearch = new ArrayDeque<ATN_Output>();
		childrenToSearch.add(this);
		//System.out.println("Seaching " + this.label + " for " + label);
		
		while (!childrenToSearch.isEmpty())
		{
			ATN_Output child = childrenToSearch.remove();
			//System.out.println("Next child = " + child.label);
			
			if (child.label.equals(label))
			{
				result = child;
				childrenToSearch.clear();
			}
			else if (recursiveSearch)
			{
				childrenToSearch.addAll(child.children);
			}
		}
		
		return result;
	}
	
	public ATN_Output findChild(String label)
	{
		return findChild(label, true);
	}
	
	public List<String> getValuesFromFeature(String feature, boolean searchChildren)
	{
		List<String> values = null;
		ATN_Element element = null;
		
		Queue<ATN_Output> childrenToSearch = new ArrayDeque<ATN_Output>();
		childrenToSearch.add(this);
		
		while (!childrenToSearch.isEmpty())
		{
			ATN_Output child = childrenToSearch.remove();
			element = child.findFeatureInList(feature);
			if (element != null)
			{
				values = element.values;
				childrenToSearch.clear();
			}
			else if (searchChildren)
			{
				childrenToSearch.addAll(child.children);
			}
		}
		
		
		return values;
	}
	
	public List<String> getValuesFromFeature(String feature)
	{
		return getValuesFromFeature(feature, true);
	}
	
	public String getFirstValueFromFeature(String feature, boolean searchChildren)
	{
		List<String> vals = getValuesFromFeature(feature, searchChildren);
		String value = null;
		if (vals != null && vals.size() > 0)
		{
			value = vals.get(0);
		}
		
		return value;
	}
	
	public String getFirstValueFromFeature(String feature)
	{
		return getFirstValueFromFeature(feature, false);
	}
	
	public String toString()
	{
		StringBuilder output = new StringBuilder();
		
		output.append("(");
		output.append(label);
		output.append(" ");
		for (ATN_Element element : list)
		{
			output.append(" ");
			output.append(element);
		}
		if (list.size() > 0)
			output.append("\n");
		
		for (ATN_Output child : children)
		{
			output.append("\t");
			output.append(child);
		}
		
		output.append(")");
		
		return output.toString();
	}
	
	/*
	if maxDepth > 0, then it will search children for the feature at maxDepth-1
	*/
	
	/*
	List<String> values = null;
	ATN_Element element = findFeatureInList(feature);
	if (element != null)
	{
		values = element.values;
	}
	else if (searchChildren)
	{
	
		//Queue<ATN_Output> childrenToSearch = getChildrenAsQueue();
		
		
		
		while (childrenToSearch.size() > 0)
		{
			ATN_Output nextChild = childrenToSearch.remove();
			
		}
		
	}
	*/
	/*
	public List<String> getValuesFromFeature(String feature, int maxDepth)
	{
		//This is only to track the current recursive depth
		ATN_Output.recursiveDepth = maxDepth;
		
		List<String> values = null;
		ATN_Element element = findFeatureInList(feature);
		if (element != null)
		{
			values = element.values;
		}
		else if (maxDepth > 0)
		{
			List<String> subValues = null;
			int shortestDepth = 999;
			for (ATN_Output child : children)
			{
				subValues = child.getValuesFromFeature(feature, maxDepth-1);
				if (subValues != null)
				{
					
				}
			}
			values = subValues;
		}
		
		return values;
	}
	*/
}
