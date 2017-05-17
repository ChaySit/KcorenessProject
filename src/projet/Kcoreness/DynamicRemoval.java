package peersim.dynamics;

import peersim.core.Network;
import projet.Kcoreness.*;
import peersim.cdsim.CDProtocol;
public class DynamicRemoval extends DynamicNetwork {

	static boolean isRemoved = false;

	public DynamicRemoval(String prefix){
		super(prefix);
	}


	/*
	 * Removes a node given a current index 
	 * */
	public void removebyIndex(int i){

		Network.swap(i, Network.size()-1);
		Network.remove();

	}

	/*
	 * Removes a node given a given node ID 
	 * @Returns 0 if removed 1 otherwise
	 * */

	public int removebyID(int ID){
		int	 i = Network.size()-1;
		int	Id = Integer.MIN_VALUE;
		KcorenessFunction protocol = null;


		while(i>=0 && Id!=ID){
			Id = (int)Network.get(i).getID();
			i++;

		}

		if(Id!=Integer.MIN_VALUE){
			Network.remove(i);
			for(int j=0 ;j<Network.size();j++){
				protocol = (KcorenessFunction)Network.get(i).getProtocol(1);
				protocol.getEstimation().remove(ID);
			}
			return 0;
		}else

			return 1;


	}

	/*
	 * Removes a given layer based on a particular coreness value
	 * @Returns number of removed nodes
	 * */

	public int removeCorenessLayer(int corenessValue){

		KcorenessFunction protocol = null ;
		int nbRemoved = 0;

		for(int i=0;i<Network.size();i++){

			protocol = (KcorenessFunction)Network.get(i).getProtocol(i);
			if(protocol.getCoreness()==corenessValue){
				removebyID((int)Network.get(i).getID());
				nbRemoved++;
			}
		}

		return nbRemoved;

	}






}

