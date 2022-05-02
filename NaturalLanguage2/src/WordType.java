import java.util.ArrayList;
import java.util.Arrays;

/*
public enum WordTypeEnum
{
	/*
	>verb
	>name
	>wh-pronoun
	>article
	>preposition
	>adjective
	>adverb
	/
	
	NAME,
	NOUN,
	PRONOUN,
	WH_PRONOUN,
	
	VERB,
	ADVERB,
	
	ARTICLE,
	PREPOSITION
}
*/

public class WordType
{
	String typeName;
	ArrayList<WordFeature> features;
	
	public WordType(String tName)
	{
		typeName = tName;
		features = new ArrayList<WordFeature>();
	}
	
	public WordType()
	{
		this(null);
	}
	
	public void setType(String tName)
	{
		typeName = tName;
	}
	
	public String getType()
	{
		return typeName;
	}
	
	//NOTE: Probably should make this private.
	public ArrayList<WordFeature> getFeatures()
	{
		return features;
	}
	
	public WordFeature getFeature(String fName)
	{
		WordFeature wFeature = null;
		for (WordFeature wf : features)
		{
			if (fName.equals(wf.getFeature()))
				wFeature = wf;
		}
		
		return wFeature;
	}
	
	public boolean addFeature(WordFeature wFeature)
	{
		return features.add(wFeature);
	}
	
	public boolean addFeatures(String fName, ArrayList<String> values)
	{
		WordFeature wFeature = getFeature(fName);
		boolean added = true;
		
		if (wFeature == null)
		{
			wFeature = new WordFeature();
			wFeature.setFeature(fName);
			added = addFeature(wFeature);
		}
		
		return added && wFeature.addValues(values);
	}
	
	public boolean addFeatures(String fName, String[] values)
	{
		return addFeatures(fName, new ArrayList<String>(Arrays.asList(values)));
	}
	
	public boolean addFeature(String fName, String value)
	{
		
		WordFeature wFeature = getFeature(fName);
		boolean added = true;
		
		if (wFeature == null)
		{
			wFeature = new WordFeature();
			wFeature.setFeature(fName);
			added = addFeature(wFeature);
		}
		
		return added && wFeature.addValue(value);
	}
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append(typeName);
		for (WordFeature f : features)
		{
			sb.append("\n");
			sb.append(f.toString().replaceAll("(?m)^", "\t"));
			//sb.append("\n");
		}
		
		//if (sb.length() > 0)
		//	sb.setLength(sb.length()-1);
		
		return sb.toString();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj == this)
			return true;
		
		if (!(obj instanceof WordType))
			return false;
		
		WordType wt = (WordType) obj;
		
		return typeName.equals(wt.getType()) && features.equals(wt.getFeatures());
	}
}