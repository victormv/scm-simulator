package br.gov.ifpb.scm.util;

public class SequentialIncrement 
{
	private static SequentialIncrement instance;
	private long sequential = 0;
	
	private SequentialIncrement() {}
	
	public static SequentialIncrement getInstance() {
		if(SequentialIncrement.instance == null) {
			SequentialIncrement.instance = new SequentialIncrement();
		}
		return SequentialIncrement.instance;
	}
	
	public long getSequential() {
		this.sequential++;
		return this.sequential;
	}
}
