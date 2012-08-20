package edu.sjtu.ist.bjggzxb.doc;

import java.util.ArrayList;

import com.google.gson.annotations.Since;

public class ResourceRelation {
	
	@Since(1.0)
	public String provider;
	@Since(1.0)
	public String name;
	@Since(1.0)
	public String uri;
	@Since(1.0)
	public ArrayList<KeyPair> keypairs;
}
