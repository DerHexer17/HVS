package com.example.datahandling;

import java.util.Comparator;

public class TabellenplatzComparator implements Comparator<Tabellenrang> {

	@Override
	public int compare(Tabellenrang t1, Tabellenrang t2) {
		//-1 wenn t1 kleiner t2
		//+1 wenn t1 größer t2
		
		if(t1.getPunktePositiv()>t2.getPunktePositiv()){
			return 1;
		}else if(t1.getPunktePositiv()==t2.getPunktePositiv()){
			if(t1.getPunkteNegativ()<t2.getPunkteNegativ()){
				return 1;
			}else if(t1.getPunkteNegativ()==t2.getPunkteNegativ()){
				if(t1.getTorePositiv()-t1.getToreNegativ()>t2.getTorePositiv()-t2.getToreNegativ()){
					return 1;
				}else if(t1.getTorePositiv()>t2.getTorePositiv()){
					return 1;
				}else{
					return -1;
				}
			}else{
				return -1;
			}
		}else{
			return -1; 
		}
		

		
	}

}
