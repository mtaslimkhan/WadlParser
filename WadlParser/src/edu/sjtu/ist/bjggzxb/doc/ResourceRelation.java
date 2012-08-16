package edu.sjtu.ist.bjggzxb.doc;

import java.util.ArrayList;

public class ResourceRelation {
	
	public String provider;
	public String name;
	public ArrayList<KeyPair> keypairs;
	
	public ResourceRelation(){
		provider = "uknown";
		name = "uknown";
		keypairs = new ArrayList<KeyPair>();
		KeyPair keypair = new KeyPair();
		keypairs.add(keypair);
	}
}
