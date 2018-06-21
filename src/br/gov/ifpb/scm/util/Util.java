package br.gov.ifpb.scm.util;

public class Util 
{
	public static int getRandom(int min, int max)
	{
		return (int) ((Math.random() * ((max+1)-min)) + min);
	}
}
