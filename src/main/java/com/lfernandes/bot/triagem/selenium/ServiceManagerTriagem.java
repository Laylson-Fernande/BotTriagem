package com.lfernandes.bot.triagem.selenium;

import java.io.IOException;

public class ServiceManagerTriagem extends ServiceManagerBase {

    public boolean downloadListOfIncidentsToAssign(){
        try {
            Authentication();
            System.out.println("Baixar lista de incidentes para atribuir");
            System.out.println("Selecionar Fila: Incidente");
            clickButtonByIdInFrame("X5ButtonDiv","detail.do");
            clickButtonByIdInFrame("X5Popup_3","detail.do");

            waitForPageToLoad(5);
            System.out.println("Selecionar Exibição: Incidents Assigned to My Groups by Group");
            clickButtonByIdInFrame("X7ButtonDiv","detail.do");
            clickButtonByIdInFrame("X7Popup_4","detail.do");
            //getDriver().get(s"https://app.mapfre.com/smbbex/detail.do?async=1");

            waitForPageToLoad(5);
            //clickButtonById("ext-comp-843735");
            clickButtonByClass("x-btn-arrow x-unselectable");
            waitMillis(500);
            clickButtonByClass("x-menu-item-text");
            waitSeconds(1);
            clickButtonByIdInFrame("X8Edit", "tpz_container.jsp");
            clickButtonByIdInFrame("X21Border","tpz_container.jsp");
            return renameLatestExportFileTo("AssignedToGroup.csv", 10);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            getDriver().quit();
        }     
    }

    public boolean redirectIncidentTo(String incident, String designado) {
        try {
            Authentication();
            waitSeconds(2);
            selectIncident(incident);
            waitSeconds(2);
            setUserDesignado(designado);

            return true;
        } catch (Exception e) {
            //e.printStackTrace();
            System.err.println("Erro ao tentar redirecionar incidente: " +e.getMessage());
            try {
                System.in.read();
            } catch (IOException e1) {
            }
            return false;
        } finally {
            getDriver().quit();
        } 
    }

    private void selectIncident(String incident) throws Exception {
        try {
            clickButtonById("ROOT/Gerenciamento de Incidentes");
            waitMillis(500);
            clickButtonById("ROOT/Gerenciamento de Incidentes/Pesquisar Incidentes");

            waitForPageToLoad(5);
            switchDriveToFrame("src","/smbbex/cwc/nav.menu?name=navStart&id=ROOT%2FGerenciamento%20de%20Incidentes%2FPesquisar%20Incidentes");
            setValueToInputTextByName("instance/number", incident);
            switchDriveToDefault();
            clickButton("aria-keyshortcuts", "Ctrl+Shift+F6");
            waitForPageToLoad(10);
        } catch (Exception e) {
            System.err.println("Erro ao tentar selecionar incidente: " +e.getMessage());
            throw e;
        }
    }

    private void setUserDesignado(String user) throws Exception{
        try {
            switchDriveToFrame("src","/smbbex/cwc/nav.menu?name=navStart&id=ROOT%2FGerenciamento%20de%20Incidentes%2FPesquisar%20Incidentes");
            
            clickButtonXPath("a","Categorização e Designação");
            setValueToInputTextByName("instance/assignee.name", user);

            clickButtonXPath("a","Atividades");
            clickButtonByClass("mandatoryFieldStyle xEdit file_probsummary field_apm_activity");
            setValueToInputTextByName("var/pmc.actions/pmc.actions", "Designado via BotTriagem para "+user);
            switchDriveToDefault();
        } catch (Exception e) {
            System.err.println("Erro ao tentar designar item: " +e.getMessage());
            throw e;
        }
    }
}
