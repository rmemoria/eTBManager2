package org.msh.tb.na.entities.enums;

public enum Dot {
	N, 
	O,
	X;
	
	public String getKey() {
		return getClass().getSimpleName().concat("." + name());
	}
}
