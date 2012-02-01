package org.openehealth.ipf.tutorials.basic

public class MyConfiguration  {

    public static String inputFileName;
    public static String inputDirectory;
    public static String ehcpAdtUrl;


    public  String getInputFileName() {
        return inputFileName;
    }

    public  void setInputFileName(String inputFileName) {
        MyConfiguration.inputFileName = inputFileName;
    }

    public  String getInputDirectory() {
        return inputDirectory;
    }

    public  void setInputDirectory(String inputDirectory) {
        MyConfiguration.inputDirectory = inputDirectory;
    }

    public  String getEhcpAdtUrl() {
        return ehcpAdtUrl;
    }

    public  void setEhcpAdtUrl(String ehcpAdtUrl) {
        MyConfiguration.ehcpAdtUrl = ehcpAdtUrl;
    }
}
