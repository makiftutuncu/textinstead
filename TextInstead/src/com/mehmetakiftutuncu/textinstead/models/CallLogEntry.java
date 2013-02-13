package com.mehmetakiftutuncu.textinstead.models;

/**
 * Model class for a single call information read from device's call log
 * 
 * @author Mehmet Akif Tütüncü
 */
public class CallLogEntry
{
	private String id;
	private String number;
	private String date;
	private String duration;
	private String callType;
	private String name;
	
	public CallLogEntry(String id, String number, String date, String duration, String callType, String name)
	{
		setId(id);
		setNumber(number);
		setDate(date);
		setDuration(duration);
		setCallType(callType);
		setName(name);
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getNumber()
	{
		return number;
	}

	public void setNumber(String number)
	{
		this.number = number;
	}

	public String getDate()
	{
		return date;
	}

	public void setDate(String date)
	{
		this.date = date;
	}

	public String getDuration()
	{
		return duration;
	}

	public void setDuration(String duration)
	{
		this.duration = duration;
	}

	public String getCallType()
	{
		return callType;
	}

	public void setCallType(String callType)
	{
		this.callType = callType;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@Override
	public String toString()
	{
		return String.format("CallLogEntry [id=%s, number=%s, date=%s, duration=%s, callType=%s, name=%s]", id, number, date, duration, callType, name);
	}
}