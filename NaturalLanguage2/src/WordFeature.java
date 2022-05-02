import java.util.ArrayList;
import java.util.List;

public class WordFeature
{
	String feature;
	List<String> values;
	
	public WordFeature()
	{
		values = new ArrayList<String>();
		feature = null;
	}
	
	//Note: I should really just use an Iterator for this...
	public List<String> getValues()
	{
		return values;
	}
	
	public String getFeature()
	{
		return feature;
	}
	
	public void setFeature(String newFeature)
	{
		feature = newFeature;
	}
	
	public boolean addValue(String val)
	{
		return !values.contains(val) && values.add(val);
	}
	
	public boolean addValues(ArrayList<String> vals)
	{
		boolean result = true;
		for (String v : vals)
		{
			if (result && !values.contains(v))
				result = result && values.add(v);
		}
		
		return result;
	}
	
	public boolean containsValue(String val, boolean isOnlyValue)
	{
		boolean preCondition = true;
		if (isOnlyValue)
			preCondition = values.size() == 1;
		
		return preCondition && values.contains(val);
	}
	
	public boolean containsValue(String val)
	{
		return containsValue(val, false);
	}
	
	public boolean removeValue(String val)
	{
		return values.remove(val);
	}
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append(feature);
		for (String val : values)
		{
			sb.append("\n\t");
			sb.append(val);
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
		
		if (!(obj instanceof WordFeature))
			return false;
		
		WordFeature wf = (WordFeature) obj;
		
		return (feature.equals(wf.getFeature()));
	}
}
