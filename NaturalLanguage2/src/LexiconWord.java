import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LexiconWord
{
	/*
	a) Word
		>what is the word to look for?
	b) Type (generally just one type?)
		>verb
		>name
		>wh-pronoun
		>article
		>preposition
		>adjective
		>adverb
	c) Feature (list of features with a list of corresponding values?)
		>NUM
		>TENSE
		>TYPE
		>VERB
		>ROOT
	d) Values
		>Associated with feature
		>May be a list of values
		>NUM
			-3s
			-3p
			-1s
			-1p
			-2s
			-2p
		>TENSE
			-past
			-present
			-infinitive
			-
	 */
	
	String word;
	//ArrayList<WordType> enumTypes;
	//ArrayList<String> types;
	//ArrayList<WordFeature> features;
	ArrayList<WordType> types;
	
	public LexiconWord()
	{
		word = null;
		types = new ArrayList<WordType>();
		//types = new ArrayList<String>();
		//enumTypes = new ArrayList<WordType>();
		//features = new ArrayList<WordFeature>();
	}
	
	public void setWord(String w)
	{
		word = w;
	}
	
	public String getWord()
	{
		String wordStr = word;
		if (types.size() > 0)
		{
			WordType type = types.get(0);
			List<String> features = getValueFromFeature(type.typeName);
			if (features != null && features.size() > 0)
				wordStr = features.get(0);
		}
		
		
		return wordStr;
	}
	
	public WordType getType(String typeName)
	{
		WordType type = null;
		for (WordType t : types)
		{
			if (t.getType().equals(typeName))
				type = t;
		}
		
		return type;
	}
	
	public boolean isType(String typeName)
	{
		boolean found = false;
		for (WordType t : types)
		{
			if (t.getType().equals(typeName))
				found = true;
		}
		
		return found;
	}
	
	private boolean isFeatureValue(WordType type, String fStr, String value)
	{
		boolean result = false;
		if (type != null)
		{
			WordFeature feature = type.getFeature(fStr);
			if (feature != null)
			{
				List<String> values = feature.getValues();
				for (String v : values)
				{
					if (v.equals(value))
						result = true;
				}
			}
		}
		
		return result;
	}
	
	public boolean isFeatureValue(String typeName, String fStr, String value)
	{
		WordType type = getType(typeName);
		return isFeatureValue(type, fStr, value);
	}
	
	public boolean isFeatureValue(String fStr, String value)
	{
		if (types.size() == 0)
			return false;
		
		WordType type = types.get(0);
		
		return isFeatureValue(type, fStr, value);
	}
	
	public boolean addType(WordType t)
	{
		return !types.contains(t) && types.add(t);
	}
	
	public List<String> getValueFromFeature(String fStr)
	{
		WordFeature feature = getFeature(fStr);
		if (feature == null)
			return null;
		else
			return feature.getValues();
	}
	
	public WordFeature getFeature(String fStr)
	{
		if (types.size() == 0)
			return null;
		
		WordType type = types.get(0);
		
		WordFeature feature = type.getFeature(fStr);
		
		return feature;
	}
	
	public WordFeature getFeature(String typeStr, String fStr)
	{
		WordType type = getType(typeStr);
		if (type != null)
		{
			WordFeature feature = type.getFeature(fStr);
			if (feature != null)
				return feature;
		}
		
		return null;
	}
	
	public WordType getLastType()
	{
		WordType lastType = null;
		if (types.size() > 0)
			lastType = types.get(types.size()-1);
		
		return lastType;
	}
	
	//NOTE: Should make this return an Iterator?
	public ArrayList<WordType> getTypes()
	{
		return types;
	}
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append(word);
		for (WordType t : types)
		{
			sb.append("\n");
			sb.append(t.toString());
		}
		
		//if (sb.length() > 0)
		//	sb.setLength(sb.length()-1);
		
		return sb.toString();
	}
	
	/*
	public boolean addType(String type)
	{
		return !types.contains(type) && types.add(type);
	}
	
	
	public boolean addType(WordType enumType)
	{
		return !enumTypes.contains(enumType) && enumTypes.add(enumType);
	}
	*/
	
	
	
	/*
	@Override
	public boolean equals(Object obj)
	{
		
	}
	*/
	
}
