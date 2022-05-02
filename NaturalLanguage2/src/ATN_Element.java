import java.util.ArrayList;
import java.util.List;

public class ATN_Element
{
	String feature;
	List<String> values;
	
	public ATN_Element(String feature, List<String> values)
	{
		this.feature = feature;
		this.values = values;
	}
	
	public ATN_Element(String feature, String value)
	{
		this.feature = feature;
		values = new ArrayList<String>();
		values.add(value);
	}
	
	public ATN_Element(String feature)
	{
		this.feature = feature;
		values = new ArrayList<String>();
	}
	
	public ATN_Element()
	{
		this(null);
	}
	
	
	public ATN_Element getIntersection(List<String> vals)
	{
		ATN_Element newValue = new ATN_Element(this.feature);
		
		for (String v : vals)
		{
			if (values.contains(v))
				newValue.values.add(v);
		}
		
		return newValue;
	}
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append("(");
		sb.append(feature);
		sb.append(" ");
		if (values.size() == 0)
			sb.append("null");
		else if (values.size() == 1)
			sb.append(values.get(0));
		else
		{
			sb.append("{");
			for (String val : values)
			{
				sb.append(val);
				sb.append(", ");
			}
			sb.setLength(sb.length()-2);
			sb.append("}");
		}
		
		sb.append(")");
		
		return sb.toString();
	}
}
