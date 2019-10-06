/*
 * Copyright (c) 2019 IBM Corporation.
 */
package dev.galasa.zos3270.internal.datastream;

import java.nio.ByteBuffer;

public class AttributeExtendedHighlighting implements IAttribute {
	
	public final static byte ATTRIBUTE_ID = 0x41;
	
	private final byte colour;
	
	public AttributeExtendedHighlighting(ByteBuffer buffer) {
		this.colour = buffer.get();
	}

}
