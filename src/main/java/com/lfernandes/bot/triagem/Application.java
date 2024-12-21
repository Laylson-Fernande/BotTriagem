package com.lfernandes.bot.triagem;

import com.lfernandes.bot.triagem.selenium.ServiceManagerTriagem;

public class Application {

	public static void main(String[] args) {
		// ServiceManagerBase smBase = new ServiceManagerBase();
		// smBase.Authentication();
		
		try {
			ServiceManagerTriagem triagem = new ServiceManagerTriagem();
			triagem.downloadListOfIncidentsToAssign();
			//triagem.redirectIncidentTo("IM04656305", "LLFERNAN@BBMAPFRE.COM.BR");
			//triagem.redirectIncidentTo("IM04672075", "LLFERNAN@BBMAPFRE.COM.BR");
			//triagem.Authentication();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
